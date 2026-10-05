package com.github.DasJava005.cmdApi.input.factory.simpleTypes;

import com.github.DasJava005.cmdApi.input.factory.AbstractInputArgumentFactory;

public class StringInputArgumentFactory extends AbstractInputArgumentFactory<String> {

    @Override
    protected String parse(String[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            sb.append(token).append(" ");
        }
        return sb.toString();
    }

}
