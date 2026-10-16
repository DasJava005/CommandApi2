package com.github.DasJava005.cmdApi.testplugin;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.entity.Player;

@CommandGroup("tablist")
public class TestTablistCommand {

    @Cmd("nick #name")
    public void nickCommand(@Sender Player p, @Arg("name") String name){
        p.setPlayerListName(name);
    }

}
