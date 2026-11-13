package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public final class StringArgument extends InputArgument<String> {

    public StringArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<String> parse(String[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            sb.append(token).append(" ");
        }
        return ParseResult.success(sb.toString().trim());
    }

}
