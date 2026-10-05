package com.github.DasJava005.cmdApi.testplugin;

import com.github.DasJava005.cmdApi.Command;
import com.github.DasJava005.cmdApi.CommandRegistry;
import com.github.DasJava005.cmdApi.reflection.CommandSyntax;
import com.github.DasJava005.cmdApi.reflection.InputArgumentRegistry;
import com.github.DasJava005.cmdApi.reflection.ReflectiveCommandBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Set;

public final class TestPlugin extends JavaPlugin {

    /*
    public Executor getMainThread() {
        return runnable -> Bukkit.getScheduler().runTask(this, runnable);
    }
    */

    @Override
    public void onEnable() {

        CommandRegistry registry = new CommandRegistry(this);

        TestAnnotationCommand command = new TestAnnotationCommand();

        final InputArgumentRegistry inputArgumentRegistry = new InputArgumentRegistry();
        inputArgumentRegistry.registerDefaults();

        final ReflectiveCommandBuilder builder = new ReflectiveCommandBuilder(inputArgumentRegistry);

        Set<Command> commandSet = builder.buildCommands(command);

        registry.registerCommands(commandSet.toArray(new Command[0]));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
