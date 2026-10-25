package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.ParseException;
import com.github.DasJava005.cmdApi.base.arguments.InputArgument;

public final class IntegerArgument extends InputArgument<Integer> {

    public IntegerArgument(String key) {
        super(key);
    }

    @Override
    public Integer parse(String[] tokens) {
        try{
            return Integer.parseInt(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Integer.class);
        }
    }

}
