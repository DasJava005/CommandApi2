package com.github.DasJava005.cmdApi.base.arguments;

import org.bukkit.command.CommandSender;

import java.util.List;

public record LiteralArgument(String literal) implements StaticArgument {

    @Override
    public String key() {
        return literal;
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        return List.of(literal);
    }

    public static LiteralArgument of(String literal) {
        return new LiteralArgument(literal);
    }

}