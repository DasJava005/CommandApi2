package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.CommandContext;
import com.github.DasJava005.cmdApi.CommandExecutor;
import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import com.github.DasJava005.cmdApi.reflection.validation.CommandMethodValidator;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class ReflectiveExecutor implements CommandExecutor {

    private final CommandMethodValidator.ValidatedCommandMethod commandMethod;

    public ReflectiveExecutor(CommandMethodValidator.ValidatedCommandMethod commandMethod) {
        this.commandMethod = commandMethod;
    }

    @Override
    public void execute(CommandContext ctx) {
        List<Object> objectList = new LinkedList<>();
        for(Parameter p : commandMethod.getExecutorMethod().getParameters()) {
            if(p.isAnnotationPresent(Sender.class)) {
                objectList.add(ctx.getSender(p.getType()));
            }
            if(p.isAnnotationPresent(Arg.class)){
                objectList.add(ctx.get(p.getAnnotation(Arg.class).value(), p.getType()));
            }
        }
        try {
            commandMethod.getExecutorMethod().invoke(commandMethod.instance(), objectList.toArray());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
