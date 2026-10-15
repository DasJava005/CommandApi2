package com.github.DasJava005.cmdApi.reflection.validation;

import com.github.DasJava005.cmdApi.reflection.CommandMethod;

public class CommandValidationException extends RuntimeException {

    public CommandValidationException(CommandMethod command, String message) {
        super("[Class: " + command.instance().getClass().getCanonicalName() + ", Method: " + command.method().getName() + "] " + message);
    }

}
