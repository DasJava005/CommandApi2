package com.github.DasJava005.cmdApi.input.factory.bukkitTypes;

import com.github.DasJava005.cmdApi.ParseException;
import com.github.DasJava005.cmdApi.input.factory.AbstractInputArgumentFactory;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class MaterialInputArgumentFactory extends AbstractInputArgumentFactory<Material> {

    private final List<String> materials;

    public MaterialInputArgumentFactory() {
        this.materials = Arrays.stream(Material.values())
                .map(Material::toString)
                .sorted()
                .toList();
    }

    @Override
    protected Material parse(String[] tokens) {
        Material material = Material.matchMaterial(tokens[0]);
        if (material == null) {
            throw new ParseException("Invalid material value: " + tokens[0]);
        }
        return material;
    }

    @Override
    protected List<String> suggest(CommandSender sender, String[] tokens) {
        String input = tokens[0];
        return materials.stream().filter(mat -> mat.regionMatches(true, 0, input, 0, input.length())).toList();
    }

}