package com.github.DasJava005.cmdApi;

import com.github.DasJava005.cmdApi.input.Argument;
import com.github.DasJava005.cmdApi.input.LiteralArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandBuilder {

    private final String label;
    private List<String> aliases = List.of();
    private String permission = "";
    private String description = "";

    private final List<Argument> arguments = new ArrayList<>();

    private Class<? extends CommandSender> sender = Player.class;

    private CommandExecutor executor = _ -> {};

    private CommandBuilder(String label) {
        this.label = label;
    }

    public CommandBuilder sender(Class<? extends CommandSender> sender) {
        this.sender = sender;
        return this;
    }

    public CommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder aliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandBuilder aliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public CommandBuilder alias(String alias) {
        this.aliases = new ArrayList<>();
        this.aliases.add(alias);
        return this;
    }

    public CommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder literal(String literal){
        this.arguments.add(new LiteralArgument(literal));
        return this;
    }

    public CommandBuilder argument(Argument argument) {
        this.arguments.add(argument);
        return this;
    }

    public CommandBuilder arguments(Argument... arguments) {
        this.arguments.addAll(Arrays.asList(arguments));
        return this;
    }

    public CommandBuilder executor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public Command create() {
        return new Command(new CommandInfo(label, aliases, permission, description),
                new CommandArguments(arguments),
                sender,
                executor);
    }

    public static CommandBuilder of(String label) {
        return new CommandBuilder(label);
    }

}
