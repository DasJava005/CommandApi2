package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public HashMap<String, Parameter> getArgAnnotatedParameters() {
        HashMap<String, Parameter> parameters = new HashMap<>();
        for(Parameter p : method.getParameters()) {
            if(p.isAnnotationPresent(Arg.class)){
                String parameterName = p.getAnnotation(Arg.class).value();
                parameters.put(parameterName, p);
            }
        }
        return parameters;
    }

}
