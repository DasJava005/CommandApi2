package com.github.DasJava005.cmdApi.base;

import java.util.Map;

public sealed interface MatchResult permits
        MatchResult.NoMatch,
        MatchResult.NoPermission,
        MatchResult.NotEnoughArguments,
        MatchResult.ParseFailure,
        MatchResult.SenderTypeMismatch,
        MatchResult.LiteralMismatch,
        MatchResult.Success,
        MatchResult.TooManyArguments {

    record Success(Map<String, Object> ctxValues) implements MatchResult {}

    record NoMatch() implements MatchResult {}

    record ParseFailure(ParseException exception) implements MatchResult {}
    record NoPermission(String permission) implements MatchResult {}
    record SenderTypeMismatch(Class<?> expectedSenderType, Class<?> providedSenderType) implements MatchResult {}
    record LiteralMismatch(String expected, String provided) implements MatchResult {}
    record TooManyArguments(int required, int provided) implements MatchResult {}
    record NotEnoughArguments(int commandRequiresAtLeast, int provided) implements MatchResult {}

    public static MatchResult success(Map<String, Object> ctx) {
        return new MatchResult.Success(ctx);
   }

    public static MatchResult parseException(ParseException exception) {
        return new MatchResult.ParseFailure(exception);
    }

    public static MatchResult noMatch() {
        return new MatchResult.NoMatch();
   }

    public static MatchResult noPermission(String permission) {
        return new MatchResult.NoPermission(permission);
   }

    public static MatchResult senderTypeMismatch(Class<?> expectedSenderType, Class<?> providedSenderType) {
         return new MatchResult.SenderTypeMismatch(expectedSenderType, providedSenderType);
    }

    public static MatchResult literalMismatch(String expected, String provided) {
        return new MatchResult.LiteralMismatch(expected, provided);
    }

    public static MatchResult tooManyArguments(int required, int provided) {
        return new TooManyArguments(required, provided);
    }

    public static MatchResult notEnoughArguments(int commandRequiresAtLeast, int provided) {
        return new MatchResult.NotEnoughArguments(commandRequiresAtLeast, provided);
    }

}
