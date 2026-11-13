package com.github.DasJava005.cmdApi.base.registry.dispatcher;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.arguments.Argument;
import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.arguments.LiteralArgument;
import com.github.DasJava005.cmdApi.base.arguments.TokenConsumer;
import com.github.DasJava005.cmdApi.base.parser.ParseException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandMatcher {

    public MatchResult match(Command command, CommandSender sender, String[] userProvidedArgs) {
        if(!command.sender().isInstance(sender)) {
            return MatchResult.senderTypeMismatch(command.sender(), sender.getClass());
        }

        final Map<String, Object> ctxValues = new HashMap<>();

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
                            } catch (ParseException e) {
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
                            } catch (ParseException e) {
                                return MatchResult.parseException(e);
                            }
                            i = userProvidedArgs.length;
                        }

                        case TokenConsumer.TokenConsumerType.Remaining _ -> {
                            final String[] parseArguments = Arrays.copyOfRange(userProvidedArgs, i, userProvidedArgs.length);
                            try {
                                Object obj = inputArgument.parse(parseArguments);
                                ctxValues.put(inputArgument.key(), obj);
                            } catch (ParseException e) {
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

        if(!sender.hasPermission(command.permission())){
            return MatchResult.noPermission(command.permission());
        }

        return MatchResult.success(ctxValues);
    }

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

}
