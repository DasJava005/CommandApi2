package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

import java.util.UUID;

public class UuidArgument extends InputArgument<UUID> {

    public UuidArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<UUID> parse(String[] tokens) {
        try {
            return ParseResult.success(UUID.fromString(tokens[0]));
        } catch(Exception e) {
            return ParseResult.mismatch(tokens[0], UUID.class);
        }
    }

}
