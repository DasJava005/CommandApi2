package com.github.DasJava005.cmdApi;

@FunctionalInterface
public interface CommandExecutor {

    public abstract void execute(CommandContext ctx);

}
