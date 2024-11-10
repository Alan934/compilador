package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private boolean inLoopContext = false; // Para controlar el contexto de bucle
    private Map<String, String> symbolTable = new HashMap<>(); // Tabla de símbolos para el análisis semántico

    public void parse(List<Token> tokens) { // Análisis sintáctico
        int index = 0;
        while (index < tokens.size()) {
            Token token = tokens.get(index);

            if (token.getType() == TokenType.KEYWORD) {
                // Verificar si el token es un tipo de dato
                if (token.getValue().equals("long") || token.getValue().equals("double")) {
                    System.out.println("Reconociendo declaración de variable con tipo " + token.getValue());
                    index = parseVariableDeclaration(tokens, index);
                } else if (token.getValue().equals("if")) {
                    System.out.println("Reconociendo declaración 'if'");
                    index = parseIfStatement(tokens, index);
                } else if (token.getValue().equals("while")) {
                    System.out.println("Reconociendo declaración 'while'");
                    index = parseWhileStatement(tokens, index);
                } else if (token.getValue().equals("break")) {
                    System.out.println("Reconociendo declaración 'break'");
                    index = parseBreakStatement(tokens, index);
                } else if (token.getValue().equals("write") || token.getValue().equals("read")) {
                    System.out.println("Reconociendo instrucción de E/S: " + token.getValue());
                    index = parseIOStatement(tokens, index);
                } else {
                    System.out.println("Error: Palabra clave inesperada " + token.getValue());
                }
            } else if (token.getType() == TokenType.IDENTIFIER) {
                System.out.println("Reconociendo asignación para " + token.getValue());
                index = parseAssignment(tokens, index);
            } else {
                index++;
            }
        }
    }

    private int parseIfStatement(List<Token> tokens, int index) {
        System.out.println("Reconociendo una declaración 'if' en el token " + tokens.get(index).getValue());

        if (tokens.size() > index + 1 && tokens.get(index + 1).getType() == TokenType.PARENTHESIS &&
                tokens.get(index + 1).getValue().equals("(")) {
            int closingParenIndex = index + 1;
            while (closingParenIndex < tokens.size() &&
                    !(tokens.get(closingParenIndex).getType() == TokenType.PARENTHESIS &&
                            tokens.get(closingParenIndex).getValue().equals(")"))) {
                closingParenIndex++;
            }

            if (closingParenIndex >= tokens.size()) {
                throw new RuntimeException("Error: Se esperaba ')' para cerrar la condición de 'if'.");
            }

            int braceIndex = closingParenIndex + 1;
            if (braceIndex < tokens.size() && tokens.get(braceIndex).getType() == TokenType.BRACE &&
                    tokens.get(braceIndex).getValue().equals("{")) {
                System.out.println("Bloque 'if' reconocido con '{' en el índice " + braceIndex);
                return braceIndex + 1;
            } else {
                throw new RuntimeException("Error: Se esperaba '{' después de la condición.");
            }
        } else {
            throw new RuntimeException("Error: Se esperaba '(' después de 'if'.");
        }
    }

    private int parseIOStatement(List<Token> tokens, int index) {
        String ioOperation = tokens.get(index).getValue();
        System.out.println("Reconociendo instrucción de E/S: " + ioOperation);

        if (tokens.size() > index + 1 && tokens.get(index + 1).getType() == TokenType.PARENTHESIS &&
                tokens.get(index + 1).getValue().equals("(")) {

            if (tokens.size() > index + 2 &&
                    (tokens.get(index + 2).getType() == TokenType.IDENTIFIER || tokens.get(index + 2).getType() == TokenType.NUMBER)) {

                System.out.println("Operación de E/S con: " + tokens.get(index + 2).getValue());

                if (tokens.size() > index + 3 && tokens.get(index + 3).getType() == TokenType.PARENTHESIS &&
                        tokens.get(index + 3).getValue().equals(")")) {

                    return index + 4;
                } else {
                    throw new RuntimeException("Error: Se esperaba ')' después del identificador o número en " + ioOperation);
                }
            } else {
                throw new RuntimeException("Error: Se esperaba un identificador o número dentro de '()' después de " + ioOperation);
            }
        } else {
            throw new RuntimeException("Error: Se esperaba '(' después de " + ioOperation);
        }
    }

    private int parseWhileStatement(List<Token> tokens, int index) {
        inLoopContext = true; // Entramos en un contexto de bucle

        // Verifica la apertura del paréntesis
        if (tokens.size() <= index + 1 || !tokens.get(index + 1).getValue().equals("(")) {
            throw new RuntimeException("Error: Se esperaba '(' después de 'while'.");
        }

        int closingParenIndex = index + 1;
        while (closingParenIndex < tokens.size() &&
                !(tokens.get(closingParenIndex).getType() == TokenType.PARENTHESIS &&
                        tokens.get(closingParenIndex).getValue().equals(")"))) {
            closingParenIndex++;
        }
        if (closingParenIndex >= tokens.size()) {
            throw new RuntimeException("Error: Se esperaba ')' para cerrar la condición de 'while'.");
        }

        // Verifica la apertura de la llave del bloque de código
        if (closingParenIndex + 1 >= tokens.size() ||
                !tokens.get(closingParenIndex + 1).getValue().equals("{")) {
            throw new RuntimeException("Error: Se esperaba '{' después de la condición de 'while'.");
        }

        int afterWhileBlockIndex = closingParenIndex + 2;

        // Analizar el contenido del bucle (si existe) antes de salir del contexto de bucle
        while (afterWhileBlockIndex < tokens.size() &&
                !(tokens.get(afterWhileBlockIndex).getType() == TokenType.BRACE &&
                        tokens.get(afterWhileBlockIndex).getValue().equals("}"))) {
            Token token = tokens.get(afterWhileBlockIndex);
            if (token.getType() == TokenType.KEYWORD && token.getValue().equals("break")) {
                parseBreakStatement(tokens, afterWhileBlockIndex);
            }
            afterWhileBlockIndex++;
        }

        if (afterWhileBlockIndex >= tokens.size()) {
            throw new RuntimeException("Error: Se esperaba '}' para cerrar el bloque de 'while'.");
        }

        // Salimos del contexto de bucle después del bloque `while`
        inLoopContext = false;
        return afterWhileBlockIndex + 1;
    }

    private int parseAssignment(List<Token> tokens, int index) {
        String identifier = tokens.get(index).getValue();

        // Validar que el identificador esté definido previamente
        if (!symbolTable.containsKey(identifier)) {
            throw new RuntimeException("Error: Variable '" + identifier + "' no declarada.");
        }

        // Verificar el operador de asignación
        if (!tokens.get(index + 1).getValue().equals("=")) {
            throw new RuntimeException("Error: Se esperaba '=' para la asignación.");
        }

        // Validar que la asignación contenga una expresión aritmética válida
        // Este ejemplo solo cubre casos básicos, para casos más complejos, implementa un análisis de expresiones completo.
        int assignmentStartIndex = index + 2;
        while (assignmentStartIndex < tokens.size() &&
                (tokens.get(assignmentStartIndex).getType() == TokenType.NUMBER ||
                        tokens.get(assignmentStartIndex).getType() == TokenType.OPERATOR ||
                        tokens.get(assignmentStartIndex).getType() == TokenType.IDENTIFIER)) {
            assignmentStartIndex++;
        }

        return assignmentStartIndex; // Avanza al siguiente token después de la asignación
    }


    private int parseVariableDeclaration(List<Token> tokens, int index) {
        String type = tokens.get(index).getValue();
        String identifier = tokens.get(index + 1).getValue();

        // Verificar que el identificador comience con "_"
        if (!identifier.matches("^_[a-zA-Z][a-zA-Z0-9]*$")) {
            throw new RuntimeException("Error: Identificador inválido, debe comenzar con '_' seguido de letras o dígitos.");
        }

        // Agregar a la tabla de símbolos
        symbolTable.put(identifier, type);

        // Manejar la asignación opcional
        if (tokens.get(index + 2).getValue().equals("=")) {
            Token valueToken = tokens.get(index + 3);

            // Validar el tipo de valor asignado
            if (type.equals("long") && valueToken.getType() != TokenType.NUMBER) {
                throw new RuntimeException("Error: Se esperaba un número entero para la variable 'long'.");
            } else if (type.equals("double") && valueToken.getType() != TokenType.NUMBER) {
                throw new RuntimeException("Error: Se esperaba un número para la variable 'double'.");
            }

            return index + 4; // Avanzar después de la asignación
        } else {
            return index + 2; // Solo declaración
        }
    }

    private int parseBreakStatement(List<Token> tokens, int index) {
        if (!inLoopContext) {
            throw new RuntimeException("Error: 'break' solo permitido dentro de bucles.");
        }
        System.out.println("Reconociendo una declaración 'break'");
        return index + 1;
    }

}
