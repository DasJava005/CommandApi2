package com.github.DasJava005.cmdApi.reflection.validation;

import com.github.DasJava005.cmdApi.reflection.CommandMethod;
import com.github.DasJava005.cmdApi.reflection.CommandSyntax;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.*;

public record CommandMethodValidator() {

    public CommandMethodValidator() {

    }

    public void validate(CommandMethod commandMethod) {
        validateSender(commandMethod);
        validateArguments(commandMethod);

        // TODO create class: ValidatedCommandMethod and return object of it
    }

    public void validateSender(CommandMethod commandMethod) {
        Set<Parameter> senderAnnotatedParams = commandMethod.getSenderAnnotatedParameters();

        if(senderAnnotatedParams.size() > 1) {
            throw new CommandValidationException(commandMethod, "Only one parameter may be annotated with @Sender");
        } else if(senderAnnotatedParams.isEmpty()) {
            throw new CommandValidationException(commandMethod, "Command method must have exactly one @Sender annotated parameter");
        }

        Parameter sender = senderAnnotatedParams.iterator().next();

        if(!CommandSender.class.isAssignableFrom(sender.getType())) {
            throw new CommandValidationException(commandMethod, "@Sender annotated parameter must be a CommandSender or one of its subtypes");
        }
    }

    public void validateArguments(CommandMethod commandMethod) {
        CommandSyntax syntax = commandMethod.getCommandSyntax();
        HashMap<String, List<Parameter>> argAnnotatedParams = commandMethod.getArgAnnotatedParameters();

        int definedArguments = syntax.getArgumentTokens().size();
        int providedArguments = argAnnotatedParams.size();

        if (syntax.getArgumentTokens().size() != providedArguments) {
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

    public void validateArgumentTypes() {

    }

}
