package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.entity.Player;

@CommandGroup("bal")
public class BalCommand {

    @Cmd("give #user #amount")
    public void balGiveCommand(@Sender Player p, @Arg("user") String userName, @Arg("amount") int amount) {
        p.sendMessage("GAVE " + userName + ": " + amount);
    }

    @Cmd("take #user #amount")
    public void balTakeCommand(@Sender Player p, @Arg("user") String userName, @Arg("amount") int amount) {
        p.sendMessage("TOOK from " + userName + ": " + amount);
    }

}
