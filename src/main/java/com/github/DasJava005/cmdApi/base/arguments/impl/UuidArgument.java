package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseException;

import java.util.UUID;

public class UuidArgument extends InputArgument<UUID> {

    public UuidArgument(String key) {
        super(key);
    }

    @Override
    public UUID parse(String[] tokens) {
        try {
            return UUID.fromString(tokens[0]);
        } catch(Exception e) {
            throw new ParseException(tokens[0], UUID.class);
        }
    }

}
