package com.github.DasJava005.cmdApi.base.matcher;

import com.github.DasJava005.cmdApi.base.parser.ParseResult;

import java.util.Map;

public sealed interface MatchResult permits MatchResult.Match, MatchResult.NoMatch {

    record Match(Map<String, Object> ctx) implements MatchResult {
    }

    sealed interface NoMatch extends MatchResult permits
            NoMatch.LiteralMismatch,
            NoMatch.NotEnoughArguments,
            NoMatch.ParseMismatch,
            NoMatch.Permission,
            NoMatch.SenderTypeMismatch,
            NoMatch.TooManyArguments {

        record ParseMismatch(ParseResult.ParseMismatch<?> parseMismatch) implements NoMatch {
        }

        record LiteralMismatch(String provided, String required) implements NoMatch {
        }

        record SenderTypeMismatch(Class<?> required, Class<?> provided) implements NoMatch {
        }

        record Permission(String requiredPermission) implements NoMatch {
        }

        record NotEnoughArguments(int provided, int required) implements NoMatch {
        }

        record TooManyArguments(int provided, int required) implements NoMatch {
        }
    }

}
