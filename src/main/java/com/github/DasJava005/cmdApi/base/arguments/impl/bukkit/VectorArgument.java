package com.github.DasJava005.cmdApi.base.arguments.impl.bukkit;

import com.github.DasJava005.cmdApi.base.ParseException;
import com.github.DasJava005.cmdApi.base.arguments.InputArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public final class VectorArgument extends InputArgument<Vector> {

    private final int requiredTokens = 3;

    public VectorArgument(String key) {
        super(key);
    }

    @Override
    public TokenConsumerType getConsumerType() {
        return TokenConsumerType.exactly(requiredTokens);
    }

    @Override
    public Vector parse(String[] tokens) {
        double[] components = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try{
                components[i] = Double.parseDouble(tokens[i]);
            }catch (Exception e){
                throw new ParseException(tokens[i], Double.class);
            }
        }

        return new Vector(components[0], components[1], components[2]);
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] tokens) {
        if(sender instanceof Player p){
            Vector pos = p.getLocation().toVector();
            String x = ""+pos.getBlockX();
            String y = ""+pos.getBlockY();
            String z = ""+pos.getBlockZ();
            return switch (tokens.length){
                case 1 -> List.of(x, x + " " + y + " " + z);
                case 2 -> List.of(y);
                case 3 -> List.of(z);
                default -> List.of();
            };
        }
        return List.of();
    }

}
