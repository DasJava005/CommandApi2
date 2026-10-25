package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.arguments.impl.BooleanArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.DoubleArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.IntegerArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.StringArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.bukkit.EntityTypeArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.bukkit.MaterialArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.bukkit.VectorArgument;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

/**
 * Provides default implementations for certain types.
 */
public enum InputArguments {

    STRING(String.class, StringArgument::new),
    BOOLEAN(Boolean.class, BooleanArgument::new),
    INTEGER(Integer.class, IntegerArgument::new),
    DOUBLE(Double.class, DoubleArgument::new),

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
