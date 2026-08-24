package com.github.DasJava005.cmdApi.input;

import org.bukkit.command.CommandSender;

import java.util.List;

public record LiteralArgument(String literal) implements Argument {

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of(literal);
    }

    public static LiteralArgument create(String literal) {
        return new LiteralArgument(literal);
    }

}