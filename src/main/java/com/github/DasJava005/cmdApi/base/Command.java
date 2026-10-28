package com.github.DasJava005.cmdApi.base;

import org.bukkit.entity.Player;

import java.util.List;

public record Command(GroupInfo groupInfo, CommandArguments arguments, Class<?> sender, String permission, CommandExecutor executor) {

    public Command(GroupInfo groupInfo, CommandArguments arguments, Class<?> sender, String permission, CommandExecutor executor) {
        this.groupInfo = groupInfo;
        this.arguments = arguments;
        this.sender = sender != null ? sender : Player.class;
        this.executor = executor != null ? executor : _ -> {};
        this.permission = permission == null ?  "" : permission;
    }

    public Command(String label, CommandArguments arguments, Class<?> sender, String permission, CommandExecutor executor) {
        this(new GroupInfo(label,List.of(), ""), arguments, sender, permission, executor);
    }

}
