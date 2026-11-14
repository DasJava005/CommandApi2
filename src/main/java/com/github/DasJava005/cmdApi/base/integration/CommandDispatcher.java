package com.github.DasJava005.cmdApi.base.integration;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.CommandContext;
import com.github.DasJava005.cmdApi.base.matcher.CommandMatchComparator;
import com.github.DasJava005.cmdApi.base.matcher.CommandMatcher;
import com.github.DasJava005.cmdApi.base.matcher.MatchResult;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import com.github.DasJava005.cmdApi.base.registry.CommandProvider;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.*;

public final class CommandDispatcher {

    private final CommandProvider commandProvider;
    private final CommandMatcher commandMatcher;
    private final CommandMatchComparator comparator = new CommandMatchComparator();
    
    public CommandDispatcher(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
        this.commandMatcher = new CommandMatcher();
    }

    public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, String @NonNull [] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName).stream()
                .sorted((c1, c2) -> Integer.compare(c2.arguments().numRequiredTokens(), c1.arguments().numRequiredTokens()))
                .toList();

        List<MatchResult.NoMatch> failedCommands = new ArrayList<>();

        for(Command command :  commands) {
            MatchResult result = commandMatcher.match(command,  commandSender, tokens);
            switch (result) {
                case MatchResult.Match(Map<String, Object> ctx) -> {
                    command.executor().execute(new CommandContext(commandSender, ctx));
                    return true;
                }
                case MatchResult.NoMatch noMatch -> failedCommands.add(noMatch);
            }
        }
        
        /*
            Give user feedback when command fails
         */
        failedCommands.sort(comparator);
        
        if(failedCommands.isEmpty()) {
            commandSender.sendMessage("[!] Could not find any command matching your input!");
            return false;
        }
        
        commandSender.sendMessage(getUserFeedback(failedCommands.getFirst()));
        
        return false;
    }

    private String getUserFeedback(MatchResult result) {
        return switch (result) {
            case MatchResult.Match ignored ->
                    "Command executed successfully.";

            case MatchResult.NoMatch.ParseMismatch mismatch ->
                    "The provided argument has an invalid format.";

            case MatchResult.NoMatch.LiteralMismatch mismatch ->
                    "Expected '%s', but received '%s'."
                            .formatted(mismatch.required(), mismatch.provided());

            case MatchResult.NoMatch.SenderTypeMismatch mismatch ->
                    "This command can only be executed by %s."
                            .formatted(mismatch.required().getSimpleName());

            case MatchResult.NoMatch.Permission permission ->
                    "You do not have the required permission: %s."
                            .formatted(permission.requiredPermission());

            case MatchResult.NoMatch.NotEnoughArguments arguments ->
                    "Not enough arguments provided. Expected %d, but received %d."
                            .formatted(arguments.required(), arguments.provided());

            case MatchResult.NoMatch.TooManyArguments arguments ->
                    "Too many arguments provided. Expected %d, but received %d."
                            .formatted(arguments.required(), arguments.provided());

            case MatchResult.NoMatch ignored ->
                    "The command could not be matched.";
        };
    }

}