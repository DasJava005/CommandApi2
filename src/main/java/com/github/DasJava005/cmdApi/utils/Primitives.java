package com.github.DasJava005.cmdApi.utils;

public final class Primitives {

    private Primitives(){}

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

    public static Class<?> wrapPrimitive(Class<?> clazz) {
        if (clazz == null) return null;

        return switch (clazz) {
            case Class<?> c when c == boolean.class -> Boolean.class;
            case Class<?> c when c == byte.class -> Byte.class;
            case Class<?> c when c == short.class -> Short.class;
            case Class<?> c when c == char.class -> Character.class;
            case Class<?> c when c == int.class -> Integer.class;
            case Class<?> c when c == long.class -> Long.class;
            case Class<?> c when c == float.class -> Float.class;
            case Class<?> c when c == double.class -> Double.class;
            default -> clazz;
        };
    }

}
