package test.java;

import main.java.Lexer;
import main.java.Parser;
import main.java.Token;
import main.java.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CompilerTest {

    @Test
    public void testTokenizeValidInput() {
        Lexer lexer = new Lexer();
        String input = "long _var = 10; // variable declaration";
        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        assertEquals(4, tokens.size());
        assertEquals(TokenType.KEYWORD, tokens.get(0).getType());
        assertEquals("_var", tokens.get(1).getValue());
        assertEquals("=", tokens.get(2).getValue());
        assertEquals("10", tokens.get(3).getValue());
    }

    @Test
    public void testParseVariableDeclaration() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "long _var = 10;";

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens));
    }

    @Test
    public void testParseIfStatement() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "if (_var >= 10) then write(_var);";

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens));
    }

    @Test
    public void testParseBreakStatement() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "while (true) { break; }";

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens));
    }

    @Test
    public void testValidIOStatement() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "write(_var);";

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens));
    }

    // Funciones incorrectas

    @Test
    public void testInvalidVariableDeclaration() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "long 10_var = 10;"; // Identificador inválido

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertTrue(exception.getMessage().contains("Error: Se esperaba un identificador después del tipo de variable."));
    }

    @Test
    public void testMissingThenInIfStatement() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "if (_var >= 10) write(_var);"; // Falta 'then'

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertTrue(exception.getMessage().contains("Error: Se esperaba 'then' después de 'if'."));
    }

    @Test
    public void testInvalidBreakOutsideLoop() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "break;"; // 'break' fuera de un bucle

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertTrue(exception.getMessage().contains("Error: 'break' solo se puede usar dentro de un bucle."));
    }

    @Test
    public void testInvalidIOStatement() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "write();"; // Falta argumento

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertTrue(exception.getMessage().contains("Error: Se esperaba un identificador o número después de write."));
    }

    @Test
    public void testInvalidOperatorUsage() {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        String input = "_var + ;"; // Uso incorrecto del operador

        lexer.tokenize(input);
        List<Token> tokens = lexer.getTokens();

        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(tokens));
        assertTrue(exception.getMessage().contains("Error: Se esperaba un valor después del operador."));
    }
}
