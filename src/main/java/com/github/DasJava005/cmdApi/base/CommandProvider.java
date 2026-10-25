package com.github.DasJava005.cmdApi.base;

import java.util.List;

@FunctionalInterface
public interface CommandProvider {

    List<Command> getCommands(String label);

}