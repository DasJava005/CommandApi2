package com.github.DasJava005.cmdApi.base.registry.dispatcher;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.CommandContext;
import com.github.DasJava005.cmdApi.base.arguments.*;
import com.github.DasJava005.cmdApi.base.registry.CommandProvider;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Stream;

public final class CommandDispatcher {

    private final CommandProvider commandProvider;
    private final CommandMatcher commandMatcher;

    public CommandDispatcher(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
        this.commandMatcher = new CommandMatcher();
    }

    public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, String @NonNull [] tokens) {
        final Stream<CommandMatcher.MatchResult> matches = commandProvider.getCommands(commandName).stream()
                .filter(command -> { // pre-filter commands that already match with all literals
                    for(int i = 0; i < tokens.length; i++) {
                        if(command.arguments().getArgumentAtIndex(i) instanceof LiteralArgument(String literal)){
                            if(!tokens[i].equalsIgnoreCase(literal)) return false;
                        }
                    }
                    return true;
                })
                .sorted((c1, c2) -> Integer.compare(c2.arguments().minRequiredTokens(), c1.arguments().minRequiredTokens()))
                .map(cmd -> commandMatcher.match(cmd, commandSender, tokens));

        Optional<CommandMatcher.MatchResult.Match> optMatch = matches
                .filter(matchResult -> matchResult instanceof CommandMatcher.MatchResult.Match)
                .map(CommandMatcher.MatchResult.Match.class::cast)
                .findFirst();

        if(optMatch.isPresent()) {
            CommandMatcher.MatchResult.Match match = optMatch.get();
            match.cmd().executor().execute(new CommandContext(commandSender, match.ctx()));
            return true;
        }

        return false;
    }

}