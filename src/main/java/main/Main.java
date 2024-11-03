package main;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de código de entrada para el compilador
        String inputCode = "/* Ejemplo */ long _var = 10; // Comentario en una línea\n" +
                "if (_var >= 10) { write(_var); }";

        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        // Tokenizar el código de entrada
        System.out.println("---- Tokens ----");
        lexer.tokenize(inputCode);
        lexer.getTokens().forEach(System.out::println);

        // Analizar el código (análisis sintáctico)
        System.out.println("\n---- Análisis Sintáctico ----");
        parser.parse(lexer.getTokens());
    }
}