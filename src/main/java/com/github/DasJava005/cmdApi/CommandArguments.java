package com.github.DasJava005.cmdApi;

import com.github.DasJava005.cmdApi.input.Argument;
import com.github.DasJava005.cmdApi.input.GreedyArgument;

import java.util.ArrayList;
import java.util.List;

public final class CommandArguments {

    private final List<Argument> definitions;
    private boolean hasGreedyArgument = false;

    /**
     * Maps each consumed token position to its corresponding argument.
     * <p>
     * Example:
     * Argument A consumes 1 token and Argument B consumes 2 tokens:
     * <p>
     * [A, B, B]
     */
    private final List<Argument> argumentsByToken;

    public CommandArguments(List<Argument> arguments) {
        this.definitions = List.copyOf(arguments);

        List<Argument> byToken = new ArrayList<>();

        int pos = 0;
        for (Argument argument : definitions) {
            if(argument instanceof GreedyArgument) {
                if(hasGreedyArgument) throw new IllegalStateException("Definitions can only have one greedy Argument!");
                hasGreedyArgument = true;
                if(pos != definitions.size() - 1) throw new IllegalStateException("Greedy Argument must be the last in the definitions list!");
            }

            for (int i = 0; i < argument.tokenConsumeCount(); i++) {
                byToken.add(argument);
            }

            pos++;
        }

        this.argumentsByToken = List.copyOf(byToken);
    }

    /**
     * Returns the argument at the given definition index.
     */
    public Argument get(int index) {
        return definitions.get(index);
    }

    public List<Argument> getDefinitions() {
        return definitions;
    }

    public boolean hasGreedyArgument() {
        return hasGreedyArgument;
    }

    /**
     * Returns the argument consuming the given token position.
     */
    public Argument getAtToken(int tokenPosition) {
        return argumentsByToken.get(tokenPosition);
    }

    public int size() {
        return definitions.size();
    }

    public int tokenConsumeCount() {
        return argumentsByToken.size();
    }

}