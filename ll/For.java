package ll;

/**
 * AST node representing a {@code FOR … TO … DO} counted loop statement.
 * <p>
 * The loop variable is initialised by {@link #getInit()}, incremented by one
 * after each iteration, and the body executes while the variable's value does
 * not exceed the upper bound returned by {@link #getTo()}.
 * </p>
 * <p>
 * The loop variable is also accessible via {@link #getVar()} as a convenience;
 * it is derived from the initialisation assignment so both always refer to the
 * same identifier.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see Assignment
 */
public class For extends Statement
{
    /** The initialisation assignment that sets the loop variable's starting value. */
    private Assignment init;

    /** The expression that evaluates to the inclusive upper bound of the loop. */
    private Expression to;

    /** The body statement executed on each iteration. */
    private Statement doStmt;

    /** The loop control variable, derived from {@link #init} for convenience. */
    private Variable var;

    /**
     * Constructs a {@code For} loop node.
     *
     * @param init   the initialisation {@link Assignment} (e.g. {@code i := 1})
     *               that sets the loop variable's starting value; must not be
     *               {@code null}
     * @param to     the expression that evaluates to the inclusive upper bound;
     *               must not be {@code null}
     * @param doStmt the body statement executed on each iteration; must not be
     *               {@code null}
     */
    public For(Assignment init, Expression to, Statement doStmt)
    {
        this.init = init;
        this.to = to;
        this.doStmt = doStmt;
        this.var = init.getVar();
    }

    /**
     * Returns the initialisation assignment of this loop.
     *
     * @return the {@link Assignment} that initialises the loop control variable
     */
    public Assignment getInit()
    {
        return this.init;
    }

    /**
     * Returns the inclusive upper-bound expression of this loop.
     *
     * @return the {@link Expression} that evaluates to the loop's upper bound
     */
    public Expression getTo()
    {
        return this.to;
    }

    /**
     * Returns the loop body statement.
     *
     * @return the {@link Statement} executed on each iteration
     */
    public Statement getDo()
    {
        return this.doStmt;
    }

    /**
     * Returns the loop control variable.
     * <p>
     * This is a convenience accessor; the variable is the same as
     * {@code getInit().getVar()}.
     * </p>
     *
     * @return the {@link Variable} used as the loop counter
     */
    public Variable getVar()
    {
        return this.var;
    }

    /**
     * Returns a Pascal-style {@code FOR} loop representation.
     *
     * @return a string of the form {@code FOR init TO upper DO body}
     */
    @Override
    public String toString()
    {
        return "FOR " + init + " TO " + to + " DO " + doStmt;
    }
}
