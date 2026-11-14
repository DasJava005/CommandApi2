package com.github.DasJava005.cmdApi.base.registry.dispatcher;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.CommandContext;
import com.github.DasJava005.cmdApi.base.arguments.*;
import com.github.DasJava005.cmdApi.base.registry.CommandProvider;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.*;

public final class CommandDispatcher {

    private final CommandProvider commandProvider;
    private final CommandMatcher commandMatcher;

    public CommandDispatcher(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
        this.commandMatcher = new CommandMatcher();
    }

    public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, String @NonNull [] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName).stream()
                .sorted((c1, c2) -> Integer.compare(c2.arguments().numRequiredTokens(), c1.arguments().numRequiredTokens()))
                .toList();

        List<CommandMatcher.MatchResult.NoMatch> failedCommands = new ArrayList<>();

        for(Command command :  commands) {
            CommandMatcher.MatchResult result = commandMatcher.match(command,  commandSender, tokens);
            switch (result) {
                case CommandMatcher.MatchResult.Match(Command cmd, Map<String, Object> ctx) -> {
                    cmd.executor().execute(new CommandContext(commandSender, ctx));
                    return true;
                }
                case CommandMatcher.MatchResult.NoMatch noMatch -> failedCommands.add(noMatch);
            }
        }

        // Find the most likely failed match
        Optional<CommandMatcher.MatchResult.NoMatch> bestMatch = failedCommands.stream()
                .max(Comparator.comparing(CommandMatcher.MatchResult.NoMatch::matchProgress));

        bestMatch.ifPresent(match ->
                commandSender.sendMessage(match.triedCmd().syntax()));

        return false;
    }

}