package com.github.DasJava005.cmdApi.input;

import com.github.DasJava005.cmdApi.Parser;
import com.github.DasJava005.cmdApi.Suggestions;
import org.bukkit.command.CommandSender;

import java.util.List;

public non-sealed interface InputArgument<T> extends Argument, Parser<T> {

    public abstract String key();

    public static <T> InputArgument<T> create(String key, Parser<T> parser) {
        return InputArgument.create(key, parser, (sender, tokens) -> List.of());
    }

    public static <T> InputArgument<T> create(String key, Parser<T> parser, Suggestions suggestions) {
        return InputArgument.create(key, parser, suggestions, 1);
    }

    public static <T> InputArgument<T> create(String key, Parser<T> parser, Suggestions suggestions, int tokenConsumeCount) {
        return new InputArgument<>() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public T parse(String[] tokens) {
                return parser.parse(tokens);
            }

            @Override
            public List<String> suggestions(CommandSender sender, String[] tokens) {
                return suggestions.suggest(sender, tokens);
            }

            @Override
            public int tokenConsumeCount() {
                return tokenConsumeCount;
            }
        };
    }

}
