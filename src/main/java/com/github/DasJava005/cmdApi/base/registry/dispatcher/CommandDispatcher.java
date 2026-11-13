package com.github.DasJava005.cmdApi.base.registry.dispatcher;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.CommandContext;
import com.github.DasJava005.cmdApi.base.parser.ParseException;
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
                .filter(command -> { // pre-filter commands that already match with all literals
                    for(int i = 0; i < tokens.length; i++) {
                        if(command.arguments().getArgumentAtIndex(i) instanceof LiteralArgument(String literal)){
                            if(!tokens[i].equalsIgnoreCase(literal)) return false;
                        }
                    }
                    return true;
                })
                .sorted((c1, c2) -> Integer.compare(c2.arguments().minRequiredTokens(), c1.arguments().minRequiredTokens()))
                .toList();

        var match = CommandMatcher.MatchResult.noMatch();
        for(Command command : commands) {
            match = commandMatcher.match(command, commandSender, tokens);
            if(match instanceof CommandMatcher.MatchResult.Success(Map<String, Object> ctxValues)) {
                command.executor().execute(new CommandContext(commandSender, ctxValues));
                return true;
            }
        }

        switch (match) {
            case CommandMatcher.MatchResult.ParseFailure(ParseException e) -> commandSender.sendMessage("[!] Could not parse command." + e.getMessage() + "\"");
            case CommandMatcher.MatchResult.NoPermission _ ->  commandSender.sendMessage("[!] You do not have permission to use this command.\"");
            case CommandMatcher.MatchResult.TooManyArguments _ ->   commandSender.sendMessage("[!] Too many arguments.\"");
            default -> commandSender.sendMessage("[!] Could not find any commands for '\" + commandName + \"'\"");
        }

        return false;
    }

}