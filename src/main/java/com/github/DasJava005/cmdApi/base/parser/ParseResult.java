package com.github.DasJava005.cmdApi.base.parser;

public sealed interface ParseResult<T> permits ParseResult.ParseSuccess, ParseResult.ParseMismatch, ParseResult.InsufficientTokens {

    public static record ParseSuccess<T>(T resultObject) implements ParseResult<T> {}

    public static record ParseMismatch<T>(String providedInput, Class<T> expectedType) implements ParseResult<T> {}

    public static record InsufficientTokens<T>(int minRequiredTokens, int provided) implements ParseResult<T> {}

    public static <T> ParseResult<T> success(T result) {
        return new ParseSuccess<T>(result);
    }

    public static <T> ParseResult<T> mismatch(String input, Class<T> expectedType) {
        return new ParseMismatch<>(input, expectedType);
    }

    public static <T> ParseResult<T> insufficientTokens(int minRequiredTokens, int provided) {
        return new InsufficientTokens<>(minRequiredTokens, provided);
    }

}
