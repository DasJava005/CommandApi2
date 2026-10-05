package com.github.DasJava005.cmdApi.input.factory.simpleTypes;

import com.github.DasJava005.cmdApi.ParseException;
import com.github.DasJava005.cmdApi.input.factory.AbstractInputArgumentFactory;

public class IntegerInputArgumentFactory extends AbstractInputArgumentFactory<Integer> {

    @Override
    protected Integer parse(String[] tokens) {
        try{
            return Integer.parseInt(tokens[0]);
        }catch (Exception e) {
            throw new ParseException(tokens[0], Integer.class);
        }
    }

}
