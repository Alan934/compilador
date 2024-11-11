package main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private List<Token> tokens = new ArrayList<>();
    private int currentLine = 1;

    // Patrones regex para los diferentes tipos de tokens
    private static final String COMMENT_MULTI = "/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/";
    private static final String COMMENT_SINGLE = "//[^\\n]*";
    private static final String KEYWORDS = "\\b(long|double|if|then|else|while|break|read|write)\\b";
    private static final String OPERATORS = "[+\\-*/><=]|(>=|<=|==|!=|<>)";
    private static final String IDENTIFIERS = "\\b_[a-zA-Z][a-zA-Z0-9]*\\b";
    private static final String DOUBLES = "\\b\\d+\\.\\d+\\b"; // Patrón para números decimales
    private static final String INTEGERS = "\\b\\d+\\b";       // Patrón para números enteros
    private static final String PARENTHESIS = "[()]";
    private static final String BRACES = "[{}]";
    private static final String SEMICOLON = ";";

    // Patrón combinado para buscar todos los tokens
    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
            COMMENT_MULTI + "|" + COMMENT_SINGLE + "|" + KEYWORDS + "|" + IDENTIFIERS + "|" + OPERATORS + "|" +
                    DOUBLES + "|" + INTEGERS + "|" + PARENTHESIS + "|" + BRACES + "|" + SEMICOLON);

    public void tokenize(String input) {
        // Separar el input en líneas
        String[] lines = input.split("\n");

        for (String line : lines) {
            Matcher matcher = TOKEN_PATTERNS.matcher(line);

            while (matcher.find()) {
                String tokenValue = matcher.group();
                TokenType tokenType = determineTokenType(tokenValue);

                if (tokenType != null) {
                    tokens.add(new Token(tokenType, tokenValue, currentLine));
                }
            }
            currentLine++;
        }
    }

    private TokenType determineTokenType(String token) {
        if (token.matches(COMMENT_MULTI)) return TokenType.COMMENT_MULTI;
        if (token.matches(COMMENT_SINGLE)) return TokenType.COMMENT_SINGLE;
        if (token.matches(KEYWORDS)) return TokenType.KEYWORD;
        if (token.matches(IDENTIFIERS)) return TokenType.IDENTIFIER;
        if (token.matches(OPERATORS)) return TokenType.OPERATOR;
        if (token.matches(DOUBLES)) return TokenType.DOUBLE;
        if (token.matches(INTEGERS)) return TokenType.INTEGER;
        if (token.matches(PARENTHESIS)) return TokenType.PARENTHESIS;
        if (token.matches(BRACES)) return TokenType.BRACE;
        if (token.matches(SEMICOLON)) return TokenType.SEMICOLON;

        // Si el token no coincide con ningún tipo válido, es un error léxico
        reportError(token, currentLine);
        return TokenType.UNKNOWN;
    }

    private void reportError(String token, int lineNumber) {
        throw new RuntimeException("Error léxico en línea " + lineNumber + ": Token inesperado '" + token + "'");
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
