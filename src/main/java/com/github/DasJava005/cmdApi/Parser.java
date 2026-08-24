package com.github.DasJava005.cmdApi;

@FunctionalInterface
public interface Parser<T> {

    T parse(String[] tokens);

}