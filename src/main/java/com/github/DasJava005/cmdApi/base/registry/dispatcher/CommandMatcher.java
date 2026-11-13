package com.github.DasJava005.cmdApi.base.registry.dispatcher;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.arguments.*;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandMatcher {

    // tokens = input provided by user
    public MatchResult match(Command command, CommandSender sender, String[] tokens) {
        if (!command.sender().isInstance(sender))
            return new MatchResult.NoMatch(MatchFailReason.SENDER_TYPE_MISMATCH, MatchProgress.of(0));
        if (!sender.hasPermission(command.permission()))
            return new MatchResult.NoMatch(MatchFailReason.NO_PERMISSION, MatchProgress.of(0));

        final Map<String, Object> ctxValues = new HashMap<>();

        CommandArguments arguments = command.arguments();
        int i = 0;
        for (Argument arg : arguments.getArguments()) {
            switch (arg) {
                case LiteralArgument(String literal) -> {
                    String currentToken = tokens[i++];
                    if (!literal.equals(currentToken)) {
                        return new MatchResult.NoMatch(MatchFailReason.LITERAL_MISMATCH, MatchProgress.of(0));
                    }
                }

                case GreedyArgument greedyArgument -> {
                    final String[] parseArguments = Arrays.copyOfRange(tokens, i, tokens.length);
                    ParseResult<?> parseResult = greedyArgument.parse(parseArguments);
                    ctxValues.put(greedyArgument.key(),  parseResult);
                    // GreedyArgument is always last in list, the loop ends here
                }

                case InputArgument<?> inputArg -> {
                    int start = i;

                    boolean success = false;

                    do {
                        String[] parseArguments = Arrays.copyOfRange(tokens, start, i + 1);
                        ParseResult<?> parseResult = inputArg.parse(parseArguments);

                        if (parseResult instanceof ParseResult.ParseSuccess<?>(Object resultObject)) {
                            ctxValues.put(inputArg.key(), resultObject);
                            success = true;
                            break;
                        }

                        if (parseResult instanceof ParseResult.ParseMismatch<?>) {
                            return new MatchResult.NoMatch(MatchFailReason.PARSE_ERROR, MatchProgress.of(0));
                        }

                        i++;
                    } while (i < tokens.length);

                    if (!success) {
                        return new MatchResult.NoMatch(MatchFailReason.PARSE_ERROR, MatchProgress.of(0));
                    }
                }
            }
        }

        return new MatchResult.Match(command, ctxValues);
    }

    public sealed interface MatchResult permits MatchResult.Match, MatchResult.NoMatch {
        public static record Match(Command cmd, Map<String, Object> ctx) implements MatchResult {}
        public static record NoMatch(MatchFailReason reason, MatchProgress matchProgress) implements MatchResult {}
    }

    public record MatchProgress(float progress) implements Comparable<MatchProgress> {
        @Override
        public int compareTo(CommandMatcher.MatchProgress o) {
            return Double.compare(progress, o.progress);
        }

        public static MatchProgress of(float progress) {
            return new MatchProgress(progress);
        }
    }

    public enum MatchFailReason {
        NO_PERMISSION,
        PARSE_ERROR,
        NOT_ENOUGH_ARGUMENTS,
        LITERAL_MISMATCH,
        TOO_MANY_ARGUMENTS,
        SENDER_TYPE_MISMATCH,
    }

    /*

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

        record ParseFailure(String userInput, Class<?> expectedType) implements MatchResult {}
        record NoPermission(String permission) implements MatchResult {}
        record SenderTypeMismatch(Class<?> expectedSenderType, Class<?> providedSenderType) implements MatchResult {}
        record LiteralMismatch(String expected, String provided) implements MatchResult {}
        record TooManyArguments(int required, int provided) implements MatchResult {}
        record NotEnoughArguments(int commandRequiresAtLeast, int provided) implements MatchResult {}

        public static MatchResult success(Map<String, Object> ctx) {
            return new MatchResult.Success(ctx);
        }

        public static MatchResult parseException(String userInput, Class<?> expectedType) {
            return new MatchResult.ParseFailure(userInput, expectedType);
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

/*

        final Map<String, Object> ctxValues = new HashMap<>(); // build context
        int i = 0; // the i-th token
        for(Argument arg : command.arguments().getArguments()) {
            switch (arg) {
                case LiteralArgument(String literal) -> {
                    if (i >= userProvidedArgs.length) return MatchResult.notEnoughArguments(command.arguments().minRequiredTokens(), userProvidedArgs.length);
                    if (!literal.equalsIgnoreCase(userProvidedArgs[i])) return MatchResult.literalMismatch(literal, userProvidedArgs[i]);
                    i++;
                }

                case InputArgument<?> inputArgument -> {
                    switch (inputArgument.getConsumerType()) {
                        case TokenConsumer.TokenConsumerType.Exactly(int tokenConsumeCount) -> {
                            if (userProvidedArgs.length < i + tokenConsumeCount)
                                return MatchResult.notEnoughArguments(command.arguments().minRequiredTokens(), userProvidedArgs.length);
                            final String[] parseArguments = Arrays.copyOfRange(userProvidedArgs, i, i + tokenConsumeCount);

                            try {
                                Object obj = inputArgument.parse(parseArguments);
                                ctxValues.put(inputArgument.key(), obj);
                            } catch (TypeCastException e) {
                                return MatchResult.parseException(e);
                            }
                            i += tokenConsumeCount;
                        }

                        case TokenConsumer.TokenConsumerType.AtLeast(int tokenConsumeCount) -> {
                            if (userProvidedArgs.length < i + tokenConsumeCount)
                                return MatchResult.notEnoughArguments(command.arguments().minRequiredTokens(), userProvidedArgs.length);
                            final String[] parseArguments = Arrays.copyOfRange(userProvidedArgs, i, userProvidedArgs.length);
                            try {
                                Object obj = inputArgument.parse(parseArguments);
                                ctxValues.put(inputArgument.key(), obj);
                            } catch (TypeCastException e) {
                                return MatchResult.parseException(e);
                            }
                            i = userProvidedArgs.length;
                        }

                        case TokenConsumer.TokenConsumerType.Remaining _ -> {
                            final String[] parseArguments = Arrays.copyOfRange(userProvidedArgs, i, userProvidedArgs.length);
                            try {
                                Object obj = inputArgument.parse(parseArguments);
                                ctxValues.put(inputArgument.key(), obj);
                            } catch (TypeCastException e) {
                                return MatchResult.parseException(e);
                            }
                            i = userProvidedArgs.length;
                        }
                    }
                }
            }
        }

        if(i != userProvidedArgs.length && !command.arguments().isTokenConsumeCountExactly()) { // user provided too many arguments
            return MatchResult.tooManyArguments(command.arguments().minRequiredTokens(), i);
        }

        return MatchResult.success(ctxValues);
    }
*/
/*
    public sealed interface MatchResult {
        record Match(Map<String, Object> ctxValues) implements MatchResult {
        }

        record NoMatch(FailReason failReason, String message) implements MatchResult {
        }

        public static MatchResult match(Map<String, Object> ctxValues) {
            return new Match(ctxValues);
        }

        public enum FailReason {
            NO_PERMISSION,

            NOT_ENOUGH_ARGUMENTS,

            INVALID_SENDER,

            LITERAL_MISMATCH,

            PARSE_NOT_POSSIBLE,
            ;

            FailReason() {

            }

            public MatchResult matchResult(String message) {
                return new NoMatch(this, message);
            }
        }
    }
*/
/*
    public abstract sealed class MatchResult implements Comparable<MatchResult> permits
            MatchResult.NoMatch,
            MatchResult.NoPermission,
            MatchResult.NotEnoughArguments,
            MatchResult.ParseFailure,
            MatchResult.SenderTypeMismatch,
            MatchResult.LiteralMismatch,
            MatchResult.Success,
            MatchResult.TooManyArguments {

        protected final int severeLevel;

        public MatchResult(int severeLevel) {
            this.severeLevel = severeLevel;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) return true;
            if (!(o instanceof MatchResult other)) return false;
            return other.severeLevel == this.severeLevel;
        }

        @Override
        public int hashCode() {
            return Objects.hash(severeLevel);
        }

        @Override
        public final int compareTo(CommandMatcher.MatchResult o) {
            return Integer.compare(this.severeLevel, o.severeLevel);
        }

        record Success(Map<String, Object> ctxValues) extends MatchResult {}

        record NoMatch() implements MatchResult {}

        record ParseFailure(String userInput, Class<?> expectedType) implements MatchResult {}

        record NoPermission(String permission) implements MatchResult {}

        record SenderTypeMismatch(Class<?> expectedSenderType, Class<?> providedSenderType) implements MatchResult {
        }

        record LiteralMismatch(String expected, String provided) implements MatchResult {
        }

        record TooManyArguments(int required, int provided) implements MatchResult {
        }

        record NotEnoughArguments(int commandRequiresAtLeast, int provided) implements MatchResult {
        }

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
*/
}
