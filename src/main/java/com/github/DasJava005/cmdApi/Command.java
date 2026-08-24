package com.github.DasJava005.cmdApi;

import com.github.DasJava005.cmdApi.input.Argument;
import com.github.DasJava005.cmdApi.input.Arguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Command {

    private final CommandInfo commandInfo;
    private final Arguments arguments;
    private final Class<? extends CommandSender> sender;
    private final CommandExecutor executor;

    public Command(CommandInfo info, Arguments arguments, Class<? extends CommandSender> senderType, CommandExecutor executor) {
          this.commandInfo = info;
          this.arguments = arguments;
          this.sender = senderType != null ? senderType : Player.class;
          this.executor = executor != null ? executor : _ -> {};
    }

    public Command(String label, Arguments arguments, Class<? extends CommandSender> sender, CommandExecutor executor){
        this(new CommandInfo(label, List.of(), "", ""), arguments, sender, executor);

    }

    public CommandInfo getCommandInfo(){
        return commandInfo;
    }

    public final String label() {
        return commandInfo.label();
    }

    public final Arguments arguments() {
        return arguments;
    }

    public Class<? extends CommandSender> sender() {
        return sender;
    }

    public final CommandExecutor executor() {
        return executor;
    }

}
