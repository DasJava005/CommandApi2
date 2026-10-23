package com.github.DasJava005.cmdApi.reflection.validation;

import com.github.DasJava005.cmdApi.reflection.CommandMethod;

public interface CommandValidator {

    void validate(CommandMethod method) throws CommandValidationException;

}
