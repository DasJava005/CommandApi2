package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;

import java.lang.reflect.Method;

public record CommandMethod(Object instance, Method method) {

    public String getCommandSyntax() {
        return method.getAnnotation(Cmd.class).value();
    }

}
