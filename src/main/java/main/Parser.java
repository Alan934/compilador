package main;

import java.util.List;

public class Parser {
    private boolean inLoopContext = false; // Para controlar el contexto de bucle

    public void parse(List<Token> tokens) { // Análisis sintáctico
        int index = 0;
        while (index < tokens.size()) {
            Token token = tokens.get(index);

            if (token.getType() == TokenType.KEYWORD) {
                // Verificar si el token es un tipo de dato
                if (token.getValue().equals("long") || token.getValue().equals("double")) {
                    index = parseVariableDeclaration(tokens, index);
                } else if (token.getValue().equals("if")) {
                    index = parseIfStatement(tokens, index);
                } else if (token.getValue().equals("while")) {
                    index = parseWhileStatement(tokens, index);
                } else if (token.getValue().equals("break")) {
                    index = parseBreakStatement(tokens, index);
                } else if (token.getValue().equals("write") || token.getValue().equals("read")) {
                    index = parseIOStatement(tokens, index);
                } else {
                    System.out.println("Error: Palabra clave inesperada " + token.getValue());
                }
            } else if (token.getType() == TokenType.IDENTIFIER) {
                index = parseAssignment(tokens, index);
            } else {
                index++;
            }
        }
    }

    private int parseIfStatement(List<Token> tokens, int index) {
        System.out.println("Reconociendo una declaración 'if' en el token " + tokens.get(index).getValue());

        // Verificar si el siguiente token es '('
        if (tokens.size() > index + 1 && tokens.get(index + 1).getType() == TokenType.PARENTHESIS &&
                tokens.get(index + 1).getValue().equals("(")) {
            // Verificar si el token de cierre de la condición es ')'
            int closingParenIndex = index + 1;
            while (closingParenIndex < tokens.size() &&
                    !(tokens.get(closingParenIndex).getType() == TokenType.PARENTHESIS &&
                            tokens.get(closingParenIndex).getValue().equals(")"))) {
                closingParenIndex++;
            }

            if (closingParenIndex >= tokens.size()) {
                throw new RuntimeException("Error: Se esperaba ')' para cerrar la condición de 'if'.");
            }

            // Verificar que el token después de ')' es '{'
            int braceIndex = closingParenIndex + 1;
            if (braceIndex < tokens.size() && tokens.get(braceIndex).getType() == TokenType.BRACE &&
                    tokens.get(braceIndex).getValue().equals("{")) {
                System.out.println("Bloque 'if' reconocido.");
                return braceIndex + 1; // Avanzar después del '{'
            } else {
                throw new RuntimeException("Error: Se esperaba '{' después de la condición.");
            }
        } else {
            throw new RuntimeException("Error: Se esperaba '(' después de 'if'.");
        }
    }


    private int parseWhileStatement(List<Token> tokens, int index) {
        if (tokens.size() <= index + 1 || !tokens.get(index + 1).getValue().equals("(")) {
            throw new RuntimeException("Error: Se esperaba '(' después de 'while'.");
        }
        // Buscar el paréntesis de cierre
        int closingParenIndex = index + 1;
        while (closingParenIndex < tokens.size() && 
               !(tokens.get(closingParenIndex).getType() == TokenType.PARENTHESIS && 
                 tokens.get(closingParenIndex).getValue().equals(")"))) {
            closingParenIndex++;
        }
        if (closingParenIndex >= tokens.size()) {
            throw new RuntimeException("Error: Se esperaba ')' para cerrar la condición de 'while'.");
        }
        // Comprobar el siguiente token después de ')'
        if (closingParenIndex + 1 >= tokens.size() || 
            !tokens.get(closingParenIndex + 1).getValue().equals("{")) {
            throw new RuntimeException("Error: Se esperaba '{' después de la condición de 'while'.");
        }
        return closingParenIndex + 2; // Moverse más del '{' después del bloque while
    }

