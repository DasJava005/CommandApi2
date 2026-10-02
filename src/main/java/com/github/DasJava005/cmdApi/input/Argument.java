package com.github.DasJava005.cmdApi.input;

import org.bukkit.command.CommandSender;

import java.util.List;

public sealed interface Argument permits GreedyArgument, InputArgument, LiteralArgument {

    /**
     * Returns the number of tokens consumed by this argument.
     *
     * @return the token count
     */
    default int tokenConsumeCount() {
        return 1;
    }

    /**
     * Returns tab-completion suggestions.
     *
     * @param sender the command sender
     * @param tokens the current input tokens
     * @return the suggestions
     */
    default List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of();
    }

}
