package com.github.DasJava005.cmdApi.base.arguments;

public final class GreedyArgument extends InputArgument<String> {

    public GreedyArgument(String key) {
        super(key);
    }

    @Override
    public String parse(String[] tokens) {
        StringBuilder builder = new StringBuilder();
        for (String s : tokens) {
            builder.append(" ").append(s);
        }
        return builder.toString();
    }

    @Override
    public TokenConsumerType getConsumerType() {
        return TokenConsumerType.remaining();
    }

}

