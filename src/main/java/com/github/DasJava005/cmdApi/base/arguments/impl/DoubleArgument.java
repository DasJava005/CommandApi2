package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.ParseException;
import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class DoubleArgument extends InputArgument<Double> {

    public DoubleArgument(String key) {
        super(key);
    }

    @Override
    public Double parse(String[] tokens) {
        try{
            return Double.parseDouble(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Double.class);
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of("<number>");
    }

}
