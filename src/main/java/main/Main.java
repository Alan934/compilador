package main;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de código de entrada para el compilador
        String inputCode = "/* Ejemplo de múltiples consignas */\n" +
                "long _var = 10.66; // Inicialización de _var1\n" +
                "double _var2 = 20.5; // Inicialización de _var2\n" +
                "// Control de flujo\n" +
                "if (_var >= 10) { write(_var); }\n" +
                "// Bucle while\n" +
                "while (_var < 20) {\n" +
                "    _var = _var + 1; // Incremento de _var1\n" +
                "    if (_var == 15) { break; } // Salir del bucle si _var1 es 15\n" +
                "}\n" +
                "// Operaciones aritméticas\n" +
                "_var = _var + 5 * 2 - 3 / 1;\n" +
                "_var2 = _var2 / 2.0;\n" +
                "// Fin del código de prueba\n";

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

        try {
            // Tokenizar el código de entrada
            System.out.println("---- Tokens ----");
            lexer.tokenize(inputCode);
            lexer.getTokens().forEach(System.out::println);

            // Analizar el código (análisis sintáctico)
            System.out.println("\n---- Análisis Sintáctico ----");
            parser.parse(lexer.getTokens());

            // Analizar el código (análisis semántico)
            System.out.println("\n---- Análisis Semántico ----");
            semanticAnalyzer.analyze(lexer.getTokens());

            // Mostrar tabla de símbolos resultante
            System.out.println("\n---- Tabla de Símbolos ----");
            semanticAnalyzer.getSymbolTable().forEach((variable, type) ->
                    System.out.println("Variable: " + variable + ", Tipo: " + type));
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
