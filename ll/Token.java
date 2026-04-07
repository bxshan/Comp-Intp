package ll;

/**
 * Represents a single lexical token produced by the {@link Lexer}.
 * <p>
 * Each token carries its {@link TokenType}, the raw source text (lexeme),
 * an optional evaluated literal value, and the source line number where it
 * was found.
 * </p>
 *
 * @author Boxuan Shan
 * @version 03242025
 */
public class Token
{
    /**
     * Enumeration of every terminal symbol recognised by the language grammar.
     * <p>
     * Categories:
     * <ul>
     *   <li><b>Keywords</b> – reserved words such as {@code BEGIN}, {@code END},
     *       {@code IF}, {@code WHILE}, {@code FOR}, {@code PROCEDURE}, etc.</li>
     *   <li><b>Symbols</b> – arithmetic operators ({@code + - * /}),
     *       assignment ({@code :=}), parentheses, brackets, semicolons, etc.</li>
     *   <li><b>Relational operators</b> – {@code = <> < > <= >=}.</li>
     *   <li><b>Literals</b> – {@code ID} (identifier), {@code NUMBER}
     *       (integer literal), {@code STRING} (string literal).</li>
     *   <li><b>Control</b> – {@code EOF} marks the end of input.</li>
     *   <li><b>Meta</b> – {@code COMMENT} is not a true terminal; it is used
     *       internally to represent source comments that have been stripped.</li>
     * </ul>
     * </p>
     */
    public enum TokenType
    {
        // Keywords
        BEGIN, END, WRITELN, READLN, TRUE, FALSE, AND, NOT, OR, MOD, ARRAY,
        IF, THEN, ELSE, WHILE, DO, FOR, TO, REPEAT, UNTIL, BREAK, CONTINUE, PROCEDURE, EXIT,

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

    /** The grammatical category of this token. */
    public final TokenType type;

    /** The exact text from the source that produced this token. */
    public final String lexeme;

    /**
     * The evaluated value of the token, if applicable.
     * <ul>
     *   <li>For {@code NUMBER} tokens this is an {@link Integer}.</li>
     *   <li>For {@code STRING} tokens this is a {@link String} with surrounding
     *       quotes removed.</li>
     *   <li>{@code null} for all other token types.</li>
     * </ul>
     */
    public final Object literal;

    /** The 1-based line number in the source file where this token appears. */
    public final int line;

    /**
     * Constructs a new Token with the given attributes.
     *
     * @param type    the grammatical category of this token
     * @param lexeme  the raw source text that was matched
     * @param literal the evaluated literal value ({@link Integer} for NUMBER,
     *                {@link String} for STRING, {@code null} otherwise)
     * @param line    the 1-based source line number of this token
     */
    public Token(TokenType type, String lexeme, Object literal, int line)
    {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    /**
     * Returns a human-readable representation of this token for debugging.
     *
     * @return a string of the form {@code Token{type=..., lexeme='...', literal=..., line=...}}
     */
    @Override
    public String toString()
    {
        return "Token{" +
               "type=" + type +
               ", lexeme='" + lexeme + "'" +
               ", literal=" + literal +
               ", line=" + line +
               '}';
    }
}
