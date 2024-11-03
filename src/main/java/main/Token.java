package main;

public class Token { //Analesis Lexico
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "main.java.Token{" + "type=" + type + ", value='" + value + '\'' + '}';
    }
}

