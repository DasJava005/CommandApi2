package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;
import com.github.DasJava005.cmdApi.base.parser.Parser;

public class LongArgument extends InputArgument<Long> {

    public LongArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Long> parse(String[] tokens) {
        try {
            return ParseResult.success(Long.parseLong(tokens[0]));
        } catch (Exception e) {
            return ParseResult.mismatch(tokens[0], Long.class);
        }
    }

}
