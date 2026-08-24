package com.github.DasJava005.cmdApi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.*;

public final class CommandRegistry {

    private final JavaPlugin plugin;
    private final Map<String, List<Command>> commands = new HashMap<>();

    private final CommandDispatcher dispatcher;

    public CommandRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dispatcher = new CommandDispatcher(this::getCommands);
    }

    public void registerCommand(Command command) {
        if(!commands.containsKey(command.label())){
            registerAsBukkitCommand(command.getCommandInfo());
        }

        HashSet<String> keys = new HashSet<>(command.getCommandInfo().aliases());
        keys.add(command.getCommandInfo().label());

        for(String key : keys){
            commands.computeIfAbsent(key, _ -> new ArrayList<>()).add(command);
        }
    }

    public void registerCommand(CommandBuilder builder) {
        registerCommand(builder.create());
    }

    public void registerCommands(Command... commands) {
        for(Command command : commands) {
            registerCommand(command);
        }
    }

    public List<Command> getCommands(String name) {
        return commands.getOrDefault(name, List.of());
    }

    private void registerAsBukkitCommand(CommandInfo info) {
        try {
            final CommandMap commandMap =  getBukkitCommandMap();
            final org.bukkit.command.Command bukkitCommand = createBukkitCommand(info);
            commandMap.register(plugin.getName(), bukkitCommand);
        }catch (ReflectiveOperationException e) {
            System.err.println("Could not register command in Bukkit. REASON: " + e.getMessage());
        }
    }

    private CommandMap getBukkitCommandMap() throws ReflectiveOperationException {
        Class<?> craftServerClass = Bukkit.getServer().getClass();
        Field bukkitCommandMap = craftServerClass.getDeclaredField("commandMap");

        bukkitCommandMap.setAccessible(true);

        return (CommandMap) bukkitCommandMap.get(plugin.getServer());
    }

    private org.bukkit.command.Command createBukkitCommand(CommandInfo info) {
        org.bukkit.command.Command command = new org.bukkit.command.Command(info.label()) {
            @Override
            public boolean execute(@NonNull CommandSender commandSender, @NonNull String commandName, String @NonNull [] tokens) {
                return dispatcher.execute(commandSender, commandName, tokens);
            }

            @NonNull
            @Override
            public List<String> tabComplete(@NonNull CommandSender sender, @NonNull String alias, String @NonNull [] args) {
                return dispatcher.tabComplete(sender, alias, args);
            }
        };

        command.setAliases(info.aliases());
        command.setPermission(info.permission());
        command.setDescription(info.description());

        return command;
    }

}
