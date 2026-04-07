package ll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexical analyser (scanner) for the LL(1) language.
 * <p>
 * The {@code Lexer} reads source text from an {@link InputStream} one line at a
 * time and converts it into a stream of {@link Token} objects. Tokens are
 * matched using a prioritised list of compiled regular-expression patterns; the
 * first pattern that matches at the beginning of the remaining line text wins.
 * </p>
 * <p>
 * Line comments beginning with {@code //} cause the rest of that line to be
 * discarded silently. Whitespace between tokens is also silently consumed.
 * </p>
 * <p>
 * Tokens are buffered internally in a queue; callers use
 * {@link #getNextToken()} to consume the next token and
 * {@link #peekNextToken()} to inspect it without consuming it.
 * </p>
 *
 * @author Boxuan Shan
 * @version 03242025
 * @see Token
 * @see Parser
 */
public class Lexer
{
    /** Underlying reader that supplies source lines one at a time. */
    private final BufferedReader reader;

    /** The most recently read source line, or {@code null} at end-of-file. */
    private String currentLine;

    /** 1-based line counter incremented each time a new source line is read. */
    private int lineNumber;

    /**
     * FIFO queue that holds {@link Token} objects produced from the current
     * source line but not yet consumed by the caller.
     */
    private Queue<Token> tokenBuffer;

    /**
     * Map from keyword text to its corresponding {@link Token.TokenType}.
     * Populated once in the static initialiser and consulted during
     * {@link #classifyLexeme(String)} to distinguish reserved words from
     * plain identifiers.
     */
    private static final Map<String, Token.TokenType> KEYWORDS;

    static
    {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("BEGIN", Token.TokenType.BEGIN);
        KEYWORDS.put("END", Token.TokenType.END);
        KEYWORDS.put("WRITELN", Token.TokenType.WRITELN);
        KEYWORDS.put("READLN", Token.TokenType.READLN);
        KEYWORDS.put("TRUE", Token.TokenType.TRUE);
        KEYWORDS.put("FALSE", Token.TokenType.FALSE);
        KEYWORDS.put("AND", Token.TokenType.AND);
        KEYWORDS.put("NOT", Token.TokenType.NOT);
        KEYWORDS.put("OR", Token.TokenType.OR);
        KEYWORDS.put("mod", Token.TokenType.MOD);
        KEYWORDS.put("array", Token.TokenType.ARRAY);
        KEYWORDS.put("IF", Token.TokenType.IF);
        KEYWORDS.put("THEN", Token.TokenType.THEN);
        KEYWORDS.put("ELSE", Token.TokenType.ELSE);
        KEYWORDS.put("WHILE", Token.TokenType.WHILE);
        KEYWORDS.put("FOR", Token.TokenType.FOR);
        KEYWORDS.put("TO", Token.TokenType.TO);
        KEYWORDS.put("REPEAT", Token.TokenType.REPEAT);
        KEYWORDS.put("UNTIL", Token.TokenType.UNTIL);
        KEYWORDS.put("BREAK", Token.TokenType.BREAK);
        KEYWORDS.put("CONTINUE", Token.TokenType.CONTINUE);
        KEYWORDS.put("PROCEDURE", Token.TokenType.PROCEDURE);
        KEYWORDS.put("EXIT", Token.TokenType.EXIT);
    }

    /**
     * Ordered list of compiled regex patterns used to tokenise each source line.
     * <p>
     * Patterns are tried in declaration order; the first match wins, so
     * multi-character operators (e.g. {@code :=}, {@code <>}) appear before
     * their single-character constituents.  Every pattern is anchored to the
     * start of the remaining line text via {@code ^} or via
     * {@link Pattern#quote(String)}.
     * </p>
     */
    private static final List<Pattern> TOKEN_PATTERNS = new ArrayList<>();

    static
    {
        // All patterns should start with ^ to anchor the match to the beginning
        // Line comments
        TOKEN_PATTERNS.add(Pattern.compile("^//.*"));
        // Multi-character operators
        TOKEN_PATTERNS.add(Pattern.compile("^:="));
        TOKEN_PATTERNS.add(Pattern.compile("^<>"));
        TOKEN_PATTERNS.add(Pattern.compile("^<="));
        TOKEN_PATTERNS.add(Pattern.compile("^>="));
        // String literals
        TOKEN_PATTERNS.add(Pattern.compile("^\"[^\"]*\""));
        // Identifiers/Keywords
        TOKEN_PATTERNS.add(Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*"));
        // Integer literals
        TOKEN_PATTERNS.add(Pattern.compile("^\\d+"));
        // Single-character operators and punctuation
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("+")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("-")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("*")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("/")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("=")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("<")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote(">")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("(")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote(")")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("[")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote("]")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote(";")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote(",")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote(":")));
        TOKEN_PATTERNS.add(Pattern.compile(Pattern.quote(".")));
    }

    /**
     * Constructs a new {@code Lexer} that reads source text from the given
     * input stream.
     * <p>
     * The stream is wrapped in a {@link BufferedReader} for line-by-line
     * reading. The first source line is <em>not</em> read eagerly; it is
     * fetched on the first call to {@link #getNextToken()} or
     * {@link #peekNextToken()}.
     * </p>
     *
     * @param in the source input stream to tokenise; must not be {@code null}
     */
    public Lexer(InputStream in)
    {
        reader = new BufferedReader(new InputStreamReader(in));
        lineNumber = 0;
        tokenBuffer = new LinkedList<>();
    }

    /**
     * Reads the next source line from the underlying reader, increments
     * {@link #lineNumber}, and tokenises the line into {@link #tokenBuffer}.
     * <p>
     * If the reader returns {@code null} the end of file has been reached and
     * {@link #currentLine} is set to {@code null} to signal that condition.
     * </p>
     *
     * @throws RuntimeException if an {@link IOException} occurs while reading
     */
    private void advanceLine()
    {
        try
        {
            currentLine = reader.readLine();
            lineNumber++;
            if (currentLine != null)
            {
                tokenizeLine();
            }
        }
        catch (IOException e)
        {
            currentLine = null;
            throw new RuntimeException("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Scans {@link #currentLine} from left to right, skipping whitespace and
     * appending recognised tokens to {@link #tokenBuffer}.
     * <p>
     * When a {@code //} comment is detected the rest of the line is skipped.
     * If no pattern matches the character at the current position a
     * {@link RuntimeException} describing the illegal character is thrown.
     * </p>
     *
     * @throws RuntimeException if an unrecognised character is encountered
     */
    private void tokenizeLine()
    {
        int currentPos = 0;
        String remainingLine;

        while (currentPos < currentLine.length())
        {
            // Skip leading whitespace
            while (currentPos < currentLine.length()
                    && Character.isWhitespace(currentLine.charAt(currentPos)))
            {
                currentPos++;
            }
            if (currentPos == currentLine.length())
            {
                break;
            }

            remainingLine = currentLine.substring(currentPos);
            boolean matched = false;

            for (Pattern pattern : TOKEN_PATTERNS)
            {
                Matcher matcher = pattern.matcher(remainingLine);
                if (matcher.lookingAt())
                {
                    String lexeme = matcher.group();

                    if (lexeme.startsWith("//"))
                    {
                        currentPos = currentLine.length();
                        matched = true;
                        break;
                    }

                    Token.TokenType type = classifyLexeme(lexeme);
                    Object literal = null;

                    switch (type)
                    {
                        case NUMBER:
                            literal = Integer.parseInt(lexeme);
                            break;
                        case STRING:
                            literal = lexeme.substring(1, lexeme.length() - 1);
                            break;
                        default:
                            break;
                    }
                    tokenBuffer.offer(new Token(type, lexeme, literal, lineNumber));
                    currentPos += lexeme.length();
                    matched = true;
                    break;
                }
            }

            if (!matched)
            {
                throw new RuntimeException(
                    "Lexing error: Illegal character '"
                    + currentLine.charAt(currentPos)
                    + "' at line " + lineNumber
                    + ", position " + currentPos
                    + ". Full line: '" + currentLine + "'");
            }
        }
    }

    /**
     * Determines the {@link Token.TokenType} for a matched lexeme string.
     * <p>
     * Resolution order:
     * <ol>
     *   <li>Exact match in {@link #KEYWORDS} → keyword type.</li>
     *   <li>All-digit string → {@link Token.TokenType#NUMBER}.</li>
     *   <li>Double-quoted string → {@link Token.TokenType#STRING}.</li>
     *   <li>Known operator or punctuation character(s) → corresponding type.</li>
     *   <li>Anything else → {@link Token.TokenType#ID} (identifier).</li>
     * </ol>
     * </p>
     *
     * @param lexeme the raw matched text from the source line
     * @return the {@link Token.TokenType} that best describes the lexeme
     */
    private Token.TokenType classifyLexeme(String lexeme)
    {
        if (KEYWORDS.containsKey(lexeme))
        {
            return KEYWORDS.get(lexeme);
        }

        if (lexeme.matches("^\\d+$"))
        {
            return Token.TokenType.NUMBER;
        }
        if (lexeme.matches("^\"[^\"]*\"$"))
        {
            return Token.TokenType.STRING;
        }

        return switch (lexeme)
        {
            case "+" -> Token.TokenType.PLUS;
            case "-" -> Token.TokenType.MINUS;
            case "*" -> Token.TokenType.STAR;
            case "/" -> Token.TokenType.SLASH;
            case ":=" -> Token.TokenType.ASSIGN;
            case "(" -> Token.TokenType.LPAREN;
            case ")" -> Token.TokenType.RPAREN;
            case "[" -> Token.TokenType.LBRACK;
            case "]" -> Token.TokenType.RBRACK;
            case ";" -> Token.TokenType.SEMI;
            case "," -> Token.TokenType.COMMA;
            case "." -> Token.TokenType.DOT;
            case "=" -> Token.TokenType.EQ;
            case "<>" -> Token.TokenType.NE;
            case "<" -> Token.TokenType.LT;
            case ">" -> Token.TokenType.GT;
            case "<=" -> Token.TokenType.LE;
            case ">=" -> Token.TokenType.GE;
            case ":" -> Token.TokenType.COLON;
            default -> Token.TokenType.ID;
        };
    }

    /**
     * Consumes and returns the next {@link Token} from the source.
     * <p>
     * If the internal buffer is empty, additional source lines are read and
     * tokenised until a token is available or end-of-file is reached.  At
     * end-of-file an {@link Token.TokenType#EOF} token is returned.
     * </p>
     *
     * @return the next token in the source stream; never {@code null}
     * @throws RuntimeException if a lexing error is encountered in the source
     */
    public Token getNextToken()
    {
        while (tokenBuffer.isEmpty())
        {
            if (currentLine == null)
            {
                return new Token(Token.TokenType.EOF, "", null, lineNumber);
            }
            advanceLine();
            if (tokenBuffer.isEmpty()
                    && currentLine != null
                    && !currentLine.trim().isEmpty())
            {
                throw new RuntimeException(
                    "Lexing error: Unrecognized token sequence on line "
                    + lineNumber + ": " + currentLine);
            }
        }
        return tokenBuffer.poll();
    }

    /**
     * Peeks at the next token without consuming it.
     * <p>
     * Behaves identically to {@link #getNextToken()} except that the returned
     * token remains at the head of the buffer and will be returned again on
     * the next call to either method.  Useful for single-token lookahead in
     * the parser.
     * </p>
     *
     * @return the next token in the source stream without removing it; never {@code null}
     * @throws RuntimeException if a lexing error is encountered in the source
     */
    public Token peekNextToken()
    {
        while (tokenBuffer.isEmpty())
        {
            if (currentLine == null)
            {
                return new Token(Token.TokenType.EOF, "", null, lineNumber);
            }
            advanceLine();
            if (tokenBuffer.isEmpty()
                    && currentLine != null
                    && !currentLine.trim().isEmpty())
            {
                throw new RuntimeException(
                    "Lexing error: Unrecognized token sequence on line "
                    + lineNumber + ": " + currentLine);
            }
        }
        return tokenBuffer.peek();
    }
}
