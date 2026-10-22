package com.github.DasJava005.cmdApi;

import org.bukkit.entity.Player;

import java.util.List;

public record Command(GroupInfo groupInfo, CommandArguments arguments, Class<?> sender, CommandExecutor executor) {

    public Command(GroupInfo groupInfo, CommandArguments arguments, Class<?> sender, CommandExecutor executor) {
        this.groupInfo = groupInfo;
        this.arguments = arguments;
        this.sender = sender != null ? sender : Player.class;
        this.executor = executor != null ? executor : _ -> {
        };
    }

    public Command(String label, CommandArguments arguments, Class<?> sender, CommandExecutor executor) {
        this(new GroupInfo(label,List.of(), ""), arguments, sender, executor);
    }

}
