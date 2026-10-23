package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@CommandGroup("get")
public class GetCommand {

    @Cmd("age #age")
    public void age(@Sender Player sender, @Arg("age") int age) {
        sender.sendMessage(Component.text("input was: " + age));
    }

    @Cmd("favchar #char")
    public void favChar(@Sender Player sender, @Arg("char") char c) {
        sender.sendMessage(Component.text("input was: " + c));
    }

}
