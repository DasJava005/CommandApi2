package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public class CharArgument extends InputArgument<Character> {

    public CharArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<Character> parse(String[] tokens) {
        if(tokens.length != 1) {
            return ParseResult.mismatch(tokens[0], Character.class);
        }
        return ParseResult.success(tokens[0].charAt(0));
    }

}
