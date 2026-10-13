package com.github.DasJava005.cmdApi.reflection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandSyntax {

    private final String syntax;

    public CommandSyntax(String syntax) {
        this.syntax = syntax;
    }

    public Iterator<CommandToken> tokens() {
        return tokenList().iterator();
    }

    public List<CommandToken> tokenList() {
        return Arrays.stream(syntax.split("\\s+"))
                .map(CommandToken::new)
                .toList();
    }

    public static final class CommandToken {

        private final String token;

        public CommandToken(String token) {
            this.token = token;
        }

        public boolean isInput(){
            return token.startsWith("#");
        }

        public boolean isLiteral(){
            return !isInput();
        }

        public String getToken() {
            if(isLiteral()){
                return token;
            }else{
                return token.replaceFirst("#", "");
            }
        }

    }

}