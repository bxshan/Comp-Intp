package ll;

/**
 * AST node representing a source comment (no-op statement).
 * <p>
 * In practice the {@link Lexer} discards line comments ({@code //…}) silently,
 * so a {@code Comment} node is only produced when the parser encounters a
 * {@link Token.TokenType#COMMENT} token that was not stripped at lex time.
 * The evaluator treats this node as a no-op and takes no action.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 */
public class Comment extends Statement
{}
