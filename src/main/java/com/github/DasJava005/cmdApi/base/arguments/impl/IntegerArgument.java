package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.TypeArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public final class IntegerArgument extends TypeArgument<Integer> {

    public IntegerArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Integer> parse(String[] tokens) {
        try {
            return ParseResult.success(Integer.parseInt(tokens[0]));
        } catch (Exception e) {
            return ParseResult.mismatch(tokens[0], Integer.class);
        }
    }

}
