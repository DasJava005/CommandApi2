package com.github.DasJava005.cmdApi.input;

public interface TokenConsumer {

    default int tokenConsumeCount() {
        return 1;
    }

}
