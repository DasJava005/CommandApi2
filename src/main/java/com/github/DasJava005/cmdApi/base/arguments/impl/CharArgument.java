package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;

public class CharArgument extends InputArgument<Character> {

    public CharArgument(String key) {
        super(key);
    }

    @Override
    public Character parse(String[] tokens) {
        if(tokens.length != 1) {
            throw new ParseException(tokens[0], Character.class);
        }

        return tokens[0].charAt(0);
    }

}
