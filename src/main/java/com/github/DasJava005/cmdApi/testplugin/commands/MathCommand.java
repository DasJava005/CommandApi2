package com.github.DasJava005.cmdApi.testplugin.commands;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

@CommandGroup(value = "math", aliases = {"m"}, description = "Provides basic math operations")
public class MathCommand {

    @Cmd("sin #value")
    public void sinCommand(@Sender CommandSender sender, @Arg("value") float value){
        sender.sendMessage("sin( " + value + ") =" + Math.sin(value));
    }

    @Cmd("cos #value")
    public void cosCommand(@Sender CommandSender sender, @Arg("value") float value){
        sender.sendMessage("cos( " + value + ") =" + Math.cos(value));
    }

    @Cmd(value = "scalarProduct #v #w", permission = "math.scp")
    public void cosCommand(@Sender CommandSender sender, @Arg("v") Vector v, @Arg("w") Vector w){
        var scp = v.getX() * w.getX() + v.getY() * w.getY() + v.getZ() * w.getZ();
        sender.sendMessage("scp =" + scp);
    }

}
