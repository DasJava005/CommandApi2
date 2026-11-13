package com.github.DasJava005.cmdApi.base.arguments.impl.bukkit;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OfflinePlayerArgument extends InputArgument<OfflinePlayer> {

    public OfflinePlayerArgument(String key) {
        super(key);
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        final String input = tokens[0].toLowerCase();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(input)).toList();
    }

    @Override
    public OfflinePlayer parse(String[] tokens) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(tokens[0]);
        if (offlinePlayer == null) {
            throw new ParseException("Player '" + tokens[0] + "' was not found!");
        }
        return offlinePlayer;
    }

}
