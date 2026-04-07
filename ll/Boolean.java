package ll;

/**
 * AST node representing a boolean literal expression ({@code TRUE} or {@code FALSE}).
 * <p>
 * {@code Boolean} wraps a Java {@code boolean} primitive.  It is produced by
 * the parser for the {@code TRUE} and {@code FALSE} keywords, and is also used
 * as the dummy left operand of {@code NOT} expressions in {@link BinOp} nodes.
 * The name shadows {@link java.lang.Boolean}; callers should use the fully-
 * qualified name if both are needed in the same compilation unit.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 * @see BinOp
 */
public class Boolean extends Expression
{
    /** The boolean value of this literal. */
    private boolean bool;

    /**
     * Constructs a {@code Boolean} node with the given boolean value.
     *
     * @param bool {@code true} for a {@code TRUE} literal,
     *             {@code false} for a {@code FALSE} literal
     */
    public Boolean(boolean bool)
    {
        this.bool = bool;
    }

    /**
     * Returns the boolean value of this literal.
     *
     * @return {@code true} if this node represents {@code TRUE},
     *         {@code false} if it represents {@code FALSE}
     */
    public boolean getVal()
    {
        return this.bool;
    }

    /**
     * Returns the Pascal-style keyword representation of this boolean literal.
     *
     * @return {@code "TRUE"} or {@code "FALSE"} depending on the stored value
     */
    @Override
    public String toString()
    {
        return bool ? "TRUE" : "FALSE";
    }
}
