package com.github.DasJava005.cmdApi.base;

import com.github.DasJava005.cmdApi.base.arguments.*;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.*;

public final class CommandDispatcher {

    //TODO clean up this class

    private final CommandProvider commandProvider;
    public CommandDispatcher(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, String @NonNull [] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName).stream()
                // pre-filter commands that already match with all literals to provide better suggestions
                .filter(command -> {
                    for(int i = 0; i < tokens.length; i++) {
                        if(command.arguments().getArgumentAtIndex(i) instanceof LiteralArgument(String literal)){
                            if(!tokens[i].equalsIgnoreCase(literal)) return false;
                        }
                    }
                    return true;
                })
                .sorted((c1, c2) -> Integer.compare(c2.arguments().minRequiredTokens(), c1.arguments().minRequiredTokens()))
                .toList();

        if(commands.isEmpty()) {
            commandSender.sendMessage("[!] Could not find any commands for '" + commandName + "'");
            return false;
        }

        for(Command command : commands) {
            var match = match(command, commandSender, tokens);
            if(match instanceof MatchResult.Success(Map<String, Object> ctxValues)) {
                command.executor().execute(new CommandContext(commandSender, ctxValues));
                return true;
            }
        }

        // TODO: improve feedback

        commandSender.sendMessage("[!] Could not find any commands for '" + commandName + "'");

        return false;
    }

    private MatchResult match(Command command, CommandSender sender, String[] userProvidedArgs) {
        if(!command.sender().isInstance(sender)) {
            return MatchResult.senderTypeMismatch(command.sender(), sender.getClass());
        }

        final Map<String, Object> ctxValues = new HashMap<>();

        int i = 0; // the i-th token
        for(Argument arg : command.arguments().getArguments()) {
            switch (arg) {
                case LiteralArgument literalArgument -> {
                    if (i >= userProvidedArgs.length) return MatchResult.noMatch();
                    if (!literalArgument.literal().equalsIgnoreCase(userProvidedArgs[i])) return MatchResult.noMatch();
                    i++;
                }

                case InputArgument<?> inputArgument -> {
                    switch (inputArgument.getConsumerType()) {
                        case TokenConsumer.TokenConsumerType.Exactly(int tokenConsumeCount) -> {
                            if (userProvidedArgs.length < i + tokenConsumeCount) return MatchResult.noMatch();
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
                            if (userProvidedArgs.length < i + tokenConsumeCount) return MatchResult.noMatch(); // did not provide enough args
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

}