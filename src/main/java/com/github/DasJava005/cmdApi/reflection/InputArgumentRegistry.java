package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.input.InputArgument;
import com.github.DasJava005.cmdApi.input.InputArgumentFactory;
import com.github.DasJava005.cmdApi.input.InputArguments;
import com.github.DasJava005.cmdApi.Parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InputArgumentRegistry {

    private final Map<Class<?>, InputArgumentFactory<?>> registry = new HashMap<>();

    public <T> void register(Class<T> clazz, InputArgumentFactory<T> factory) {
        registry.put(clazz, factory);
    }

    public <T> void register(Class<T> clazz, Parser<T> parser) {
        registry.put(clazz, (InputArgumentFactory<T>) key -> InputArgument.create(key, parser));
    }

    public void registerDefaults() {
        for(var defArg : InputArguments.values()){
            registry.put(defArg.type(), defArg.factory());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<InputArgument<T>> create(String key, Class<T> clazz) {
        if(!registry.containsKey(clazz)) {
            return Optional.empty();
        }
        return Optional.of((InputArgument<T>) registry.get(clazz).create(key));
    }

    public boolean supports(Class<?> clazz) {
        return registry.containsKey(clazz);
    }

}
