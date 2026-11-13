package com.github.DasJava005.cmdApi.base.arguments.impl.bukkit;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class EntityTypeArgument extends InputArgument<EntityType> {

    private final List<String> entities;

    public EntityTypeArgument(String key) {
        super(key);
        entities = Arrays.stream(EntityType.values())
                .map(Objects::toString)
                .toList();
    }

    @Override
    public ParseResult<EntityType> parse(String[] tokens) {
        try {
            return ParseResult.success(EntityType.valueOf(tokens[0]));
        } catch (IllegalArgumentException e) {
            return ParseResult.mismatch(tokens[0], EntityType.class);
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        return entities.stream()
                .filter(s -> s.startsWith(tokens[0].toUpperCase()))
                .toList();
    }

}
