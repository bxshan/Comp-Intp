package ll;

/**
 * AST node representing a variable assignment statement ({@code var := expr}).
 * <p>
 * At runtime the evaluator computes the right-hand side expression and stores
 * the result under the variable's name in the current environment.
 * </p>
 * <p>
 * {@code Assignment} is also used by the parser to wrap procedure calls that
 * appear in statement position; in that case the target variable is the
 * synthetic identifier {@code __ignore}.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see ArrayAssignment
 * @see Variable
 */
public class Assignment extends Statement
{
    /** The variable that receives the computed value. */
    private Variable var;

    /** The expression whose value is assigned to {@link #var}. */
    private Expression expr;

    /**
     * Constructs an {@code Assignment} node.
     *
     * @param var  the target variable; must not be {@code null}
     * @param expr the expression whose evaluated value will be stored in
     *             {@code var}; must not be {@code null}
     */
    public Assignment(Variable var, Expression expr)
    {
        this.var = var;
        this.expr = expr;
    }

    /**
     * Returns the target variable of this assignment.
     *
     * @return the {@link Variable} node that receives the computed value
     */
    public Variable getVar()
    {
        return this.var;
    }

    /**
     * Returns the right-hand side expression of this assignment.
     *
     * @return the {@link Expression} whose value is assigned to the variable
     */
    public Expression getExpression()
    {
        return this.expr;
    }

    /**
     * Returns a Pascal-style assignment statement representation.
     *
     * @return a string of the form {@code var := expr;}
     */
    @Override
    public String toString()
    {
        return var.toString() + " := " + expr.toString() + ";";
    }
}
