public class Main {
    public static void main(String[] args) {
        // Ejemplo de código de entrada para el compilador
        String inputCode = "/* Ejemplo */ long _var = 10; // Comentario en una línea\n" +
                "if (_var >= 10) { write(_var); }";

        // Crear instancias del Lexer y Parser
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        // Tokenizar el código de entrada
        System.out.println("---- Tokens ----");
        lexer.tokenize(inputCode);
        lexer.getTokens().forEach(System.out::println); // Imprimir los tokens

        // Analizar el código (análisis sintáctico)
        System.out.println("\n---- Análisis Sintáctico ----");
        parser.parse(lexer.getTokens());
    }
}