package com.github.DasJava005.cmdApi.input.factory;

import com.github.DasJava005.cmdApi.input.InputArgument;
import com.github.DasJava005.cmdApi.input.InputArgumentFactory;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class AbstractInputArgumentFactory<T> implements InputArgumentFactory<T> {

    protected int  tokenConsumeCount() {
        return 1;
    }

    protected abstract T parse(String[] tokens);

    protected List<String> suggest(CommandSender sender, String[] tokens) {
        return List.of();
    }

    @Override
    public final InputArgument<T> create(String key) {
        return InputArgument.create(key, this::parse, this::suggest, tokenConsumeCount());
    }

}
