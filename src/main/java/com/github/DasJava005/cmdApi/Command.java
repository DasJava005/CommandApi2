package com.github.DasJava005.cmdApi;

import org.bukkit.entity.Player;

import java.util.List;

public record Command(CommandInfo commandInfo, CommandArguments arguments, Class<?> sender, CommandExecutor executor) {

    public Command(CommandInfo commandInfo, CommandArguments arguments, Class<?> sender, CommandExecutor executor) {
        this.commandInfo = commandInfo;
        this.arguments = arguments;
        this.sender = sender != null ? sender : Player.class;
        this.executor = executor != null ? executor : _ -> {
        };
    }

    public Command(String label, CommandArguments arguments, Class<?> sender, CommandExecutor executor) {
        this(new CommandInfo(label, List.of(), "", ""), arguments, sender, executor);
    }

}
