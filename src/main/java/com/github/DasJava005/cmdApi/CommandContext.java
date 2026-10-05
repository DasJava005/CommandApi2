package com.github.DasJava005.cmdApi;

import org.bukkit.command.CommandSender;

import java.util.Map;

public class CommandContext {

    private final CommandSender sender;
    private final Map<String, Object> arguments;

    public CommandContext(CommandSender sender, Map<String, Object> arguments) {
        this.sender = sender;
        this.arguments = arguments;
    }

    public <T> T getSender(Class<T> senderClazz) {
        if(!senderClazz.isInstance(this.sender))
            throw new IllegalArgumentException("Sender must be of type " + senderClazz.getName());

        return senderClazz.cast(this.sender);
    }

    public <T> T get(String key, Class<T> clazz) {
        if (!arguments.containsKey(key))
            throw new IllegalArgumentException("Missing argument: " + key);

        Object value = arguments.get(key);

        if (!clazz.isInstance(value))
            throw new IllegalArgumentException("Argument '" + key + "' must be of type " + clazz.getName());

        return clazz.cast(value);
    }

}
