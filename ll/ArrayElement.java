package ll;

/**
 * AST node representing an array element access expression ({@code name[idx]}).
 * <p>
 * {@code ArrayElement} captures both the array variable's identifier and the
 * index expression.  The evaluator resolves the array by name from the current
 * environment and then computes the index at runtime.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 * @see ArrayAssignment
 */
public class ArrayElement extends Expression
{
    /** The identifier of the array variable being indexed. */
    private String name;

    /** The index expression evaluated to determine which element to access. */
    private Expression idx;

    /**
     * Constructs an {@code ArrayElement} node for the given array and index.
     *
     * @param name the identifier of the array variable; must not be {@code null}
     * @param idx  the expression that evaluates to the integer index;
     *             must not be {@code null}
     */
    public ArrayElement(String name, Expression idx)
    {
        this.name = name;
        this.idx = idx;
    }

    /**
     * Returns the identifier of the array variable.
     *
     * @return the array variable's identifier string
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the index expression for this element access.
     *
     * @return the {@link Expression} that evaluates to the integer array index
     */
    public Expression getIdx()
    {
        return this.idx;
    }

    /**
     * Returns a bracketed representation of this array element access.
     *
     * @return a string of the form {@code name[idx]}
     */
    @Override
    public String toString()
    {
        return name + "[" + idx + "]";
    }
}
