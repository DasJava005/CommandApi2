package com.github.DasJava005.cmdApi;

import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface Suggestions {

    List<String> suggest(CommandSender sender, String[] tokens);

}
