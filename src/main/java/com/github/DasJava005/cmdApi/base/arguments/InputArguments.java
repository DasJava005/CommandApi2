package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.arguments.factory.bukkitTypes.EntityTypeArgumentFactory;
import com.github.DasJava005.cmdApi.base.arguments.factory.bukkitTypes.MaterialInputArgumentFactory;
import com.github.DasJava005.cmdApi.base.arguments.factory.bukkitTypes.VectorInputArgumentFactory;
import com.github.DasJava005.cmdApi.base.arguments.factory.simpleTypes.*;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Provides default implementations for certain types.
 */
public enum InputArguments {

    STRING(String.class, new StringInputArgumentFactory()),
    INTEGER(Integer.class, new IntegerInputArgumentFactory()),
    DOUBLE(Double.class, new DoubleInputArgumentFactory()),
    BOOLEAN(Boolean.class, new BooleanInputArgumentFactory()),
    UUID(UUID.class, new UuidInputArgumentFactory()),

    MATERIAL(Material.class, new MaterialInputArgumentFactory()),
    VECTOR(Vector.class, new VectorInputArgumentFactory()),
    ENTITY_TYPE(EntityType.class, new EntityTypeArgumentFactory());

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
