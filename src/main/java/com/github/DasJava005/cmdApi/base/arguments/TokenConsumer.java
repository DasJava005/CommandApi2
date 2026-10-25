package com.github.DasJava005.cmdApi.base.arguments;

public interface TokenConsumer {

    public abstract ConsumerType getConsumerType();

    public sealed interface ConsumerType permits ConsumerType.AtLeast, ConsumerType.Exactly {
        public record Exactly(int tokens) implements ConsumerType {}
        public record AtLeast() implements ConsumerType {}

        public static ConsumerType.Exactly exactly(int tokens) {
            return new ConsumerType.Exactly(tokens);
        }

        public static ConsumerType.AtLeast atLeast(int tokens) {
            return new ConsumerType.AtLeast();
        }
    }

}
