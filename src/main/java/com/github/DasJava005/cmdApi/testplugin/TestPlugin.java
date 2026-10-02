package com.github.DasJava005.cmdApi.testplugin;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.DasJava005.cmdApi.Command;
import com.github.DasJava005.cmdApi.CommandBuilder;
import com.github.DasJava005.cmdApi.CommandRegistry;
import com.github.DasJava005.cmdApi.input.InputArguments;
import com.github.DasJava005.cmdApi.input.LiteralArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.Executor;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandRegistry registry = new CommandRegistry(this);

        registry.registerCommands(createItemCommand().create(),
                createTablistCommand().create(),
                createSkinCommand(),
                createSummonCommand(),
                createMessageCommand().create());
    }

    public Executor getMainThread() {
        return runnable -> Bukkit.getScheduler().runTask(this, runnable);
    }

    private CommandBuilder createItemCommand() {
        return CommandBuilder.of("item")
                .sender(Player.class)
                .aliases("it", "itam")
                .argument(LiteralArgument.create("give"))
                .argument(InputArguments.MATERIAL.createArgument("mat"))
                .argument(InputArguments.INTEGER.createArgument("amount"))
                .argument(InputArguments.STRING.createArgument("name"))
                .executor(ctx -> {
                    Player p = ctx.getSender(Player.class);
                    Material m = ctx.get("mat", Material.class);
                    int amount = ctx.get("amount", Integer.class);
                    String name = ctx.get("name", String.class);

                    ItemStack item = new ItemStack(m, amount);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(name);
                    item.setItemMeta(meta);
                    p.getInventory().addItem(item);
                });
    }
    private CommandBuilder createTablistCommand() {
        return CommandBuilder.of("tablist")
                .literal("nick")
                .argument(InputArguments.STRING.createArgument("name"))
                .executor(ctx ->{
                    Player p = ctx.getSender(Player.class);
                    String name = ctx.get("name", String.class);
                    p.playerListName(Component.text(name));
                });
    }
    private Command createSkinCommand() {
        return CommandBuilder.of("skin")
                .argument(LiteralArgument.create("change"))
                .argument(InputArguments.UUID.createArgument("id"))
                .executor(ctx -> {
                    Player p =  ctx.getSender(Player.class);
                    UUID id = ctx.get("id", UUID.class);

                    Bukkit.createProfile(id).update().thenAcceptAsync(skinProfile -> {
                        PlayerProfile playerProfile = p.getPlayerProfile();
                        playerProfile.setTextures(skinProfile.getTextures());
                        p.setPlayerProfile(playerProfile);
                    }, getMainThread());
                })
                .create();
    }

    private Command createSummonCommand(){
        return CommandBuilder.of("summon")
                .argument(InputArguments.VECTOR.createArgument("pos"))
                .argument(InputArguments.ENTITY_TYPE.createArgument("type"))
                .executor(ctx -> {
                    Player p = ctx.getSender(Player.class);
                    Vector pos = ctx.get("pos", Vector.class);
                    EntityType type = ctx.get("type", EntityType.class);
                    Location location = new Location(p.getWorld(), pos.getX(), pos.getY(), pos.getZ());
                    p.getWorld().spawnEntity(location, type);
                }).create();
    }

    public CommandBuilder createMessageCommand(){
        return CommandBuilder.of("message")
                .literal("send")
                .argument(InputArguments.createGreedyArgument("msg"))
                .executor(ctx ->{
                    Player p = ctx.getSender(Player.class);
                    String msg = ctx.get("msg", String.class);
                    p.sendMessage(msg);
                });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
