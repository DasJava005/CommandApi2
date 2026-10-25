package com.github.DasJava005.cmdApi.base.arguments.impl;

import com.github.DasJava005.cmdApi.base.arguments.InputArgument;

public final class StringArgument extends InputArgument<String> {

    public StringArgument(String key) {
        super(key);
    }

    @Override
    public String parse(String[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            sb.append(token).append(" ");
        }
        return sb.toString();
    }

}
