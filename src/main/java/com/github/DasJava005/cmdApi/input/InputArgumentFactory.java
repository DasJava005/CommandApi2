package com.github.DasJava005.cmdApi.input;

@FunctionalInterface
public interface InputArgumentFactory<T> {

    InputArgument<T> create(String key);

}