package com.company;

public class Token {
    public enum Type {
        VARIABLE, KEYWORD, SEPARATOR, OPERATOR, LITERAL, UNKNOWN
    }

    public final Type type;
    public final String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return String.format("(%s, %s )", type.name(), value);
    }

    public boolean equals(Object token) {
        return token instanceof Token && this.type == ((Token)token).type && this.value.equals(((Token)token).value);
    }

    public int hashCode() {
        return type.hashCode()*value.hashCode();
    }
}