package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;

public class ShortArgument extends InputArgument<Short> {

    public ShortArgument(String key) {
        super(key);
    }

    @Override
    public Short parse(String[] tokens) {
        try{
            return Short.parseShort(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Short.class);
        }
    }

}
