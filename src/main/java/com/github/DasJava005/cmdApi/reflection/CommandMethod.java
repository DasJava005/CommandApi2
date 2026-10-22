package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.Greedy;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public record CommandMethod(Object instance, Method method) {

    public CommandSyntax getCommandSyntax() {
        String syntax = method.getAnnotation(Cmd.class).value();
        return new CommandSyntax(syntax);
    }

    public List<Parameter> parameters() {
        return Arrays.stream(method.getParameters()).toList();
    }

    public List<Parameter> getSenderAnnotatedParameters() {
        return parameters().stream().filter(p -> p.isAnnotationPresent(Sender.class)).toList();
    }

    public Map<String, List<Parameter>> getArgAnnotatedParameters() {
        return Arrays.stream(method.getParameters())
                .filter(p -> p.isAnnotationPresent(Arg.class))
                .collect(Collectors.groupingBy(p -> p.getAnnotation(Arg.class).value()));
    }

    public List<Parameter> getGreedyAnnotatedParameters() {
        return parameters().stream().filter(p -> p.isAnnotationPresent(Greedy.class)).toList();
    }

}
