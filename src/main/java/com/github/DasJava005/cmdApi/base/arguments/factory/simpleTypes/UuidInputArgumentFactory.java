package com.github.DasJava005.cmdApi.base.arguments.factory.simpleTypes;

import com.github.DasJava005.cmdApi.base.ParseException;
import com.github.DasJava005.cmdApi.base.arguments.factory.AbstractInputArgumentFactory;

import java.util.UUID;

public class UuidInputArgumentFactory extends AbstractInputArgumentFactory<UUID> {

    @Override
    protected UUID parse(String[] tokens) {
        try{
            return java.util.UUID.fromString(tokens[0]);
        }catch (IllegalArgumentException iae){
            throw new ParseException(tokens[0], UUID.class);
        }
    }

}
