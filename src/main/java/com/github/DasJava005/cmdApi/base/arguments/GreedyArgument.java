package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.parser.ParseResult;

public final class GreedyArgument extends InputArgument<String> {

    public GreedyArgument(String key) {
        super(key);
    }

    @Override
    public ParseResult<String> parse(String[] tokens) {
        StringBuilder builder = new StringBuilder();
        for (String s : tokens) {
            builder.append(" ").append(s);
        }
        return ParseResult.success(builder.toString().trim());
    }

    @Override
    public TokenConsumerType getConsumerType() {
        return TokenConsumerType.remaining();
    }

}

