package com.github.DasJava005.cmdApi.base.arguments;

public record GreedyArgument(String key) implements SwallowArgument {

    @Override
    public String key() {
        return key;
    }

    public String swallow(String[] tokens) {
        StringBuilder builder = new StringBuilder();
        for (String s : tokens) {
            builder.append(" ").append(s);
        }
        return builder.toString().trim();
    }

}

