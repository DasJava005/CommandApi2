package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandGroup("chat")
public class ChatCommand {

    @Cmd("#message")
    public void message(@Sender Player p, @Arg("message") String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
