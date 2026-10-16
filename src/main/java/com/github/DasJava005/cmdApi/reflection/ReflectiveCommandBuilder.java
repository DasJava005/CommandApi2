package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.Command;
import com.github.DasJava005.cmdApi.CommandArguments;
import com.github.DasJava005.cmdApi.CommandExecutor;
import com.github.DasJava005.cmdApi.CommandInfo;
import com.github.DasJava005.cmdApi.input.Argument;
import com.github.DasJava005.cmdApi.input.LiteralArgument;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.validation.CommandMethodValidator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public final class ReflectiveCommandBuilder {

    private final InputArgumentRegistry inputArgumentRegistry;
    private final CommandMethodValidator commandMethodValidator = new CommandMethodValidator();

    public ReflectiveCommandBuilder(InputArgumentRegistry inputArgumentRegistry) {
        this.inputArgumentRegistry = inputArgumentRegistry;
    }

    public Set<Command> buildCommands(Object instance) {
        if(!instance.getClass().isAnnotationPresent(CommandGroup.class))
            throw new IllegalArgumentException("Class is not annotated with @CommandGroup");

        Set<Command> commands = new HashSet<>();

        for(Method method : instance.getClass().getDeclaredMethods()) {
            if(!method.isAnnotationPresent(Cmd.class)) {
                continue;
            }
            CommandMethod commandMethod = new CommandMethod(instance, method);
            var validationResult = commandMethodValidator.validate(commandMethod, inputArgumentRegistry);

            switch (validationResult) {
                case CommandMethodValidator.Success success -> commands.add(buildCommand(success.getValidatedCommandMethod()));
                case CommandMethodValidator.Fail fail -> System.out.println("Could not build command '"
                        + method.getAnnotation(Cmd.class).value()
                        + "' REASON:"
                        + fail.exception().getMessage());
            }
        }

        return commands;
    }

    private Command buildCommand(CommandMethodValidator.ValidatedCommandMethod commandMethod) {
        final String commandName  = commandMethod.instance().getClass().getAnnotation(CommandGroup.class).value();
        final CommandSyntax commandSyntax = commandMethod.getCommandSyntax();
        final Map<String, Parameter> parameters = commandMethod.getArgumentParameters();

        // build command arguments from syntax
        List<Argument> arguments = new ArrayList<>();

        for (CommandSyntax.CommandToken token : commandSyntax.tokens()) {
            if (token.isLiteral()) {
                arguments.add(new LiteralArgument(token.getToken()));
                continue;
            }
            if (token.isArgument()) {
                final String key = token.getToken();
                Parameter p = parameters.get(key);

                var inputArgument = inputArgumentRegistry.create(key, p.getType());
                arguments.add(inputArgument);
            }
        }

        CommandInfo commandInfo = new CommandInfo(commandName, List.of(), "", "");
        CommandArguments commandArguments = new CommandArguments(arguments);
        Class<?> commandSender = commandMethod.getSenderParameter().getType();
        CommandExecutor commandExecutor = new ReflectiveExecutor(commandMethod);

        return new Command(commandInfo, commandArguments, commandSender, commandExecutor);
    }

}