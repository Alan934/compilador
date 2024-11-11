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
        Token identifierToken = tokens.get(index + 1);
        String variable = identifierToken.getValue();

        if (symbolTable.containsKey(variable)) {
            reportSemanticError("Variable ya declarada", identifierToken);
        } else {
            // Imprimir la declaración de la variable
            System.out.println("Declarando variable: " + variable + " de tipo: " + typeToken.getValue());
        }

        symbolTable.put(variable, typeToken.getValue());
        return index + 3; // Saltar el tipo, identificador y el token `;` o `=`
    }

    private int analyzeVariableUsage(List<Token> tokens, int index) {
        Token identifierToken = tokens.get(index);
        String variable = identifierToken.getValue();

        if (!symbolTable.containsKey(variable)) {
            reportSemanticError("Variable no declarada", identifierToken);
        } else {
            // Imprimir el uso de la variable
            System.out.println("Usando variable: " + variable);
        }

        return index + 1;
    }

    private void reportSemanticError(String message, Token token) {
        throw new RuntimeException("Error semántico en línea " + token.getLine() + ": " + message + " - Variable: '" + token.getValue() + "'");
    }

    public Map<String, String> getSymbolTable() {
        return symbolTable;
    }
}
