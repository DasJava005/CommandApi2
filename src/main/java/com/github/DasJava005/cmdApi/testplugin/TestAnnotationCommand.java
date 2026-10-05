package com.github.DasJava005.cmdApi.testplugin;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;
import com.github.DasJava005.cmdApi.reflection.annotations.Cmd;
import com.github.DasJava005.cmdApi.reflection.annotations.CommandGroup;
import com.github.DasJava005.cmdApi.reflection.annotations.Sender;
import org.bukkit.entity.Player;

@CommandGroup("verify")
public class TestAnnotationCommand {

    @Cmd("#username #password")
    public void testVerify(@Sender Player executor, @Arg("username") String username, @Arg("password") String password) {

        executor.sendMessage("Verifying username and password");
        executor.sendMessage(username + " " + password);

    }

}
