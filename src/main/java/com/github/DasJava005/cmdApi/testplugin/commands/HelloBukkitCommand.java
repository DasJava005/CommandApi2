package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.entity.Player;

@CommandGroup("hello")
public class HelloBukkitCommand {

    @Cmd("bukkit")
    public void nickCommand(@Sender Player p){
        p.sendMessage("Hello Bukkit!");
    }

}
