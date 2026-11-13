package com.github.DasJava005.cmdApi.base;

import com.github.DasJava005.cmdApi.base.arguments.*;
import org.bukkit.entity.Player;

import java.awt.*;
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

    public String syntax() {
        StringBuilder sb = new StringBuilder(groupInfo.label());

        if(!arguments.getArguments().isEmpty()) {
            sb.append(" ");
        }

        for(Argument arg : arguments.getArguments()) {
            switch (arg) {
                case LiteralArgument(String literal) -> sb.append(literal).append(" ");
                case GreedyArgument _ -> sb.append("<text>");
                case InputArgument<?> inputArg -> sb.append(inputArg.getClass().getGenericSuperclass().getClass().getTypeName()).append(" ");
            }
        }

        return sb.toString().trim();
    }

}
