package com.github.DasJava005.cmdApi.reflection.validation;

import com.github.DasJava005.cmdApi.reflection.CommandMethod;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;

public record CommandMethodValidator() {

    public CommandMethodValidator() {

    }

    public void validate(CommandMethod commandMethod) {
        validateSender(commandMethod);
    }

    private void validateSender(CommandMethod commandMethod) {
        Parameter sender = null;

        for (Parameter parameter : commandMethod.method().getParameters()) {
            if (!parameter.isAnnotationPresent(Sender.class)) {
                continue;
            }

            if (sender != null) {
                throw new CommandValidationException("Only one parameter may be annotated with @Sender");
            }

            sender = parameter;
        }

        if (sender == null) {
            throw new CommandValidationException("Command method must have exactly one @Sender parameter");
        }

        if (!CommandSender.class.isAssignableFrom(sender.getType())) {
            throw new CommandValidationException("@Sender parameter must be a CommandSender or one of its subtypes");
        }
    }


}
