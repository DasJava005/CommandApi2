package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.base.arguments.TypeArgument;
import com.github.DasJava005.cmdApi.base.arguments.TypeArgumentFactory;
import com.github.DasJava005.cmdApi.base.arguments.TypeArguments;
import com.github.DasJava005.cmdApi.utils.Primitives;

import java.util.HashMap;
import java.util.Map;

public final class InputArgumentRegistry {

    public static InputArgumentRegistry defaultRegistry() {
        InputArgumentRegistry registry = new InputArgumentRegistry();
        registry.registerDefaults();
        return registry;
    }

    private final Map<Class<?>, TypeArgumentFactory<?>> registry = new HashMap<>();

    public <T> void register(Class<T> clazz, TypeArgumentFactory<T> factory) {
        registry.put(Primitives.wrapPrimitive(clazz), factory);
    }

    public void registerDefaults() {
        for(var defArg : TypeArguments.values()) {
            registry.put(Primitives.wrapPrimitive(defArg.type()), defArg.factory());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> TypeArgument<T> create(String key, Class<T> clazz) {
        if(!supports(Primitives.wrapPrimitive(clazz))) {
            return null;
        }
        return (TypeArgument<T>) registry.get(Primitives.wrapPrimitive(clazz)).create(key);
    }

    public boolean supports(Class<?> clazz) {
        return registry.containsKey(Primitives.wrapPrimitive(clazz));
    }

}
