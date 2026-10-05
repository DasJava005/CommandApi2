package com.github.DasJava005.cmdApi.input.factory.simpleTypes;

import com.github.DasJava005.cmdApi.ParseException;
import com.github.DasJava005.cmdApi.input.factory.AbstractInputArgumentFactory;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BooleanInputArgumentFactory extends AbstractInputArgumentFactory<Boolean> {

    @Override
    protected Boolean parse(String[] tokens) {
        if(tokens[0].equalsIgnoreCase("true") || tokens[0].equalsIgnoreCase("1")){
            return true;
        }
        if(tokens[0].equalsIgnoreCase("false") || tokens[0].equalsIgnoreCase("0")){
            return false;
        }
        throw new ParseException(tokens[0], Boolean.class);
    }

    @Override
    protected List<String> suggest(CommandSender sender, String[] tokens) {
        return List.of("true", "false");
    }

}