package com.github.DasJava005.cmdApi.base.arguments.impl.bukkit;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerArgument extends InputArgument<Player> {

    public PlayerArgument(String key) {
        super(key);
    }

    @Override
    public Player parse(String[] tokens) {
        Player p = Bukkit.getPlayerExact(tokens[0]);
        if (p == null) {
            throw new ParseException("Player '" + tokens[0] + "' was not found!");
        }
        return p;
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        final String input = tokens[0].toLowerCase();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(input)).toList();
    }

}
