package com.github.DasJava005.cmdApi.base.registry.dispatcher;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.arguments.*;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandMatcher {

    public MatchResult match(Command command, CommandSender sender, String[] tokens) {
        if (!command.sender().isInstance(sender))
            return new MatchResult.NoMatch.SenderTypeMismatch(command.sender(), sender.getClass());

        if (!sender.hasPermission(command.permission()))
            return new MatchResult.NoMatch.Permission(command.permission());

        final CommandArguments arguments = command.arguments();

        if(tokens.length < arguments.numRequiredTokens())
            return new MatchResult.NoMatch.NotEnoughArguments(tokens.length,  arguments.numRequiredTokens());

        if(tokens.length > arguments.numRequiredTokens() && !arguments.hasSwallowArgument())
            return new MatchResult.NoMatch.TooManyArguments(tokens.length, arguments.numRequiredTokens());

        final Map<String, Object> ctxValues = new HashMap<>();
        int i = 0;
        for (Argument arg : arguments.getArguments()) {
            if(arg instanceof StaticArgument argument) {

                switch (argument) {
                    case LiteralArgument(String literal) -> {
                        if(!literal.equalsIgnoreCase(tokens[i])){
                            return new MatchResult.NoMatch.LiteralMismatch(tokens[i], literal);
                        }
                        i++;
                    }

                    case TypeArgument<?> typeArgument -> {
                        int start = i;
                        i += typeArgument.numTokens();

                        String[] parseTokens = Arrays.copyOfRange(tokens, start, i);
                        ParseResult<?> parseResult = typeArgument.parse(parseTokens);

                        switch (parseResult) {
                            case ParseResult.ParseSuccess<?> success -> ctxValues.put(typeArgument.key(), success.resultObject());
                            case ParseResult.ParseMismatch<?> mismatch -> {
                                return new MatchResult.NoMatch.ParseMismatch(mismatch);
                            }
                        }

                    }
                } // end of switch

            } else if(arg instanceof SwallowArgument swallowArg) {
                if(swallowArg instanceof GreedyArgument greedyArgument) {
                    final String[] remainingTokens = Arrays.copyOfRange(tokens, i, tokens.length);
                    ctxValues.put(greedyArgument.key(), greedyArgument.swallow(remainingTokens));
                    break;
                }
            }
        }

        return new MatchResult.Match(ctxValues);
    }

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

}
