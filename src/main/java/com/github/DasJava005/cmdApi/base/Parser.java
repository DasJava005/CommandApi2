package com.github.DasJava005.cmdApi.base;

@FunctionalInterface
public interface Parser<T> {

    T parse(String[] tokens);

}