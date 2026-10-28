package com.github.DasJava005.cmdApi.base;

import com.github.DasJava005.cmdApi.base.arguments.Argument;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public final class CommandTabCompleter {

    private final CommandProvider commandProvider;

    public CommandTabCompleter(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    public List<String> complete(CommandSender commandSender, String commandName, String[] tokens) {
        final List<Command> commands = commandProvider.getCommands(commandName);

        for (final Command command : commands) {
            if (!command.sender().isInstance(commandSender)) continue;
            if(tokens.length > command.arguments().minRequiredTokens()) continue;

            final CommandArguments arguments = command.arguments();

            Argument lastArgument = arguments.getArgumentAtIndex(tokens.length - 1);
            if (lastArgument == null) continue;

            int lastInputLength = 0;
            for (int i = tokens.length - 1; i >= 0; i--) {
                if (arguments.getArgumentAtIndex(i) != lastArgument) break;
                lastInputLength++;
            }

            return lastArgument.suggestions(commandSender, Arrays.copyOfRange(
                    tokens,
                    tokens.length - lastInputLength,
                    tokens.length));
        }

        return List.of();
    }

}
