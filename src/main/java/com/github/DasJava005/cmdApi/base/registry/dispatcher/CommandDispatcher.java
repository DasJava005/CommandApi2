package com.github.DasJava005.cmdApi.base.registry.dispatcher;

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
        final List<CommandMatcher.MatchResult> matches = commandProvider.getCommands(commandName).stream()
                .filter(command -> { // pre-filter commands that already match with all literals
                    for(int i = 0; i < tokens.length; i++) {
                        if(command.arguments().getArgumentAtIndex(i) instanceof LiteralArgument(String literal)){
                            if(!tokens[i].equalsIgnoreCase(literal)) return false;
                        }
                    }
                    return true;
                })
                .sorted((c1, c2) -> Integer.compare(c2.arguments().minRequiredTokens(), c1.arguments().minRequiredTokens()))
                .map(cmd -> commandMatcher.match(cmd, commandSender, tokens))
                .toList();

        Optional<CommandMatcher.MatchResult.Match> optMatch = matches.stream()
                .filter(matchResult -> matchResult instanceof CommandMatcher.MatchResult.Match)
                .map(CommandMatcher.MatchResult.Match.class::cast)
                .findFirst();

        if(optMatch.isPresent()) {
            CommandMatcher.MatchResult.Match match = optMatch.get();
            match.cmd().executor().execute(new CommandContext(commandSender, match.ctx()));
            return true;
        }

        List<CommandMatcher.MatchResult.NoMatch> remaining = matches.stream()
                .filter(matchResult -> matchResult instanceof CommandMatcher.MatchResult.NoMatch)
                .map(CommandMatcher.MatchResult.NoMatch.class::cast)
                .toList();

        // Find the most likely failed match
        Optional<CommandMatcher.MatchResult.NoMatch> bestMatch = matches.stream()
                .filter(CommandMatcher.MatchResult.NoMatch.class::isInstance)
                .map(CommandMatcher.MatchResult.NoMatch.class::cast)
                .max(Comparator.comparing(CommandMatcher.MatchResult.NoMatch::matchProgress));

        bestMatch.ifPresent(match ->
                commandSender.sendMessage("Did you look for:" + match.triedCmd().syntax() + " ?"));

        return false;
    }

}