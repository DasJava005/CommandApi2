package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public abstract non-sealed class TypeArgument<T> implements StaticArgument {

    protected final String key;

    public TypeArgument(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

    public abstract ParseResult<T> parse(String[] tokens);

}
