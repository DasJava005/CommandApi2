package com.github.DasJava005.cmdApi;

import com.github.DasJava005.cmdApi.input.Argument;
import com.github.DasJava005.cmdApi.input.Arguments;
import com.github.DasJava005.cmdApi.input.InputArgument;
import com.github.DasJava005.cmdApi.input.LiteralArgument;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.*;

public final class CommandDispatcher {

    private final CommandProvider commandProvider;
    public CommandDispatcher(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, String @NonNull [] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName).stream()
                .sorted((c1, c2) -> Integer.compare(c2.arguments().tokenConsumeCount(), c1.arguments().tokenConsumeCount()))
                .toList();

        if(commands.isEmpty()) {
            commandSender.sendMessage("[!] Could not find any commands for '" + commandName + "'");
            return false;
        }

        MatchResult matchResult = MatchResult.noMatch(); //this is also the likeliest command the user tried to execute
        for(Command command : commands) {
            if(!matchSender(command,  commandSender)) {
                continue;
            }

            matchResult = matchArguments(command, tokens);
            if(matchResult instanceof MatchResult.Success || matchResult instanceof MatchResult.ParseFailure) {
                break;
            }
        }

        switch (matchResult){
            case MatchResult.Success success-> {
                CommandContext ctx = new CommandContext(commandSender, success.values());
                success.command().executor().execute(ctx);
                return true;
            }
            case MatchResult.ParseFailure fail -> {
                Exception e = fail.exception();
                if(e != null) {
                    commandSender.sendMessage("[!] Could not parse arguments. " + e.getMessage()); // this mainly contains ParseExceptions and the one above
                }
                return false;
            }
            case MatchResult.NoMatch _ -> {
                commandSender.sendMessage("[!] Could not find any command matching your input.");
                return false;
            }
        }
    }

    private boolean matchSender(Command command, CommandSender sender) {
        return command.sender().isInstance(sender);
    }

    private MatchResult matchArguments(Command command, String[] tokens) {
        int i = 0; // the i-th token
        final Map<String, Object> ctxValues = new HashMap<>();
        for(Argument arg : command.arguments().getDefinitions()) {
            switch (arg) {

                case LiteralArgument literalArgument -> {
                    if (i >= tokens.length) return MatchResult.noMatch();
                    if (!literalArgument.literal().equalsIgnoreCase(tokens[i])) return MatchResult.noMatch();
                    i++;
                }

                case InputArgument<?> inputArgument -> {
                    if (tokens.length < i + inputArgument.tokenConsumeCount()) return MatchResult.noMatch();
                    final String[] parseArguments = Arrays.copyOfRange(tokens, i, i + inputArgument.tokenConsumeCount());

                    try {
                        Object obj = inputArgument.parse(parseArguments);
                        ctxValues.put(inputArgument.key(), obj);
                    } catch (ParseException e) {
                        return MatchResult.parseException(e);
                    }
                    i += inputArgument.tokenConsumeCount();
                }

            }
        }

        if (i != tokens.length) { // user provided too many tokens
            return MatchResult.noMatch();
        }

        return MatchResult.success(command, ctxValues);
    }

    public List<String> tabComplete(CommandSender commandSender, String commandName, String[] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName);

        for (final Command command : commands) {
            if (!command.sender().isInstance(commandSender)) continue;
            if(tokens.length > command.arguments().tokenConsumeCount()) continue;

            final Arguments arguments = command.arguments();

            Argument lastArgument = arguments.getAtToken(tokens.length - 1);
            if (lastArgument == null) continue;

            int lastInputLength = 0;
            for (int i = tokens.length - 1; i >= 0; i--) {
                if (lastArgument == arguments.getAtToken(i)) { //the map contains the same object for both keys
                    lastInputLength++;
                } else
                    break;
            }

            return lastArgument.suggestions(commandSender, Arrays.copyOfRange(
                    tokens,
                    tokens.length - lastInputLength,
                    tokens.length));
        }

        return List.of();
    }

}