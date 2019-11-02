package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Tokenizer {
    // This Tokenizer is built to recognize Python
    // if it were written to be separated by spaces

    // Map for known symbols and what type they are
    private static final Map<String, Token.Type> symbols = new HashMap<>();
    static {
        // Known keyword symbols
        symbols.put("if",Token.Type.KEYWORD);
        symbols.put("elif",Token.Type.KEYWORD);
        symbols.put("else",Token.Type.KEYWORD);
        symbols.put("while",Token.Type.KEYWORD);
        symbols.put("for",Token.Type.KEYWORD);

        // Known operator symbols
        symbols.put("+",Token.Type.OPERATOR);
        symbols.put("-",Token.Type.OPERATOR);
        symbols.put("*",Token.Type.OPERATOR);
        symbols.put("/",Token.Type.OPERATOR);
        symbols.put("%",Token.Type.OPERATOR);
        symbols.put("+=",Token.Type.OPERATOR);
        symbols.put("-=",Token.Type.OPERATOR);
        symbols.put("*=",Token.Type.OPERATOR);
        symbols.put("/=",Token.Type.OPERATOR);
        symbols.put("%=",Token.Type.OPERATOR);
        symbols.put("=",Token.Type.OPERATOR);
        symbols.put("==",Token.Type.OPERATOR);
        symbols.put(">",Token.Type.OPERATOR);
        symbols.put(">=",Token.Type.OPERATOR);
        symbols.put("<",Token.Type.OPERATOR);
        symbols.put("<=",Token.Type.OPERATOR);
        symbols.put("!=",Token.Type.OPERATOR);
        symbols.put("!",Token.Type.OPERATOR);

        // Known separator symbols
        symbols.put("\n",Token.Type.SEPARATOR);
        symbols.put("\t",Token.Type.SEPARATOR);
        symbols.put("\"",Token.Type.SEPARATOR);
        symbols.put("\'",Token.Type.SEPARATOR);
        symbols.put("[",Token.Type.SEPARATOR);
        symbols.put("]",Token.Type.SEPARATOR);
        symbols.put("(",Token.Type.SEPARATOR);
        symbols.put(")",Token.Type.SEPARATOR);
        symbols.put("{",Token.Type.SEPARATOR);
        symbols.put("}",Token.Type.SEPARATOR);
        symbols.put(",",Token.Type.SEPARATOR);
        symbols.put(":",Token.Type.SEPARATOR);

        // Known literal symbols
        symbols.put("True",Token.Type.LITERAL);
        symbols.put("False",Token.Type.LITERAL);
        symbols.put("None",Token.Type.LITERAL);
    }

    private static final Pattern
    // Pattern matching for strings to allow single or double quotes around any sequence of chars
        strLiteral = Pattern.compile("([\"']).*\\1"),
    // Pattern matching for numbers, including negatives and decimals
        numLiteral = Pattern.compile("-?\\d+\\.?\\d*"),
    // Pattern matching for variable names, any letters, underscores, or digits
        variable = Pattern.compile("[a-zA-Z_][\\w]*");

    private byte[] bytes;
    private List<String> wordsList;

    // Map for keeping track of how many tokens of each type are found
    private Map<Token, Integer> count = new HashMap<>();
    private List<Token> tokenList = new ArrayList<>();

    public Tokenizer(File file) throws IOException {
        bytes = Files.readAllBytes(file.toPath());
        tokenize();
    }

    public void tokenize() {
        StringBuilder wordBuild = new StringBuilder();
        for (byte b : bytes) {
            char c = (char)b;
            if (c == (' ') || c == ('\n') || c == ('\t') || c == ('\r')) {
                if (wordBuild.length() > 0) {
                    String word = wordBuild.toString();
                    wordBuild.setLength(0);
                    handleWord(word);
                }
            } else {
                wordBuild.append(c);
            }
        }
        if (wordBuild.length() > 0) {
            handleWord(wordBuild.toString());
        }
    }

    public void handleWord(String word) {
        // Can tell if a word is a keyword, separator, or an operator
        Token t;
        if (symbols.containsKey(word)) {
            t = new Token(symbols.get(word), word);
        } else {
        // If word is not a recognized symbol, check other possibilities
            if (strLiteral.matcher(word).matches()) {
                t = new Token(Token.Type.LITERAL, word);
            } else if (numLiteral.matcher(word).matches()) {
                t = new Token(Token.Type.LITERAL, word);
            } else if (variable.matcher(word).matches()) {
                t = new Token(Token.Type.VARIABLE, word);
            } else {
                // If word does not match any known patterns or symbols
                t = new Token(Token.Type.UNKNOWN, word);
            }
        }

        tokenList.add(t);
        // Adding the token to the count map
        if (count.containsKey(t)) {
            count.put(t, count.get(t)+1);
        } else {
            count.put(t, 1);
        }
    }

    public List<Token> getTokens() {
        return tokenList;
    }

    public Map<Token, Integer> getCount() {
        return count;
    }
}