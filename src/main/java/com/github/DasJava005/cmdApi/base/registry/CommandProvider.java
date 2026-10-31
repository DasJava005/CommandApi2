package com.github.DasJava005.cmdApi.base.registry;

import com.github.DasJava005.cmdApi.base.Command;

import java.util.List;

@FunctionalInterface
public interface CommandProvider {

    List<Command> getCommands(String label);

}