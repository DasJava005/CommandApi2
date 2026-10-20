package com.github.DasJava005.cmdApi.testplugin;

import com.github.DasJava005.cmdApi.CommandRegistry;
import com.github.DasJava005.cmdApi.reflection.InputArgumentRegistry;
import com.github.DasJava005.cmdApi.reflection.ReflectiveCommandBuilder;
import com.github.DasJava005.cmdApi.testplugin.commands.HelloBukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin {

    /*
    public Executor getMainThread() {
        return runnable -> Bukkit.getScheduler().runTask(this, runnable);
    }
    */

    @Override
    public void onEnable() {

        CommandRegistry registry = new CommandRegistry(this);

        InputArgumentRegistry inputArgumentRegistry = InputArgumentRegistry.defaultRegistry();

        var commandBuilder = new ReflectiveCommandBuilder(inputArgumentRegistry);

        HelloBukkitCommand landCommand = new HelloBukkitCommand();

        registry.registerCommands(commandBuilder.buildCommands(landCommand));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
