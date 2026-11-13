package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public class ByteArgument extends InputArgument<Byte> {

    public ByteArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Byte> parse(String[] tokens) {
        try{
            return ParseResult.success(Byte.parseByte(tokens[0]));
        }catch (Exception e) {
            return ParseResult.mismatch(tokens[0], Byte.class);
        }
    }

}
