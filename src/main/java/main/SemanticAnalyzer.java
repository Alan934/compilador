package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, String> symbolTable = new HashMap<>(); // Tabla de símbolos para almacenar variables y tipos

    public void analyze(List<Token> tokens) {
        int index = 0;
        while (index < tokens.size()) {
            Token token = tokens.get(index);

            // Verificar declaración de variables
            if (token.getType() == TokenType.KEYWORD && (token.getValue().equals("long") || token.getValue().equals("double"))) {
                index = analyzeVariableDeclaration(tokens, index);
            } else if (token.getType() == TokenType.IDENTIFIER) {
                index = analyzeVariableUsage(tokens, index);
            } else {
                index++;
            }
        }
    }

    private int analyzeVariableDeclaration(List<Token> tokens, int index) {
        String type = tokens.get(index).getValue();

        if (index + 1 < tokens.size() && tokens.get(index + 1).getType() == TokenType.IDENTIFIER) {
            String identifier = tokens.get(index + 1).getValue();

            // Validar que el identificador comienza con "_"
            if (!identifier.startsWith("_")) {
                throw new RuntimeException("Error: El identificador " + identifier + " debe comenzar con '_'.");
            }

            // Verificar si la variable ya fue declarada
            if (symbolTable.containsKey(identifier)) {
                throw new RuntimeException("Error: La variable " + identifier + " ya fue declarada.");
            }

            symbolTable.put(identifier, type); // Añadir a la tabla de símbolos
            System.out.println("Declaración de variable: " + identifier + " con tipo " + type);

            return index + 2; // Avanzar después del identificador
        } else {
            throw new RuntimeException("Error: Se esperaba un identificador después del tipo.");
        }
    }

    private int analyzeVariableUsage(List<Token> tokens, int index) {
        String identifier = tokens.get(index).getValue();

        // Verificar que la variable fue declarada
        if (!symbolTable.containsKey(identifier)) {
            throw new RuntimeException("Error: La variable " + identifier + " no ha sido declarada.");
        }

        // Verificar que se use correctamente según su tipo
        String expectedType = symbolTable.get(identifier);
        System.out.println("Uso de variable: " + identifier + " con tipo " + expectedType);

        return index + 1;
    }

    public Map<String, String> getSymbolTable() {
        return symbolTable;
    }
}
