package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandGroup("chat")
public class ChatCommand {

    @Cmd("#message")
    public void message(@Sender Player p, @Arg("message") @Greedy String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