    private int parseAssignment(List<Token> tokens, int index) {
        // Verificar que hay suficientes tokens para una asignación
        if (tokens.size() <= index + 2 || !tokens.get(index + 2).getValue().equals("=")) {
            throw new RuntimeException("Error: Se esperaba un '=' para la asignación.");
        }
    
        // Comprobar el tipo del token antes del '='
        if (tokens.get(index + 1).getType() == TokenType.OPERATOR) {
            throw new RuntimeException("Error: Operador inesperado en la asignación.");
        }
    
        // Comprobar el valor asignado
        if (tokens.size() <= index + 3 || 
            (tokens.get(index + 3).getType() != TokenType.NUMBER && 
             tokens.get(index + 3).getType() != TokenType.IDENTIFIER)) {
            throw new RuntimeException("Error: Se esperaba un valor válido después de '='.");
        }
    
        // Verificar si el siguiente token es un operador
        if (tokens.size() > index + 4 && tokens.get(index + 4).getType() == TokenType.OPERATOR) {
            throw new RuntimeException("Error: Operador inesperado después del valor asignado.");
        }
    
        return index + 4; // Avanzar después del valor asignado
    }    

    private int parseVariableDeclaration(List<Token> tokens, int index) {
        System.out.println("Reconociendo una declaración de variable con tipo " + tokens.get(index).getValue());
        if (tokens.get(index + 1).getType() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Error: Se esperaba un identificador después del tipo de variable.");
        }
        if (tokens.get(index + 1).getType() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Error: Se esperaba un identificador válido después del tipo de variable.");
        }
        // Asegurarse de que el siguiente token es un identificador
        if (tokens.get(index + 1).getType() == TokenType.IDENTIFIER) {
            System.out.println("Identificador: " + tokens.get(index + 1).getValue());
            if (tokens.get(index + 1).getType() != TokenType.IDENTIFIER) {
                throw new RuntimeException("Error: Se esperaba un identificador válido después del tipo de variable.");
            }
            // Verificar si después viene un operador de asignación y un valor
            if (tokens.get(index + 2).getValue().equals("=")) {
                System.out.println("Asignación de valor: " + tokens.get(index + 3).getValue());
                return index + 4; // Avanzar después del valor asignado
            } else {
                System.out.println("Error: Se esperaba un '=' para la asignación.");
            }
        } else {
            System.out.println("Error: Se esperaba un identificador después del tipo de variable.");
        }
        if (!tokens.get(index + 2).getValue().equals("=")) {
            throw new RuntimeException("Error: Se esperaba un '=' para la asignación.");
        }
        if (tokens.size() <= index + 3 || tokens.get(index + 3).getType() != TokenType.NUMBER) {
            throw new RuntimeException("Error: Se esperaba un número para la asignación.");
        }
        return index + 4;
    }

    private int parseIOStatement(List<Token> tokens, int index) {
        String ioOperation = tokens.get(index).getValue();
        System.out.println("Reconociendo una instrucción de E/S: " + ioOperation);

        // Verificar que el próximo token es un paréntesis de apertura "("
        if (tokens.size() > index + 1 && tokens.get(index + 1).getType() == TokenType.PARENTHESIS &&
                tokens.get(index + 1).getValue().equals("(")) {

            // Verificar si el siguiente token dentro de los paréntesis es un identificador o número
            if (tokens.size() > index + 2 &&
                    (tokens.get(index + 2).getType() == TokenType.IDENTIFIER || tokens.get(index + 2).getType() == TokenType.NUMBER)) {

                System.out.println("Operación de E/S con: " + tokens.get(index + 2).getValue());

                // Verificar que después del identificador/número haya un paréntesis de cierre ")"
                if (tokens.size() > index + 3 && tokens.get(index + 3).getType() == TokenType.PARENTHESIS &&
                        tokens.get(index + 3).getValue().equals(")")) {

                    return index + 4; // Avanzar después del paréntesis de cierre
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

    private int parseBreakStatement(List<Token> tokens, int index) {
        if (!inLoopContext) {
            throw new RuntimeException("Error: Palabra clave inesperada break");
        }
        System.out.println("Reconociendo una declaración 'break'");
        return index + 1;
    }
    
    
}