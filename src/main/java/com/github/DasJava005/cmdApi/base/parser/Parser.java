package com.github.DasJava005.cmdApi.base.parser;

@FunctionalInterface
public interface Parser<T> {

    T parse(String[] tokens);

}