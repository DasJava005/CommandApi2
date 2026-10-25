package com.github.DasJava005.cmdApi.base.arguments;

import org.bukkit.command.CommandSender;

import java.util.List;

public sealed interface Argument extends TokenConsumer permits InputArgument, LiteralArgument {

    @Override
    default TokenConsumerType getConsumerType() {
        return TokenConsumerType.exactly(1);
    }

    /**
     * Returns tab-completion suggestions.
     *
     * @param sender the command sender
     * @param tokens the current input from the sender as tokens
     * @return the suggestions
     */
    default List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of();
    }

}
