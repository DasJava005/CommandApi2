package com.github.DasJava005.cmdApi.base.integration;

import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.arguments.*;
import com.github.DasJava005.cmdApi.base.registry.CommandProvider;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public final class CommandTabCompleter {

    private final CommandProvider commandProvider;

    public CommandTabCompleter(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    public List<String> complete(CommandSender commandSender, String commandName, String[] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName);
        final List<String> completions = new ArrayList<>();

        if(tokens.length == 0) return completions;

        for (final Command command : commands) {
            if (!command.sender().isInstance(commandSender)) continue;
            final CommandArguments arguments = command.arguments();

            Optional<Argument> lA = arguments.getAtTokenPosition(tokens.length - 1);
            if (lA.isEmpty()) continue;
            if(!(lA.get() instanceof StaticArgument lastArgument)) continue;

            boolean allPreviousLiteralsMatch = true;
            for(int i = 0; i < tokens.length - 1; i++) {
                Optional<Argument> optionalArgument = arguments.getAtTokenPosition(i);
                if(optionalArgument.isEmpty()) continue;
                Argument argument = optionalArgument.get();
                if(argument instanceof LiteralArgument(String literal)) {
                    if(!tokens[i].equalsIgnoreCase(literal)) {
                        allPreviousLiteralsMatch = false;
                    }
                }
            }
            if(!allPreviousLiteralsMatch) continue;

            int lastInputLength = 0;
            for (int i = tokens.length - 1; i >= 0; i--) {
                if (arguments.getAtTokenPosition(i).get() != lastArgument) break;
                lastInputLength++;
            }

            completions.addAll(lastArgument.suggestions(commandSender, Arrays.copyOfRange(tokens, tokens.length - lastInputLength, tokens.length)).stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }
        return completions;
    }

}
