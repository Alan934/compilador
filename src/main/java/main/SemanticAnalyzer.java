package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, String> symbolTable = new HashMap<>();

    public void analyze(List<Token> tokens) {
        int index = 0;
        while (index < tokens.size()) {
            Token token = tokens.get(index);

            try {
                if (token.getType() == TokenType.KEYWORD && (token.getValue().equals("long") || token.getValue().equals("double"))) {
                    index = analyzeVariableDeclaration(tokens, index);
                } else if (token.getType() == TokenType.IDENTIFIER) {
                    index = analyzeVariableUsage(tokens, index);
                } else {
                    index++;
                }
            } catch (RuntimeException e) {
                System.err.println("Error en línea " + token.getLine() + ": " + e.getMessage());
                return;
            }
        }
    }

    private int analyzeVariableDeclaration(List<Token> tokens, int index) {
        Token typeToken = tokens.get(index);
        String type = typeToken.getValue();
        Token identifierToken = tokens.get(index + 1);
        String variable = identifierToken.getValue();

        if (symbolTable.containsKey(variable)) {
            throw new RuntimeException("Error semántico: Variable '" + variable + "' ya declarada en la línea " + identifierToken.getLine());
        }

        symbolTable.put(variable, type);
        return index + 3;
    }

    private int analyzeVariableUsage(List<Token> tokens, int index) {
        Token identifierToken = tokens.get(index);
        String variable = identifierToken.getValue();

        if (!symbolTable.containsKey(variable)) {
            throw new RuntimeException("Error semántico: Variable '" + variable + "' no declarada en la línea " + identifierToken.getLine());
        }

        return index + 1;
    }

    public Map<String, String> getSymbolTable() {
        return symbolTable;
    }
}
