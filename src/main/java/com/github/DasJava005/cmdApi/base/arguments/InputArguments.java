package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.arguments.impl.*;
import com.github.DasJava005.cmdApi.base.arguments.impl.bukkit.EntityTypeArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.bukkit.MaterialArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.bukkit.VectorArgument;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Provides default implementations for certain types.
 */
public enum InputArguments {

    // java primitives
    BOOLEAN(Boolean.class, BooleanArgument::new),
    CHAR(Character.class, CharArgument::new),
    BYTE(Byte.class, ByteArgument::new),
    SHORT(Short.class, ShortArgument::new),
    INT(Integer.class, IntegerArgument::new),
    LONG(Long.class, LongArgument::new),
    DOUBLE(Double.class, DoubleArgument::new),
    FLOAT(Float.class, FloatArgument::new),

    // java complex types
    STRING(String.class, StringArgument::new),
    UUID(UUID.class, UuidArgument::new),

    // bukkit types
    VECTOR(Vector.class, VectorArgument::new),
    ENTITY_TYPE(EntityType.class, EntityTypeArgument::new),
    MATERIAL(Material.class, MaterialArgument::new);

    private final Class<?> type;
    private final InputArgumentFactory<?> factory;

    private <T> InputArguments(Class<T> type, InputArgumentFactory<T> factory) {
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
