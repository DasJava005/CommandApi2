package com.github.DasJava005.cmdApi;

import java.util.List;

@FunctionalInterface
public interface CommandProvider {

    List<Command> getCommands(String label);

}