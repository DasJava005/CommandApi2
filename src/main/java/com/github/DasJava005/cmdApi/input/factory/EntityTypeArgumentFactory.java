package com.github.DasJava005.cmdApi.input.factory;

import com.github.DasJava005.cmdApi.ParseException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EntityTypeArgumentFactory extends AbstractInputArgumentFactory<EntityType> {

    private final List<String> entities;

    public EntityTypeArgumentFactory() {
        entities = Arrays.stream(EntityType.values())
                .map(Objects::toString)
                .toList();
    }

    @Override
    protected EntityType parse(String[] tokens) {
        try {
            return EntityType.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException(tokens[0], EntityType.class);
        }
    }

    @Override
    protected List<String> suggest(CommandSender sender, String[] tokens) {
        return entities.stream()
                .filter(s -> s.startsWith(tokens[0].toUpperCase()))
                .toList();
    }

}
