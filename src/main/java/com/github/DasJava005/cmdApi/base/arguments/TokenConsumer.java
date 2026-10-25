package com.github.DasJava005.cmdApi.base.arguments;

public interface TokenConsumer {

    public abstract TokenConsumerType getConsumerType();

    public static sealed interface TokenConsumerType permits TokenConsumerType.AtLeast, TokenConsumerType.Exactly, TokenConsumerType.Remaining {
        public record Exactly(int tokens) implements TokenConsumerType {}
        public record AtLeast(int tokens) implements TokenConsumerType {}
        public record Remaining() implements TokenConsumerType {}

        public static TokenConsumerType.Exactly exactly(int tokens) {
            return new TokenConsumerType.Exactly(tokens);
        }

        public static TokenConsumerType.AtLeast atLeast(int tokens) {
            return new TokenConsumerType.AtLeast(tokens);
        }

        public static TokenConsumerType.Remaining remaining() {
            return new TokenConsumerType.Remaining();
        }

    }

}
