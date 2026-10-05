package com.github.DasJava005.cmdApi.input.factory.simpleTypes;

import com.github.DasJava005.cmdApi.ParseException;
import com.github.DasJava005.cmdApi.input.factory.AbstractInputArgumentFactory;

public class DoubleInputArgumentFactory extends AbstractInputArgumentFactory<Double> {

    @Override
    protected Double parse(String[] tokens) {
        try{
            return Double.parseDouble(tokens[0]);
        }catch (IllegalArgumentException iae){
            throw new ParseException(tokens[0], Double.class);
        }
    }

}
