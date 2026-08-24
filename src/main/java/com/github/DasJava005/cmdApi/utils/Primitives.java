package com.github.DasJava005.cmdApi.utils;

public class Primitives {

    public static boolean isPrimitiveType(Class<?> clazz) {
        if (clazz == null) return false;

        return switch (clazz) {
            case Class<?> c when c == boolean.class -> true;
            case Class<?> c when c == byte.class -> true;
            case Class<?> c when c == short.class -> true;
            case Class<?> c when c == char.class -> true;
            case Class<?> c when c == int.class -> true;
            case Class<?> c when c == long.class -> true;
            case Class<?> c when c == float.class -> true;
            case Class<?> c when c == double.class -> true;
            default -> false;
        };
    }

}
