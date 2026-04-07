package ll;

/**
 * AST node representing an integer literal expression.
 * <p>
 * {@code Number} wraps a single {@code int} value parsed directly from a
 * numeric token in the source.  It is the leaf node for all integer constant
 * expressions, including those synthesised by the parser for unary minus
 * (e.g. {@code 0 - expr}).
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 */
public class Number extends Expression
{
    /** The integer value of this literal. */
    private int num;

    /**
     * Constructs a {@code Number} node with the given integer value.
     *
     * @param num the integer value of this literal
     */
    public Number(int num)
    {
        this.num = num;
    }

    /**
     * Returns the integer value of this literal.
     *
     * @return the integer value stored in this node
     */
    public int getVal()
    {
        return this.num;
    }

    /**
     * Returns the decimal string representation of the integer value.
     *
     * @return the integer value formatted as a decimal string
     */
    @Override
    public String toString()
    {
        return Integer.toString(num);
    }
}
