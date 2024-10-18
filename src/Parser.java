import java.util.List;
public class Parser {

    public void parse(List<Token> tokens) { //Analisis sintactico
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
        // Aquí implementarías el análisis detallado de la declaración if
        return index + 1; // Simulando que avanzamos por el código
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
        // Validar la sintaxis de la declaración de una variable
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
        // Verificar si es una instrucción de 'write' o 'read'
        String ioOperation = tokens.get(index).getValue();
        System.out.println("Reconociendo una instrucción de E/S: " + ioOperation);

        // Asegurarse de que el siguiente token es un identificador o un número
        if (tokens.get(index + 1).getType() == TokenType.IDENTIFIER || tokens.get(index + 1).getType() == TokenType.NUMBER) {
            System.out.println("Operación de E/S con: " + tokens.get(index + 1).getValue());
            return index + 2; // Avanzar después de la instrucción E/S
        } else {
            System.out.println("Error: Se esperaba un identificador o número después de " + ioOperation);
        }
        return index + 1;
    }
}
