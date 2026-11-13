package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public class ShortArgument extends InputArgument<Short> {

    public ShortArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Short> parse(String[] tokens) {
        try {
            return ParseResult.success(Short.parseShort(tokens[0]));
        } catch (Exception e) {
            return  ParseResult.mismatch(tokens[0], Short.class);
        }
    }

}
