/*
 Descripción de los Tests
testTokenizeValidInput: Verifica que la tokenización de una declaración de variable sea correcta.
testParseValidIfStatement: Verifica que un bloque if válido no genere excepciones.
testInvalidBreakOutsideLoop: Comprueba que break fuera de un bucle genera un error.
testInvalidVariableDeclaration: Verifica que los identificadores inválidos (que comienzan con números) generen una excepción.
testInvalidIOStatement: Asegura que write() sin argumentos lance una excepción.
testMissingThenInIfStatement: Verifica que falte la llave después de if y arroje un error.
testParseWhileStatement: Confirma que un bloque while válido no lance excepciones.
testInvalidOperatorUsage: Verifica que el uso incorrecto de operadores arroje un error.
testParseAssignment: Verifica que una asignación simple sea válida.
testInvalidParenthesesInIfStatement: Comprueba que falte el paréntesis de apertura en if y lance una excepción.
*/
package test.java;

import main.Lexer;
import main.Parser;
import main.Token;
import main.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {
    private Lexer lexer;
    private Parser parser;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
        parser = new Parser();
    }

    @Test
    void testTokenizeValidInput() {
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
    
        assertEquals(TokenType.NUMBER, tokens.get(3).getType());
        assertEquals("10", tokens.get(3).getValue());
    
        assertEquals(TokenType.SEMICOLON, tokens.get(4).getType());
        assertEquals(";", tokens.get(4).getValue());
    }
    

    @Test
    void testParseValidIfStatement() {
        String code = "if (_var >= 10) { write(_var); }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        assertDoesNotThrow(() -> parser.parse(tokens), "El bloque if debería ser válido y no lanzar excepción");
    }

    @Test
    void testInvalidBreakOutsideLoop() {
        String code = "break;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción para break fuera de un bucle");
        assertEquals("Error: Palabra clave inesperada break", thrown.getMessage());
    }

    @Test
    void testInvalidVariableDeclaration() {
        String code = "long 123abc = 10;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción por identificador no válido");
        assertEquals("Error: Se esperaba un identificador después del tipo de variable.", thrown.getMessage());
    }

    @Test
    void testInvalidIOStatement() {
        String code = "write();";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción para write sin argumentos");
        assertEquals("Error: Se esperaba un identificador o número dentro de '()' después de write", thrown.getMessage());
    }

    @Test
    void testMissingThenInIfStatement() {
        String code = "if (_var >= 10) write(_var);";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción por falta de llaves después del if");
        assertEquals("Error: Se esperaba '{' después de la condición.", thrown.getMessage());
    }

//    @Test
//    void testParseWhileStatement() {
//        String code = "while (_var < 10 { _var = _var + 1; }"; // Error: Falta el paréntesis de cierre
//        lexer.tokenize(code);
//        List<Token> tokens = lexer.getTokens();
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
//                "Se esperaba una excepción debido a la falta del paréntesis de cierre en while");
//        assertEquals("Error: Se esperaba ')' para cerrar la condición de 'while'.", thrown.getMessage());
//    }

