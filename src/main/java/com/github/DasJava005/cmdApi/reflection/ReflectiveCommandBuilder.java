package com.github.DasJava005.cmdApi.reflection;

import com.github.DasJava005.cmdApi.Command;
import com.github.DasJava005.cmdApi.CommandArguments;
import com.github.DasJava005.cmdApi.CommandExecutor;
import com.github.DasJava005.cmdApi.CommandInfo;
import com.github.DasJava005.cmdApi.input.Argument;
import com.github.DasJava005.cmdApi.input.LiteralArgument;
import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ReflectiveCommandBuilder {

    private final InputArgumentRegistry inputArgumentRegistry;

    public ReflectiveCommandBuilder(InputArgumentRegistry inputArgumentRegistry) {
        this.inputArgumentRegistry = inputArgumentRegistry;
    }

    public ReflectiveCommandBuilder() {
        this.inputArgumentRegistry = new InputArgumentRegistry();
        this.inputArgumentRegistry.registerDefaults();
    }

    public Set<Command> buildCommands(Object instance){
        if(!instance.getClass().isAnnotationPresent(CommandGroup.class)){
            throw new IllegalArgumentException("Class is not annotated with @CommandGroup");
        }
        Set<Command> commands = new HashSet<>();
        for(Method method : instance.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(Cmd.class)){
                commands.add(buildCommand(new CommandMethod(instance, method)));
            }
        }
        return commands;
    }

    private Command buildCommand(CommandMethod commandMethod) {
        String commandName  = commandMethod.instance().getClass().getAnnotation(CommandGroup.class).value();
        String syntax =  commandMethod.method().getAnnotation(Cmd.class).value();

        CommandSyntax commandSyntax = new CommandSyntax(syntax);

        HashMap<String, Parameter> parameters = getParameters(commandMethod.method());

        //build arguments from syntax
        List<Argument> arguments = new ArrayList<>();

        for (Iterator<CommandSyntax.CommandToken> it = commandSyntax.tokens(); it.hasNext();) {
            CommandSyntax.CommandToken token = it.next();
            if(token.isLiteral()){
                arguments.add(new LiteralArgument(token.getToken()));
                continue;
            }
            if(token.isInput()){
                String key = token.getToken();
                if(!parameters.containsKey(key))
                    throw new IllegalArgumentException(String.format("%s is not annotated with @Cmd", commandMethod.method().getName()));

                Parameter p = parameters.get(key);
                var optInputArgument = inputArgumentRegistry.create(key, p.getType());
                if(optInputArgument.isPresent()){
                    arguments.add(optInputArgument.get());
                }else{
                    throw new IllegalArgumentException("not supported type: " + p.getType());
                }
            }
        }

        CommandInfo commandInfo = new CommandInfo(commandName, List.of(), "", "");
        CommandArguments commandArguments = new CommandArguments(arguments);
        Class<?> commandSender = getSenderParameter(commandMethod.method()).getType();
        CommandExecutor commandExecutor = new ReflectiveExecutor(commandMethod);

        return new Command(commandInfo, commandArguments, commandSender, commandExecutor);
    }

    private Parameter getSenderParameter(Method method) {
        for(Parameter p : method.getParameters()) {
            if(p.isAnnotationPresent(Sender.class)){
                return p;
            }
        }
        return null;
    }

    private HashMap<String, Parameter> getParameters(Method m) {
        HashMap<String, Parameter> parameters = new HashMap<>();
        for(Parameter p : m.getParameters()) {
            if(p.isAnnotationPresent(Arg.class)){
                String parameterName = p.getAnnotation(Arg.class).value();
                parameters.put(parameterName, p);
            }
        }
        return parameters;
    }


}
