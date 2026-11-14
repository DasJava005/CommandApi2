package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.TypeArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public class FloatArgument extends TypeArgument<Float> {

    public FloatArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Float> parse(String[] tokens) {
        try{
            return ParseResult.success(Float.parseFloat(tokens[0]));
        }catch (Exception e) {
            return ParseResult.mismatch(tokens[0], Float.class);
        }
    }

}
