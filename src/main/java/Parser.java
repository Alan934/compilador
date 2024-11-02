package main.java;

import java.util.List;

public class Parser {

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
                    index = parseBreakStatement(tokens, index); // Nueva regla para break
                } else if (token.getValue().equals("write") || token.getValue().equals("read")) {
                    index = parseIOStatement(tokens, index);  // Nueva regla para read/write
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
                // Aquí podrías agregar lógica para analizar el contenido dentro del bloque
                return braceIndex + 1; // Avanzar después del '{'
            } else {
                throw new RuntimeException("Error: Se esperaba '{' después de la condición.");
            }
        } else {
            throw new RuntimeException("Error: Se esperaba '(' después de 'if'.");
        }
    }


    private int parseWhileStatement(List<Token> tokens, int index) {
        System.out.println("Reconociendo una declaración 'while' en el token " + tokens.get(index).getValue());
        // Implementación de análisis detallado de la declaración while
        return index + 1;
    }

    private int parseAssignment(List<Token> tokens, int index) {
        System.out.println("Reconociendo una asignación para " + tokens.get(index).getValue());
        // Implementación del análisis de asignación
        return index + 1;
    }

    private int parseVariableDeclaration(List<Token> tokens, int index) {
        System.out.println("Reconociendo una declaración de variable con tipo " + tokens.get(index).getValue());

        // Asegurarse de que el siguiente token es un identificador
        if (tokens.get(index + 1).getType() == TokenType.IDENTIFIER) {
            System.out.println("Identificador: " + tokens.get(index + 1).getValue());
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
        return index + 1;
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
        System.out.println("Reconociendo una declaración 'break'");
        // Implementación del análisis para 'break'
        return index + 1; // Avanzar después del 'break'
    }
}