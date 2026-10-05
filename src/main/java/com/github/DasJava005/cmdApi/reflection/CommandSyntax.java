package com.github.DasJava005.cmdApi.reflection;

import java.util.Arrays;
import java.util.Iterator;

public class CommandSyntax {

    private final String syntax;

    public CommandSyntax(String syntax) {
        this.syntax = syntax;
    }

    public Iterator<CommandToken> tokens() {
        return Arrays.stream(syntax.split("\\s+"))
                .map(CommandToken::new)
                .iterator();
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

        public String getToken(){
            if(isLiteral()){
                return token;
            }else{
                return token.replaceFirst("#", "");
            }
        }
    }

}