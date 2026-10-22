package com.github.DasJava005.cmdApi.reflection.validation;

import com.github.DasJava005.cmdApi.reflection.CommandMethod;
import com.github.DasJava005.cmdApi.reflection.CommandSyntax;
import com.github.DasJava005.cmdApi.reflection.InputArgumentRegistry;

import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class CommandMethodValidator {

    public CommandMethodValidator() {}

    public final ValidationResult validate(CommandMethod commandMethod, InputArgumentRegistry inputArgumentRegistry) {
        try {
            validateSender(commandMethod);
            validateArguments(commandMethod);
            validateArgumentTypes(commandMethod, inputArgumentRegistry);
        } catch(CommandValidationException exception){
            return ValidationResult.fail(exception);
        }

        var senderParam = commandMethod.getSenderAnnotatedParameters().getFirst();
        Map<String, Parameter> paramMap = new HashMap<>();
        for(Map.Entry<String, List<Parameter>> argParameters : commandMethod.getArgAnnotatedParameters().entrySet()){
            paramMap.put(argParameters.getKey(), argParameters.getValue().getFirst());
        }

        final ValidatedCommandMethod result = new ValidatedCommandMethod(commandMethod.instance(),
                commandMethod.method(),
                commandMethod.getCommandSyntax()
                ,senderParam, paramMap);

        return ValidationResult.success(result);
    }

    public void validateAnnotations(CommandMethod commandMethod) {
        for(Parameter p : commandMethod.parameters()){
            //TODO
        }
    }

    public void validateSender(CommandMethod commandMethod) throws CommandValidationException {
        List<Parameter> senderAnnotatedParams = commandMethod.getSenderAnnotatedParameters();

        if(senderAnnotatedParams.size() > 1) {
            throw new CommandValidationException(commandMethod, "Only one parameter may be annotated with @Sender");
        } else if(senderAnnotatedParams.isEmpty()) {
            throw new CommandValidationException(commandMethod, "Command method must have exactly one @Sender annotated parameter");
        }

        Parameter sender = senderAnnotatedParams.getFirst();

        if(!CommandSender.class.isAssignableFrom(sender.getType())) {
            throw new CommandValidationException(commandMethod, "@Sender annotated parameter must be a CommandSender or one of its subtypes");
        }
    }

    public void validateArguments(CommandMethod commandMethod) throws CommandValidationException {
        CommandSyntax syntax = commandMethod.getCommandSyntax();
        Map<String, List<Parameter>> argAnnotatedParams = commandMethod.getArgAnnotatedParameters();

        int definedArguments = syntax.getArgumentTokens().size();
        int providedArguments = argAnnotatedParams.size();

        if (definedArguments != providedArguments) {
            throw new CommandValidationException(commandMethod,
                    "The number of input arguments defined in the syntax " +
                    "does not match the number of @Arg-annotated parameters."
            );
        }

        List<CommandSyntax.CommandToken> inputTokens = syntax.getArgumentTokens();
        Set<CommandSyntax.CommandToken> uniqueTokens = new HashSet<>(inputTokens);

        if (uniqueTokens.size() < inputTokens.size()) {
            throw new CommandValidationException(commandMethod, "Syntax has duplicate argument key(s)");
        }

        for(var list : argAnnotatedParams.values()){
            if (list.size() > 1)
                throw new CommandValidationException(commandMethod, "Multiple parameters are annotated with @Arg for the same key!");
        }

        for(CommandSyntax.CommandToken token : uniqueTokens) {
            if(!argAnnotatedParams.containsKey(token.getToken()))
                throw new CommandValidationException(commandMethod, "Method signature requires parameter annotated with: @Arg(" + token.getToken() + ")");
        }
    }

    public void validateArgumentTypes(CommandMethod commandMethod, InputArgumentRegistry registry) throws CommandValidationException {
        List<Parameter> parameters = commandMethod.getArgAnnotatedParameters().values()
                .stream()
                .flatMap(Collection::stream)
                .toList();

        for(var param : parameters) {
            if(!registry.supports(param.getType())) {
                throw new CommandValidationException(commandMethod, "Registry does not provide support for type: " + param.getType());
            }
        }

        List<Parameter> greedyParameters = commandMethod.getGreedyAnnotatedParameters();
        if(greedyParameters.size() > 1) {
            throw new CommandValidationException(commandMethod, "Only one parameter may be annotated with @Greedy");
        }

        if(!greedyParameters.isEmpty()) {
            if(greedyParameters.getFirst().getType() != String.class) {
                throw new CommandValidationException(commandMethod, "Parameter annotated with @Greedy must be a String");
            }
        }
    }

    public static sealed interface ValidationResult permits Success, Fail {

        public static Success success(ValidatedCommandMethod commandMethod) {
            return new Success(commandMethod);
        }

        public static Fail fail(CommandValidationException exception) {
            return new Fail(exception);
        }
    }

    public static final class Success implements ValidationResult {

        private final ValidatedCommandMethod validatedCommandMethod;

        private Success(ValidatedCommandMethod validatedCommandMethod) {
            this.validatedCommandMethod = validatedCommandMethod;
        }

        public ValidatedCommandMethod getValidatedCommandMethod() {
            return validatedCommandMethod;
        }
    }

    public static final class Fail implements ValidationResult {

        private final CommandValidationException exception;

        private Fail(CommandValidationException exception) {
            this.exception = exception;
        }

        public CommandValidationException exception() {
            return exception;
        }
    }

    public static final class ValidatedCommandMethod {

        private final Object instance;
        private final Method commadMethod;
        private final CommandSyntax commandSyntax;
        private final Parameter sender;
        private final Map<String, Parameter> parameters;

        private ValidatedCommandMethod(Object instance,
                                       Method commandMethod,
                                       CommandSyntax commandSyntax,
                                       Parameter senderParameter,
                                       Map<String, Parameter> argParameters){
            this.instance = instance;
            this.commadMethod = commandMethod;
            this.commandSyntax = commandSyntax;
            this.sender = senderParameter;
            this.parameters = argParameters;
        }

        public Object instance() {
            return instance;
        }

        public Method getExecutorMethod() {
            return commadMethod;
        }

        public CommandSyntax getCommandSyntax() {
            return commandSyntax;
        }

        public Parameter getSenderParameter() {
            return sender;
        }

        public Map<String, Parameter> getArgumentParameters() {
            return parameters;
        }
    }

}
