package com.github.DasJava005.cmdApi.base;

@FunctionalInterface
public interface CommandExecutor {

    public abstract void execute(CommandContext ctx);

}
