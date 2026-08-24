package com.github.DasJava005.cmdApi.input;

public class GreedyArgument implements InputArgument<String> {

    private final String key;

    public GreedyArgument(String key){
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public final int tokenConsumeCount() {
        return 1;
    }

    @Override
    public String parse(String[] tokens) {
        StringBuilder sb = new StringBuilder();
        for(String token : tokens){
            sb.append(" ").append(token);
        }
        return sb.toString();
    }

}