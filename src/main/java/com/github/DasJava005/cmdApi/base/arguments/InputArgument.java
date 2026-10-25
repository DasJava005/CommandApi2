package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.base.Parser;
import com.github.DasJava005.cmdApi.base.Suggestions;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract non-sealed class InputArgument<T> implements Argument, Parser<T> {

    private final String key;

    public InputArgument(String key) {
        this.key = key;
    }

    public final String key() {
        return key;
    }

}
