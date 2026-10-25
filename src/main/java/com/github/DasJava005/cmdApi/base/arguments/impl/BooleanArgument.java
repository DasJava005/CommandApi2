package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.ParseException;
import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class BooleanArgument extends InputArgument<Boolean> {

    public BooleanArgument(String key) {
        super(key);
    }

    @Override
    public Boolean parse(String[] tokens) {
        if(tokens[0].equalsIgnoreCase("true") || tokens[0].equalsIgnoreCase("1")){
            return true;
        }
        if(tokens[0].equalsIgnoreCase("false") || tokens[0].equalsIgnoreCase("0")){
            return false;
        }
        throw new ParseException(tokens[0], Boolean.class);
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of("true", "false");
    }

}
