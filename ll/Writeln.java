package ll;

/**
 * AST node representing a {@code WRITELN} output statement.
 * <p>
 * At runtime the evaluator evaluates the contained expression and prints its
 * result to standard output followed by a newline, matching the behaviour of
 * Pascal's {@code Writeln} procedure.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see Readln
 */
public class Writeln extends Statement
{
    /** The expression whose evaluated value is printed to standard output. */
    private Expression expr;

    /**
     * Constructs a {@code Writeln} node for the given output expression.
     *
     * @param expr the expression to evaluate and print; must not be {@code null}
     */
    public Writeln(Expression expr)
    {
        this.expr = expr;
    }

    /**
     * Returns the expression to be printed.
     *
     * @return the {@link Expression} whose value is written to standard output
     */
    public Expression getExpression()
    {
        return this.expr;
    }

    /**
     * Returns a Pascal-style {@code WRITELN} statement representation.
     *
     * @return a string of the form {@code WRITELN(expr);}
     */
    @Override
    public String toString()
    {
        return "WRITELN(" + expr.toString() + ");";
    }
}
