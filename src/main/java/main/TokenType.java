package main;

public enum TokenType {
    COMMENT_MULTI,// Comentarios multiples: /* Este es un comentario multiple*/
    COMMENT_SINGLE, // Comentario Simple: //este es un comentario simple
    KEYWORD, //Palabras clave reservadas: `long`, `double`, `if`, `while`, `break`, `read`, `write`
    IDENTIFIER, //Identificadores:  `_var`, `_contador`
    OPERATOR, //Operadores aritméticos, de comparación y lógicos: `+`, `-`, `*`, `/`, `>`, `<`, `>=`, `<=`, `==`, `!=`
    INTEGER,  // Para números enteros
    DOUBLE, //Para numeros reales
    NUMBER, //Números enteros y decimales
    PARENTHESIS, //Paréntesis utilizados para agrupar expresiones o definir condiciones
    BRACE, //Llaves utilizadas para definir bloques de código: `{`, `}`
    SEMICOLON, //Punto y coma utilizado para finalizar expresiones o declaraciones: `;`
    UNKNOWN //Token desconocido, utilizado para manejar errores
}
