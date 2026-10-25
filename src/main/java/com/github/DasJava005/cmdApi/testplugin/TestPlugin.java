package com.github.DasJava005.cmdApi.testplugin;

import com.github.DasJava005.cmdApi.base.CommandBuilder;
import com.github.DasJava005.cmdApi.base.CommandRegistry;
import com.github.DasJava005.cmdApi.base.arguments.GreedyArgument;
import com.github.DasJava005.cmdApi.base.arguments.InputArguments;
import com.github.DasJava005.cmdApi.reflection.InputArgumentRegistry;
import com.github.DasJava005.cmdApi.testplugin.commands.ChatCommand;
import com.github.DasJava005.cmdApi.testplugin.commands.GetCommand;
import com.github.DasJava005.cmdApi.testplugin.commands.HelloBukkitCommand;
import com.github.DasJava005.cmdApi.testplugin.commands.InputCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class TestPlugin extends JavaPlugin {

    /*
    public Executor getMainThread() {
        return runnable -> Bukkit.getScheduler().runTask(this, runnable);
    }
    */

    @Override
    public void onEnable() {

        CommandRegistry registry = new CommandRegistry(this);

        registry.registerCommand(CommandBuilder.of("test_a")
                .argument(InputArguments.BOOLEAN.createArgument("bool"))
                .argument(InputArguments.STRING.createArgument("string"))
                .executor(ctx -> {
                    Player p = ctx.getSender(Player.class);
                    boolean bool = ctx.get("bool", Boolean.class);
                    String str = ctx.get("string", String.class);
                    p.sendMessage(bool +  " " + str);
                })
        );

        registry.registerCommand(CommandBuilder.of("test_b")
                .argument(InputArguments.VECTOR.createArgument("vec"))
                .argument(new GreedyArgument("greedy"))
                .executor(ctx -> {
                    Vector v = ctx.get("vec", Vector.class);
                    String str =  ctx.get("greedy", String.class);
                    IO.println("test_b:");
                    IO.println(v.toString()+" "  + str);
                })
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
