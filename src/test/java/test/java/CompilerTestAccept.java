package test.java;

import main.*;
import main.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTestAccept {
    private Lexer lexer;
    private Parser parser;
    private SemanticAnalyzer semanticAnalyzer;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
        parser = new Parser();
        semanticAnalyzer = new SemanticAnalyzer();
    }

    @Test
    void testTokenizeValidInput() {
        // Verifica que el lexer genere los tokens correctos para una declaración de variable válida con tipo 'long'.
        String code = "long _var = 10;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertEquals(5, tokens.size(), "Debería haber 5 tokens en la declaración de variable incluyendo el punto y coma");
        assertEquals(TokenType.KEYWORD, tokens.get(0).getType());
        assertEquals("long", tokens.get(0).getValue());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals("_var", tokens.get(1).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(2).getType());
        assertEquals("=", tokens.get(2).getValue());
        assertEquals(TokenType.INTEGER, tokens.get(3).getType());
        assertEquals("10", tokens.get(3).getValue());
        assertEquals(TokenType.SEMICOLON, tokens.get(4).getType());
        assertEquals(";", tokens.get(4).getValue());

        System.out.println("testTokenizeValidInput passed.");
    }

    @Test
    void testParseValidIfStatement() {
        // Verifica que un bloque 'if' con estructura y sintaxis válidas se analice sin lanzar excepciones.
        String code = "if (_var >= 10) { write(_var); }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "El bloque if debería ser válido y no lanzar excepción");
    }

    @Test
    void testValidIOStatement() {
        // Verifica que una instrucción de entrada/salida válida ('write') se analice sin errores.
        String code = "write(_var);";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "La declaración de E/S debería ser válida y no lanzar excepción");
    }

    @Test
    void testValidMultipleStatements() {
        // Comprueba que múltiples declaraciones válidas en una sola línea se analicen sin errores.
        String code = "long _x = 10; _x = _x + 5; write(_x);";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "Varias declaraciones deberían ser válidas y no lanzar excepción");
    }

    @Test
    void testValidFunctionCall() {
        // Verifica que una llamada a la función 'write' con un valor entero como argumento sea válida.
        String code = "write(10);";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "La llamada a la función debería ser válida y no lanzar excepción");
    }

    @Test
    void testValidNestedIfStatements() {
        // Verifica que las declaraciones 'if' anidadas con bloques válidos se analicen sin errores.
        String code = "if (_x > 0) { if (_x < 10) { write(_x); } }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "Las declaraciones if anidadas deberían ser válidas y no lanzar excepción");
    }

    @Test
    void testValidVariableDeclaration() {
        // Verifica que una declaración de variable con tipo 'long' y asignación inicial se analice sin errores.
        String code = "long _x = 25;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "La declaración de variable debería ser válida y no lanzar excepción");
        System.out.println("testValidVariableDeclaration passed.");
    }

    @Test
    void testValidFunctionDefinition() {
        // Verifica que una definición de función con un bloque de código sea válida.
        String code = "void myFunction() { write(_var); }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "La definición de función debería ser válida y no lanzar excepción");
    }

    @Test
    void testParseValidWhileStatement() {
        // Verifica que un bucle 'while' válido se analice correctamente sin errores.
        String code = "while (_var < 10) { _var = _var + 1; }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "El bloque while debería ser válido y no lanzar excepción");
        System.out.println("testParseValidWhileStatement passed.");
    }

    @Test
    void testValidVariableInitialization() {
        // Verifica que una inicialización de variable 'long' con un valor entero sea válida.
        String code = "long _x = 5;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "La inicialización de variable debería ser válida y no lanzar excepción");
        System.out.println("testValidVariableInitialization passed.");
    }
}
