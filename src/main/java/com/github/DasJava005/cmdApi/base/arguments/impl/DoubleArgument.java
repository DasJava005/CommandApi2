package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class DoubleArgument extends InputArgument<Double> {

    public DoubleArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Double> parse(String[] tokens) {
        try{
            return ParseResult.success(Double.parseDouble(tokens[0]));
        }catch (Exception e) {
            return ParseResult.mismatch(tokens[0], Double.class);
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of("<number>");
    }

}
