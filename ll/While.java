package ll;

/**
 * AST node representing a {@code WHILE … DO} loop statement.
 * <p>
 * The condition is evaluated before each iteration; the body executes only
 * while the condition is {@code true}.  A {@link Break} statement inside the
 * body causes early exit, and a {@link Continue} statement skips to the next
 * condition check.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see Break
 * @see Continue
 */
public class While extends Statement
{
    /** The boolean guard expression evaluated before each iteration. */
    private Expression condition;

    /** The statement (often a {@link Block}) executed on each iteration. */
    private Statement doStmt;

    /**
     * Constructs a {@code While} node.
     *
     * @param condition the boolean guard expression tested before each
     *                  iteration; must not be {@code null}
     * @param doStmt    the body statement executed while {@code condition} is
     *                  {@code true}; must not be {@code null}
     */
    public While(Expression condition, Statement doStmt)
    {
        this.condition = condition;
        this.doStmt = doStmt;
    }

    /**
     * Returns the loop guard expression.
     *
     * @return the boolean {@link Expression} tested before each iteration
     */
    public Expression getCond()
    {
        return this.condition;
    }

    /**
     * Returns the loop body statement.
     *
     * @return the {@link Statement} executed on each iteration while the
     *         condition holds
     */
    public Statement getDo()
    {
        return this.doStmt;
    }

    /**
     * Returns a Pascal-style {@code WHILE} loop representation.
     *
     * @return a string of the form {@code WHILE cond DO body}
     */
    @Override
    public String toString()
    {
        return "WHILE " + condition + " DO " + doStmt;
    }
}
