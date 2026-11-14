package com.github.DasJava005.cmdApi.base.arguments;

import org.bukkit.command.CommandSender;

import java.util.List;

public sealed interface StaticArgument extends Argument permits LiteralArgument, TypeArgument {

    /**
     * @return the number of tokens the Argument consumes
     */
    default int numTokens() {
        return 1;
    }

    default List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of();
    }

}
