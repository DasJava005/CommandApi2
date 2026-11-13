package com.github.DasJava005.cmdApi.base.arguments;

import com.github.DasJava005.cmdApi.reflection.annotations.Arg;

import java.util.List;

public class CommandArguments {

    private final List<Argument> arguments;
    private final int numLiterals;
    private final int numInputArgs;
    private final int numGreedyArgs; // basically this is smth. like hasGreedy bcs Args can only have one GreedyArg

    private final int minTokenConsumeCount;
    private final boolean isTokenConsumeCountExact;

    public CommandArguments(List<Argument> arguments) {
        this.arguments = List.copyOf(arguments);

        int numLiterals = 0, numInputArgs = 0, numGreedyArgs = 0;

        for(Argument arg : arguments) {
            switch (arg) {
                case LiteralArgument _ -> numLiterals++;
                case GreedyArgument _ -> numGreedyArgs++;
                case InputArgument<?> _ -> numInputArgs++;
            }
        }

        this.numLiterals = numLiterals;
        this.numInputArgs = numInputArgs;
        this.numGreedyArgs = numGreedyArgs;

        for (int i = 0; i < this.arguments.size(); i++) {
            Argument argument = this.arguments.get(i);

            if (!(argument.getConsumerType() instanceof TokenConsumer.TokenConsumerType.Exactly) && i != this.arguments.size() - 1) {
                throw new IllegalArgumentException("Argument with non-exact token consumer type must be the last object in the list.");
            }
        }

        switch (arguments.getLast().getConsumerType()) {
            case TokenConsumer.TokenConsumerType.Exactly exactly -> {
                this.minTokenConsumeCount = exactly.tokens();
                this.isTokenConsumeCountExact = true;
            }
            case TokenConsumer.TokenConsumerType.AtLeast atLeast -> {
                this.minTokenConsumeCount = arguments.stream()
                        .map(Argument::getConsumerType)
                        .filter(c -> c instanceof TokenConsumer.TokenConsumerType.Exactly)
                        .map(c -> (TokenConsumer.TokenConsumerType.Exactly)c)
                        .mapToInt(TokenConsumer.TokenConsumerType.Exactly::tokens)
                        .sum()
                + atLeast.tokens();
                this.isTokenConsumeCountExact = false;
            }
            case TokenConsumer.TokenConsumerType.Remaining remaining -> {
                this.minTokenConsumeCount = arguments.stream()
                        .map(Argument::getConsumerType)
                        .filter(c -> c instanceof TokenConsumer.TokenConsumerType.Exactly)
                        .map(c -> (TokenConsumer.TokenConsumerType.Exactly)c)
                        .mapToInt(TokenConsumer.TokenConsumerType.Exactly::tokens)
                        .sum();
                this.isTokenConsumeCountExact = false;
            }
        }
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public int minRequiredTokens() {
        return minTokenConsumeCount;
    }

    public boolean isTokenConsumeCountExactly() {
        return isTokenConsumeCountExact;
    }

    public Argument getArgumentAtIndex(int index) {
        int i = 0;
        for(Argument argument : arguments) {
            if(argument.getConsumerType() instanceof TokenConsumer.TokenConsumerType.Exactly(int tokens)) {
                if(index >= i && index < i + tokens){
                    return argument;
                }
                i += tokens;
            } else {
                return arguments.getLast();
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CommandArguments other)) return false;
        if (arguments.size() != other.arguments.size()) return false;

        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) != other.arguments.get(i)) {
                return false;
            }
        }

        return true;
    }

    public int numLiterals() {
        return numLiterals;
    }

    public int numInputArgs() {
        return numInputArgs;
    }

    public int numGreedyArgs() {
        return numGreedyArgs;
    }

    @Override
    public int hashCode() {
        int result = 1;

        for (Argument argument : arguments) {
            result = 31 * result + argument.hashCode();
        }

        return result;
    }

}