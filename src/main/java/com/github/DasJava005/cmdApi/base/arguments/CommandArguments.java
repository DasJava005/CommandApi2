package com.github.DasJava005.cmdApi.base.arguments;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CommandArguments {

    private final List<Argument> arguments;

    private final int numRequiredTokens;

    private final int numLiterals;
    private final int numInputArgs;

    private final boolean swallowArgument;

    public CommandArguments(List<Argument> arguments) {
        this.arguments = List.copyOf(arguments);

        int numRequiredTokens = 0;
        int numLiterals = 0, numInputArgs = 0;
        boolean swallowArgument = false;

        for(Argument arg : arguments) {
            switch (arg) {
                case StaticArgument staticArgument -> {
                    numRequiredTokens += staticArgument.numTokens();
                    switch (staticArgument) {
                        case LiteralArgument _ -> numLiterals++;
                        case TypeArgument<?> _ -> numInputArgs++;
                    }
                }
                case SwallowArgument swArg -> {
                    if(swArg != arguments.getLast()) {
                        throw new IllegalArgumentException("Arguments can only have one SwallowArgument, which must be last in the list!");
                    }
                    swallowArgument = true;
                }
            }
        }

        this.numRequiredTokens = numRequiredTokens;

        this.numLiterals = numLiterals;
        this.numInputArgs = numInputArgs;
        this.swallowArgument = swallowArgument;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public int numRequiredTokens() {
        return numRequiredTokens;
    }

    public int numLiterals() {
        return numLiterals;
    }

    public int numInputArgs() {
        return numInputArgs;
    }

    public boolean hasSwallowArgument() {
        return swallowArgument;
    }

    public Iterator<Argument> iterator() {
        return arguments.iterator();
    }

    public Argument getLast() {
        return arguments.getLast();
    }

    /**
     * Returns the argument corresponding to the specified token position.
     * <p>
     * Static arguments may consume multiple tokens. Consequently, multiple
     * consecutive token positions can correspond to the same argument.
     * If a {@link SwallowArgument} is present, it matches all token positions
     * beyond the required tokens.
     * <p>
     * Example:
     * <pre>
     * Arguments: [LiteralArgument("teleport"), VectorArgument]
     *
     * Token position | Argument
     * ---------------|-------------------------
     * 0              | LiteralArgument
     * 1              | VectorArgument
     * 2              | VectorArgument
     * 3              | VectorArgument
     * </pre>
     *
     * @param position the zero-based position of the token
     * @return an {@link Optional} containing the corresponding argument,
     *         or {@link Optional#empty()} if no argument matches the position
     * @throws IndexOutOfBoundsException if the position is negative
     */
    public Optional<Argument> getAtTokenPosition(int position) {
        if(position < 0) throw new IndexOutOfBoundsException("position cannot be negative!");
        if(position >= numRequiredTokens) {
            if (swallowArgument){
               return Optional.of(getLast());
            } else return Optional.empty();
        }

        int j = 0;
        for (Argument a : arguments) {
            if (a instanceof StaticArgument arg) {
                if (position < j + arg.numTokens()) {
                    return Optional.of(arg);
                }
                j += arg.numTokens();
            }
        }

        return Optional.empty();
    }

}