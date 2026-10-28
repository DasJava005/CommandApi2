package com.github.DasJava005.cmdApi.base;

import java.util.Map;

public sealed interface MatchResult permits MatchResult.NoMatch, MatchResult.NoPermission, MatchResult.ParseFailure, MatchResult.SenderTypeMismatch, MatchResult.Success, MatchResult.TooManyArguments {

    record Success(Map<String, Object> ctxValues) implements MatchResult {}

    record NoMatch() implements MatchResult {}

    record ParseFailure(ParseException exception) implements MatchResult {}
    record NoPermission(String permission) implements MatchResult {}
    record SenderTypeMismatch(Class<?> expectedSenderType, Class<?> providedSenderType) implements MatchResult {}
    record TooManyArguments(int required, int provided) implements MatchResult {}

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

   public static MatchResult tooManyArguments(int required, int provided) {
        return new TooManyArguments(required, provided);
   }

}
