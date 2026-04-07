package ll;

/**
 * AST node representing an array element assignment statement
 * ({@code var[idx] := expr}).
 * <p>
 * Extends {@link Assignment} with an additional index expression.  At runtime
 * the evaluator resolves the array by the variable's name, evaluates the index,
 * and stores the right-hand side value at that position.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Assignment
 * @see ArrayElement
 */
public class ArrayAssignment extends Assignment
{
    /** The expression that evaluates to the integer index to be written. */
    private Expression idx;

    /**
     * Constructs an {@code ArrayAssignment} node.
     *
     * @param var  the array variable being written to; must not be {@code null}
     * @param idx  the expression that evaluates to the target index;
     *             must not be {@code null}
     * @param expr the expression whose value is stored at {@code var[idx]};
     *             must not be {@code null}
     */
    public ArrayAssignment(Variable var, Expression idx, Expression expr)
    {
        super(var, expr);
        this.idx = idx;
    }

    /**
     * Returns the index expression for this array element assignment.
     *
     * @return the {@link Expression} that evaluates to the integer write index
     */
    public Expression getIdx()
    {
        return this.idx;
    }

    /**
     * Returns a Pascal-style array element assignment representation.
     *
     * @return a string of the form {@code var[idx] := expr;}
     */
    @Override
    public String toString()
    {
        return getVar().toString() + "[" + idx + "] := " + getExpression().toString() + ";";
    }
}
