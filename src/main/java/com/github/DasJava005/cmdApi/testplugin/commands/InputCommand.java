package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.entity.Player;

@CommandGroup("input")
public class InputCommand {

    @Cmd("integer #value")
    public void integer(@Sender Player sender, @Arg("value") int input){
        sender.sendMessage("Input: " + input);
    }

}