//    @Test
//    void testInvalidOperatorUsage() {
//        String code = "long _var = 10 +;"; // Error: Operador inesperado al final de la expresión
//        lexer.tokenize(code);
//        List<Token> tokens = lexer.getTokens();
//
//        Exception thrown = assertThrows(Exception.class, () -> parser.parse(tokens),
//                "Se esperaba una excepción para operador mal usado");
//        assertEquals("Error: Operador inesperado al final de la expresión", thrown.getMessage());
//    }

    // @Test
    // void testParseWhileStatement() {
    //     String code = "while (_var < 10) { _var = _var + 1; }";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     assertDoesNotThrow(() -> parser.parse(tokens), "El bloque while debería ser válido y no lanzar excepción");
    // }

    // @Test
    // void testInvalidOperatorUsage() {
    //     String code = "long _var = 10 +;";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
    //             "Se esperaba una excepción para operador mal usado");
    //     assertEquals("Error: Operador inesperado al final de la expresión", thrown.getMessage());
    // }

    // @Test
    // void testParseAssignment() {
    //     String code = "_var = 20;";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     assertDoesNotThrow(() -> parser.parse(tokens), "La asignación debería ser válida y no lanzar excepción");
    // }

    // @Test
    // void testParseAssignment() {
    //     String code = "_var = 20;";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     assertDoesNotThrow(() -> parser.parse(tokens), "La asignación debería ser válida y no lanzar excepción");
    // }

    @Test
    void testInvalidAssignmentWithoutEqualSign() {
        String code = "_var 20;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción para asignación sin '='");
        assertEquals("Error: Se esperaba un '=' para la asignación.", thrown.getMessage());
    }

    // @Test
    // void testInvalidAssignmentWithIncorrectOperator() {
    //     String code = "_var = + 20;";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
    //             "Se esperaba una excepción por operador inesperado en la asignación");
    //     assertEquals("Error: Operador inesperado en la asignación.", thrown.getMessage());
    // }

    // @Test
    // void testInvalidParenthesesInIfStatement() {
    //     String code = "if _var >= 10) { write(_var); }";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
    //             "Se esperaba una excepción por paréntesis faltante en la declaración if");
    //     assertEquals("Error: Se esperaba '(' después de 'if'.", thrown.getMessage());
    // }
//     @Test
// void testParseValidWhileStatement() {
//     String code = "while (_var < 10) { _var = _var + 1; }";
//     lexer.tokenize(code);
//     List<Token> tokens = lexer.getTokens();

//     assertDoesNotThrow(() -> parser.parse(tokens), "El bloque while debería ser válido y no lanzar excepción");
// }

    @Test
    void testInvalidWhileMissingBrace() {
        String code = "while (_var < 10) _var = _var + 1;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción por falta de llaves después del while");
        assertEquals("Error: Se esperaba '{' después de la condición de 'while'.", thrown.getMessage());
    }

    @Test
    void testInvalidWhileMissingParenthesis() {
        String code = "while _var < 10) { _var = _var + 1; }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción por paréntesis faltante en la declaración while");
        assertEquals("Error: Se esperaba '(' después de 'while'.", thrown.getMessage());
    }

    // @Test
    // void testInvalidOperatorUsage() {
    //     String code = "long _var = 10 +;";
    //     lexer.tokenize(code);
    //     List<Token> tokens = lexer.getTokens();

    //     RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
    //             "Se esperaba una excepción para uso incorrecto de operador");
    //     assertEquals("Error: Operador inesperado en la asignación.", thrown.getMessage());
    // }
    @Test
    void testInvalidParenthesesInIfStatement() {
        String code = "if _var >= 10) { write(_var); }";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();
    
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción por paréntesis faltante en la declaración if");
        assertEquals("Error: Se esperaba '(' después de 'if'.", thrown.getMessage());
    }
    
    @Test
    void testValidIOStatement() {
        String code = "write(_var);";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();
    
        assertDoesNotThrow(() -> parser.parse(tokens), "La declaración de E/S debería ser válida y no lanzar excepción");
    }

    @Test
    void testInvalidIOStatementWithoutArguments() {
        String code = "write();";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción para write sin argumentos");
        assertEquals("Error: Se esperaba un identificador o número dentro de '()' después de write", thrown.getMessage());
    }

    @Test
    void testInvalidBreakStatementOutsideLoop() {
        String code = "break;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción para break fuera de un bucle");
        assertEquals("Error: Palabra clave inesperada break", thrown.getMessage());
    }

    @Test
    void testInvalidVariableDeclarationWithInvalidIdentifier() {
        String code = "long 123abc = 10;";
        lexer.tokenize(code);
        List<Token> tokens = lexer.getTokens();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> parser.parse(tokens),
                "Se esperaba una excepción por identificador no válido");
        assertEquals("Error: Se esperaba un identificador después del tipo de variable.", thrown.getMessage());
    }
}
