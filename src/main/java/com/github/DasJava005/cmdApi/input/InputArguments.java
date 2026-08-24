package com.github.DasJava005.cmdApi.input;

import com.github.DasJava005.cmdApi.ParseException;
import com.github.DasJava005.cmdApi.input.factory.BooleanArgumentFactory;
import com.github.DasJava005.cmdApi.input.factory.EntityTypeArgumentFactory;
import com.github.DasJava005.cmdApi.input.factory.MaterialInputArgumentFactory;
import com.github.DasJava005.cmdApi.input.factory.VectorInputArgumentFactory;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Provides default implementations for certain types.
 */
public enum InputArguments {

    STRING(String.class, key -> InputArgument.create(key, tokens -> {
        StringBuilder builder = new StringBuilder();
        for (String s : tokens) {
            builder.append(" ").append(s);
        }
        return builder.toString();
    })),

    INTEGER(Integer.class, key -> InputArgument.create(key, tokens -> {
        try{
            return Integer.parseInt(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Integer.class);
        }
    })),

    BOOLEAN(Boolean.class, new BooleanArgumentFactory()),

    UUID(UUID.class, key -> InputArgument.create(key, tokens ->{
        try{
            return java.util.UUID.fromString(tokens[0]);
        }catch (IllegalArgumentException iae){
            throw new ParseException(tokens[0], UUID.class);
        }
    })),

    MATERIAL(Material.class, new MaterialInputArgumentFactory()),
    VECTOR(Vector.class, new VectorInputArgumentFactory()),
    ENTITY_TYPE(EntityType.class, new EntityTypeArgumentFactory());

    private final Class<?> type;
    private final InputArgumentFactory<?> factory;
    private <T> InputArguments(Class<T> type, InputArgumentFactory<T> factory){
        this.type = type;
        this.factory = factory;
    }

    public Class<?> type() {
        return type;
    }

    public InputArgumentFactory<?> factory() {
        return factory;
    }

    public InputArgument<?> createArgument(String key) {
        return factory.create(key);
    }

}
