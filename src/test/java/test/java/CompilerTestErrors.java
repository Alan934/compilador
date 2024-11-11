package test.java;

import main.*;
import main.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompilerTestErrors {

    private Lexer lexer;
    private Parser parser;
    private SemanticAnalyzer semanticAnalyzer;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
        parser = new Parser();
        semanticAnalyzer = new SemanticAnalyzer();
    }

    private void assertError(RuntimeException exception, String errorType, int line) {
        assertTrue(exception.getMessage().contains(errorType), "Debería indicar que es un error de tipo " + errorType);
        assertTrue(exception.getMessage().contains("línea " + line), "Debería especificar que ocurre en la línea " + line);
    }

    @Test
    void testInvalidBreakOutsideLoop() {
        String code = "break;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error sintáctico", 1);
    }

    @Test
    void testSemanticErrorDetection() {
        String inputCode = """
            long _x = 10;
            _z = 30; // Error semántico: variable no declarada
            """;

        lexer.tokenize(inputCode);
        List<Token> tokens = lexer.getTokens();
        parser.parse(tokens);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> semanticAnalyzer.analyze(tokens));
        assertError(exception, "Error semántico", 2);
    }

    @Test
    void testInvalidVariableDeclaration() {
        String code = "long 123abc = 10;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba un identificador después del tipo de variable.", 1);
    }

    @Test
    void testInvalidIOStatement() {
        String code = "write();";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba un identificador o número dentro de '()' después de write", 1);
    }

    @Test
    void testMissingThenInIfStatement() {
        String code = "if (_var >= 10) write(_var);";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba '{' después de la condición.", 1);
    }

    @Test
    void testInvalidAssignmentWithoutEqualSign() {
        String code = "_var 20;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba un '=' para la asignación.", 1);
    }

    @Test
    void testInvalidAssignmentWithIncorrectOperator() {
        String code = "_var = + 20;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Operador inesperado en la asignación.", 1);
    }

    @Test
    void testInvalidWhileMissingBrace() {
        String code = "while (_var < 10) _var = _var + 1;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba '{' después de la condición de 'while'.", 1);
    }

    @Test
    void testInvalidWhileMissingParenthesis() {
        String code = "while _var < 10) { _var = _var + 1; }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba '(' después de 'while'.", 1);
    }

    @Test
    void testInvalidOperatorUsage() {
        String code = "long _var = 10 +;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Operador inesperado en la asignación.", 1);
    }

    @Test
    void testInvalidParenthesesInIfStatement() {
        String code = "if _var >= 10) { write(_var); }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba '(' después de 'if'.", 1);
    }

    @Test
    void testInvalidIOStatementWithoutArguments() {
        String code = "write();";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba un identificador o número dentro de '()' después de write", 1);
    }

    @Test
    void testInvalidBreakStatementOutsideLoop() {
        String code = "break;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Palabra clave inesperada break", 1);
    }

    @Test
    void testInvalidVariableDeclarationWithInvalidIdentifier() {
        String code = "long 123abc = 10;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(thrown, "Error: Se esperaba un identificador después del tipo de variable.", 1);
    }

    @Test
    void testSyntaxErrorDetection() {
        String inputCode = """
                long _x = 10;
                if (_x >= 10) write(_x; // Error sintáctico aquí
                """;

        lexer.tokenize(inputCode);
        List<Token> tokens = lexer.getTokens();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertError(exception, "Error sintáctico", 2);
    }
}
