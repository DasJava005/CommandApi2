package com.github.DasJava005.cmdApi.input;

import com.github.DasJava005.cmdApi.Parser;

public record GreedyArgument(String key) implements Argument, Parser<String> {

    @Override
    public String parse(String[] tokens) {
        StringBuilder builder = new StringBuilder();
        for (String s : tokens) {
            builder.append(" ").append(s);
        }
        return builder.toString();
    }

    @Override
    public final int tokenConsumeCount() {
        return -1;
    }

}

