package com.github.DasJava005.cmdApi.reflection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandSyntax {

    private final String syntax;

    public CommandSyntax(String syntax) {
        this.syntax = syntax;
    }

    public List<CommandToken> tokens() {
        return Arrays.stream(syntax.split("\\s+"))
                .map(CommandToken::new)
                .toList();
    }

    public List<CommandToken> getArgumentTokens() {
        return tokens().stream().filter(CommandToken::isArgument).toList();
    }

    public List<CommandToken> getLiteralTokens() {
        return tokens().stream().filter(CommandToken::isLiteral).toList();
    }

    public static final class CommandToken {

        public static final String ARGUMENT_IDENTIFIER = "#";

        private final String token;
        private final boolean literal; // otherwise argument

        public CommandToken(String token) {
            if(token.startsWith(ARGUMENT_IDENTIFIER)) {
                this.token = token.replaceFirst(ARGUMENT_IDENTIFIER, "");
                this.literal = false;
            } else {
                this.token = token;
                this.literal = true;
            }
        }

        public boolean isArgument(){
            return !literal;
        }

        public boolean isLiteral(){
            return literal;
        }

        public String getToken() {
            return token;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this)
                return true;
            if(obj instanceof CommandToken other){
                return token.equals(other.token) && literal == other.literal;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(token, literal);
        }

        @Override
        public String toString() {
            return "{token=" + token + ", literal=" + literal + "}";
        }
    }

}