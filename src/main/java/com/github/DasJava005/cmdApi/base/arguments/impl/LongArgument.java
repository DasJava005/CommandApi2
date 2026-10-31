package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;

public class LongArgument extends InputArgument<Long> {

    public LongArgument(String key) {
        super(key);
    }

    @Override
    public Long parse(String[] tokens) {
        try{
            return Long.parseLong(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Long.class);
        }
    }

}
