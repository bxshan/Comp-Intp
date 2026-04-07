package ll;

/**
 * AST node representing a string literal expression.
 * <p>
 * {@code SString} ("source string") wraps a {@link String} value whose
 * surrounding double-quotes have already been stripped by the {@link Lexer}.
 * The name avoids a collision with {@link java.lang.String}.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 */
public class SString extends Expression
{
    /** The string value of this literal, with surrounding quotes removed. */
    private String str;

    /**
     * Constructs an {@code SString} node with the given string value.
     *
     * @param str the string value of this literal (quotes already stripped);
     *            must not be {@code null}
     */
    public SString(String str)
    {
        this.str = str;
    }

    /**
     * Returns the string value of this literal.
     *
     * @return the unquoted string content stored in this node
     */
    public String getVal()
    {
        return this.str;
    }

    /**
     * Returns the string value directly.
     *
     * @return the unquoted string content of this literal
     */
    @Override
    public String toString()
    {
        return this.str;
    }
}
