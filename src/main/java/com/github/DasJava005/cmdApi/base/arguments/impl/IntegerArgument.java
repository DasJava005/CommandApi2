package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import com.github.DasJava005.cmdApi.base.parser.Parser;

public final class IntegerArgument extends InputArgument<Integer> {

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
