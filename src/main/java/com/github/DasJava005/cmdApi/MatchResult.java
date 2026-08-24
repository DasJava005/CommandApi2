package com.github.DasJava005.cmdApi;

import java.util.Map;

public sealed interface MatchResult permits MatchResult.NoMatch, MatchResult.ParseFailure, MatchResult.Success {

    record Success(Command command, Map<String, Object> values) implements MatchResult {}

    record NoMatch() implements MatchResult {}

    record ParseFailure(ParseException exception) implements MatchResult {}

   public static MatchResult success(Command command, Map<String, Object> values) {
        return new MatchResult.Success(command, values);
   }

   public static MatchResult parseException(ParseException exception) {
        return new MatchResult.ParseFailure(exception);
   }

   public static MatchResult noMatch() {
        return new MatchResult.NoMatch();
   }

}
