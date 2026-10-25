package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.arguments.impl.BooleanArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.StringArgument;

/**
 * Provides default implementations for certain types.
 */
public enum InputArguments {

    STRING(String.class, StringArgument::new),
    BOOLEAN(Boolean.class, BooleanArgument::new);

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
