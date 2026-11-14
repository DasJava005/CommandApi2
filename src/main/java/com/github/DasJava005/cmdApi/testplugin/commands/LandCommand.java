package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandGroup("land")
public class LandCommand {

    @Cmd("members")
    public void members(@Sender Player sender) {
        sender.sendMessage("Command: /land members");
    }

    @Cmd("members list")
    public void membersList(@Sender Player sender) {
        sender.sendMessage("Command: /land members list");
    }

    @Cmd("members add #target")
    public void membersAdd(@Sender Player sender, @Arg("target")Player target) {
        sender.sendMessage("Command: /land members add target");
    }

    @Cmd("members remove #target")
    public void membersRemove(@Sender Player sender, @Arg("target")OfflinePlayer target) {
        sender.sendMessage("Command: /land members remove target");
    }

}
