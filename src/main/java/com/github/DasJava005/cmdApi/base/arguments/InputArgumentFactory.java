package com.github.DasJava005.cmdApi.base.arguments;

@FunctionalInterface
public interface InputArgumentFactory<T> {

    InputArgument<T> create(String key);

}