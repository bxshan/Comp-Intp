package ll;

/**
 * AST node representing a {@code REPEAT … UNTIL} post-condition loop.
 * <p>
 * Unlike {@link While}, the body is always executed at least once.  After each
 * execution the condition is evaluated; the loop terminates when the condition
 * becomes {@code true}.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see While
 */
public class RepeatUntil extends Statement
{
    /** The body statement (often a {@link Block}) executed at least once. */
    private Statement repeat;

    /** The boolean termination condition evaluated after each iteration. */
    private Expression until;

    /**
     * Constructs a {@code RepeatUntil} node.
     *
     * @param repeat the body statement to execute on each iteration;
     *               must not be {@code null}
     * @param until  the boolean expression that, when {@code true}, causes the
     *               loop to terminate; must not be {@code null}
     */
    public RepeatUntil(Statement repeat, Expression until)
    {
        this.repeat = repeat;
        this.until = until;
    }

    /**
     * Returns the loop body statement.
     *
     * @return the {@link Statement} executed before the condition is checked
     */
    public Statement getRepeat()
    {
        return this.repeat;
    }

    /**
     * Returns the termination condition expression.
     *
     * @return the boolean {@link Expression} evaluated after each iteration;
     *         the loop stops when this evaluates to {@code true}
     */
    public Expression getUntil()
    {
        return this.until;
    }

    /**
     * Returns a Pascal-style {@code REPEAT … UNTIL} loop representation.
     *
     * @return a string of the form {@code REPEAT body UNTIL cond}
     */
    @Override
    public String toString()
    {
        return "REPEAT " + repeat + " UNTIL " + until;
    }
}
