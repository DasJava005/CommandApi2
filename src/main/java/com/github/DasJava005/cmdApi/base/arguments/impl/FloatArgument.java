package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;

public class FloatArgument extends InputArgument<Float> {

    public FloatArgument(String key) {
        super(key);
    }

    @Override
    public Float parse(String[] tokens) {
        try{
            return Float.parseFloat(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Float.class);
        }
    }

}
