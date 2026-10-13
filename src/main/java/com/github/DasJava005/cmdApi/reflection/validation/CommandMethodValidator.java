package com.github.DasJava005.cmdApi.reflection.validation;

import com.github.DasJava005.cmdApi.reflection.CommandMethod;
import com.github.DasJava005.cmdApi.reflection.CommandSyntax;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public record CommandMethodValidator() {

    public CommandMethodValidator() {

    }

    public void validate(CommandMethod commandMethod) {
        validateSender(commandMethod);
        validateArguments(commandMethod);
    }

    public void validateSender(CommandMethod commandMethod) {
        Set<Parameter> senderAnnotatedParams = commandMethod.getSenderAnnotatedParameters();

        if(senderAnnotatedParams.size() > 1) {
            throw new CommandValidationException("Only one parameter may be annotated with @Sender");
        } else if(senderAnnotatedParams.isEmpty()) {
            throw new CommandValidationException("Command method must have exactly one @Sender annotated parameter");
        }

        Parameter sender = senderAnnotatedParams.iterator().next();

        if(!CommandSender.class.isAssignableFrom(sender.getType())) {
            throw new CommandValidationException("@Sender annotated parameter must be a CommandSender or one of its subtypes");
        }
    }

    public void validateArguments(CommandMethod commandMethod) {
        CommandSyntax syntax = commandMethod.getCommandSyntax();

        HashMap<String, Parameter> argAnnotatedParams = commandMethod.getArgAnnotatedParameters();

        long definedArguments = syntax.tokenList().stream()
                .filter(CommandSyntax.CommandToken::isInput)
                .count();

        int providedArguments = argAnnotatedParams.size();

        if (definedArguments != providedArguments) {
            throw new CommandValidationException(
                    "The number of input arguments defined in the syntax " +
                            "does not match the number of @Arg-annotated parameters."
            );
        }

    // TODO: check for key duplicates, check for existence
    // TODO: check if type is supported
    }

}
