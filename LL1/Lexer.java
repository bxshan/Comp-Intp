package LL1;

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

public class Lexer {
    private final BufferedReader reader;
    private String currentLine;
    private int lineNumber;
    private Queue<Token> tokenBuffer;

    private static final Map<String, Token.TokenType> KEYWORDS;

    static {
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

    // List of token patterns, ordered by precedence (longer/more specific patterns first)
    private static final List<Pattern> TOKEN_PATTERNS = new ArrayList<>();

    static {
        // All patterns should start with ^ to anchor the match to the beginning of the string segment
        // Line comments
        TOKEN_PATTERNS.add(Pattern.compile("^//.*"));
        // Multi-character operators
        TOKEN_PATTERNS.add(Pattern.compile("^:=" ));
        TOKEN_PATTERNS.add(Pattern.compile("^<>"));
        TOKEN_PATTERNS.add(Pattern.compile("^<="));
        TOKEN_PATTERNS.add(Pattern.compile("^>="));
        // String literals
        TOKEN_PATTERNS.add(Pattern.compile("^\"[^\"]*\""));
        // Identifiers/Keywords
        TOKEN_PATTERNS.add(Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*"));
        // Integer literals
        TOKEN_PATTERNS.add(Pattern.compile("^\\d+"));
        // Single-character operators and punctuation (using Pattern.quote for safety)
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

    public Lexer(InputStream in) {
        reader = new BufferedReader(new InputStreamReader(in));
        lineNumber = 0;
        tokenBuffer = new LinkedList<>();
    }

    private void advanceLine() {
        try {
            currentLine = reader.readLine();
            lineNumber++;
            if (currentLine != null) {
                tokenizeLine();
            }
        } catch (IOException e) {
            currentLine = null; // Indicate EOF or error
            throw new RuntimeException("Error reading input: " + e.getMessage());
        }
    }

    private void tokenizeLine() {
        int currentPos = 0;
        String remainingLine;

        while (currentPos < currentLine.length()) {
            // Skip leading whitespace in the current segment of the line
            while (currentPos < currentLine.length() && Character.isWhitespace(currentLine.charAt(currentPos))) {
                currentPos++;
            }
            if (currentPos == currentLine.length()) break; // Reached end of line after skipping whitespace

            remainingLine = currentLine.substring(currentPos);
            boolean matched = false;

            for (Pattern pattern : TOKEN_PATTERNS) {
                Matcher matcher = pattern.matcher(remainingLine);
                if (matcher.lookingAt()) { // Attempts to match the pattern at the beginning of the region
                    String lexeme = matcher.group();
                    
                    // Ignore line comments: if a comment is found, stop processing the rest of the line
                    if (lexeme.startsWith("//")) {
                        currentPos = currentLine.length(); // Advance to end of line
                        matched = true;
                        break;
                    }

                    Token.TokenType type = classifyLexeme(lexeme);
                    Object literal = null;

                    switch (type) {
                        case NUMBER:
                            literal = Integer.parseInt(lexeme);
                            break;
                        case STRING:
                            literal = lexeme.substring(1, lexeme.length() - 1); // Remove quotes
                            break;
                        default:
                            // No literal for other types
                    }
                    tokenBuffer.offer(new Token(type, lexeme, literal, lineNumber));
                    currentPos += lexeme.length();
                    matched = true;
                    break; // Matched a token, move to the next segment of the line
                }
            }

            if (!matched) {
                // If no pattern matched, it's an illegal character
                throw new RuntimeException("Lexing error: Illegal character '" + currentLine.charAt(currentPos) +
                                           "' at line " + lineNumber + ", position " + currentPos +
                                           ". Full line: '" + currentLine + "'");
            }
        }
    }

    private Token.TokenType classifyLexeme(String lexeme) {
        // Check for keywords
        if (KEYWORDS.containsKey(lexeme)) {
            return KEYWORDS.get(lexeme);
        }

        // Check for literals
        if (lexeme.matches("^\\d+$")) { // Use anchors for full match
            return Token.TokenType.NUMBER;
        }
        if (lexeme.matches("^\"[^\"]*\"$")) { // Use anchors for full match
            return Token.TokenType.STRING;
        }

        // Check for operators and symbols (should match exactly now as patterns are specific)
        return switch (lexeme) {
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
            default -> Token.TokenType.ID; // Default to ID if none of the above
        };
    }

    public Token getNextToken() {
        while (tokenBuffer.isEmpty()) {
            if (currentLine == null) { // EOF reached
                return new Token(Token.TokenType.EOF, "", null, lineNumber);
            }
            advanceLine();
            // After advancing line, if buffer is still empty and line was not just whitespace/comment, it's an error
            if (tokenBuffer.isEmpty() && currentLine != null && !currentLine.trim().isEmpty()) {
                throw new RuntimeException("Lexing error: Unrecognized token sequence on line " + lineNumber + ": " + currentLine);
            }
        }
        return tokenBuffer.poll();
    }

    /**
     * Peeks at the next token without consuming it.
     * Useful for lookahead in the parser.
     * @return The next token.
     */
    public Token peekNextToken() {
        while (tokenBuffer.isEmpty()) {
            if (currentLine == null) { // EOF reached
                return new Token(Token.TokenType.EOF, "", null, lineNumber);
            }
            advanceLine();
            // Same logic as getNextToken for advancing lines
            if (tokenBuffer.isEmpty() && currentLine != null && !currentLine.trim().isEmpty()) {
                throw new RuntimeException("Lexing error: Unrecognized token sequence on line " + lineNumber + ": " + currentLine);
            }
        }
        return tokenBuffer.peek();
    }
}
