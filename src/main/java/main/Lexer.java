package main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private List<Token> tokens = new ArrayList<>();

    // Patrones regex para los diferentes tipos de tokens
    private static final String COMMENT_MULTI = "/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/";
    private static final String COMMENT_SINGLE = "//[^\\n]*";
    private static final String KEYWORDS = "\\b(long|double|if|then|else|while|break|read|write)\\b";
    private static final String OPERATORS = "[+\\-*/><=]|(>=|<=|==|!=|<>)";
    private static final String IDENTIFIERS = "\\b_[a-zA-Z][a-zA-Z0-9]*\\b";
    private static final String NUMBERS = "\\b\\d+\\b";
    private static final String PARENTHESIS = "[()]";
    private static final String BRACES = "[{}]";
    private static final String SEMICOLON = ";";

    // Patrón combinado para buscar todos los tokens
    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
            COMMENT_MULTI + "|" + COMMENT_SINGLE + "|" + KEYWORDS + "|" + IDENTIFIERS + "|" + OPERATORS + "|" +
                    NUMBERS + "|" + PARENTHESIS + "|" + BRACES + "|" + SEMICOLON);

    // Tokenizar la entrada
    public void tokenize(String input) {
        Matcher matcher = TOKEN_PATTERNS.matcher(input);

        while (matcher.find()) {
            if (matcher.group().matches(COMMENT_MULTI)) {
                tokens.add(new Token(TokenType.COMMENT_MULTI, matcher.group()));
            } else if (matcher.group().matches(COMMENT_SINGLE)) {
                tokens.add(new Token(TokenType.COMMENT_SINGLE, matcher.group()));
            } else if (matcher.group().matches(KEYWORDS)) {
                tokens.add(new Token(TokenType.KEYWORD, matcher.group()));
            } else if (matcher.group().matches(IDENTIFIERS)) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group()));
            } else if (matcher.group().matches(OPERATORS)) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group()));
            } else if (matcher.group().matches(NUMBERS)) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group()));
            } else if (matcher.group().matches(PARENTHESIS)) {
                tokens.add(new Token(TokenType.PARENTHESIS, matcher.group()));
            } else if (matcher.group().matches(BRACES)) {
                tokens.add(new Token(TokenType.BRACE, matcher.group()));
            } else if (matcher.group().matches(SEMICOLON)) {
                tokens.add(new Token(TokenType.SEMICOLON, matcher.group()));
            } else {
                tokens.add(new Token(TokenType.UNKNOWN, matcher.group()));
            }
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }
}

