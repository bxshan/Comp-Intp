package LL1;

public class Token { // Changed from record to class
    public enum TokenType {
        // Keywords
        BEGIN, END, WRITELN, READLN, TRUE, FALSE, AND, NOT, OR, MOD, ARRAY,
        IF, THEN, ELSE, WHILE, DO, FOR, TO, REPEAT, UNTIL, BREAK, CONTINUE, PROCEDURE, EXIT, // Added DO

        // Symbols
        PLUS, MINUS, STAR, SLASH,
        ASSIGN, LPAREN, RPAREN, LBRACK, RBRACK, SEMI, COMMA, DOT, COLON,

        // Relational operators
        EQ, NE, LT, GT, LE, GE,

        // Literals
        ID, NUMBER, STRING,

        // Control
        EOF,

        // Not really a token, but used for comments
        COMMENT
    }

    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token{" +
               "type=" + type +
               ", lexeme='" + lexeme + "'" + // Corrected escaping for lexeme
               ", literal=" + literal +
               ", line=" + line +
               '}';
    }
}
