package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public record CommandMethod(Object instance, Method method) {

    public CommandSyntax getCommandSyntax() {
        String syntax = method.getAnnotation(Cmd.class).value();
        return new CommandSyntax(syntax);
    }

    public Set<Parameter> getSenderAnnotatedParameters() {
        HashSet<Parameter> parameters = new HashSet<>();
        for(Parameter p : method.getParameters()) {
            if(p.isAnnotationPresent(Sender.class))
                parameters.add(p);
        }
        return parameters;
    }

    public HashMap<String, List<Parameter>> getArgAnnotatedParameters() {
        HashMap<String, List<Parameter>> parameters = new HashMap<>();
        for(Parameter p : method.getParameters()) {
            if(p.isAnnotationPresent(Arg.class)){
                String parameterName = p.getAnnotation(Arg.class).value();
                parameters.computeIfAbsent(parameterName, _ -> new ArrayList<>()).add(p);
            }
        }
        return parameters;
    }

}
