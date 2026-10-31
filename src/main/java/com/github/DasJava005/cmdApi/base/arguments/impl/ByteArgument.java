package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;

public class ByteArgument extends InputArgument<Byte> {

    public ByteArgument(String key) {
        super(key);
    }

    @Override
    public Byte parse(String[] tokens) {
        try{
            return Byte.parseByte(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Byte.class);
        }
    }

}
