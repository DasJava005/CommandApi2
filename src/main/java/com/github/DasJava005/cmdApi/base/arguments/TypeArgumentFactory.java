package com.github.DasJava005.cmdApi.base.arguments;

@FunctionalInterface
public interface TypeArgumentFactory<T> {

    TypeArgument<T> create(String key);

}