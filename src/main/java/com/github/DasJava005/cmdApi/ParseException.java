package com.github.DasJava005.cmdApi;

public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String userInput, Class<?> expectedType) {
        super("'" + userInput + "' is not of type " + expectedType.getSimpleName());
    }

}
