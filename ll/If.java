package ll;

/**
 * AST node representing an {@code IF … THEN … ELSE} conditional statement.
 * <p>
 * The {@code ELSE} branch is optional; if absent, {@link #getElse()} returns
 * {@code null} and only the {@code THEN} branch is executed when the condition
 * holds.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 */
public class If extends Statement
{
    /** The boolean guard expression tested before each execution of either branch. */
    private Expression condition;

    /** The statement executed when {@link #condition} evaluates to {@code true}. */
    private Statement then;

    /**
     * The statement executed when {@link #condition} evaluates to {@code false},
     * or {@code null} if there is no {@code ELSE} branch.
     */
    private Statement elseStmt;

    /**
     * Constructs an {@code If} node without an {@code ELSE} branch.
     *
     * @param condition the boolean guard expression; must not be {@code null}
     * @param then      the statement to execute when {@code condition} is
     *                  {@code true}; must not be {@code null}
     */
    public If(Expression condition, Statement then)
    {
        this.condition = condition;
        this.then = then;
        this.elseStmt = null;
    }

    /**
     * Constructs an {@code If} node with an optional {@code ELSE} branch.
     *
     * @param condition the boolean guard expression; must not be {@code null}
     * @param then      the statement to execute when {@code condition} is
     *                  {@code true}; must not be {@code null}
     * @param elseStmt  the statement to execute when {@code condition} is
     *                  {@code false}; may be {@code null} to indicate no
     *                  {@code ELSE} branch
     */
    public If(Expression condition, Statement then, Statement elseStmt)
    {
        this.condition = condition;
        this.then = then;
        this.elseStmt = elseStmt;
    }

    /**
     * Returns the boolean guard expression of this conditional.
     *
     * @return the condition {@link Expression} tested at each evaluation
     */
    public Expression getCond()
    {
        return this.condition;
    }

    /**
     * Returns the {@code THEN} branch statement.
     *
     * @return the {@link Statement} executed when the condition is {@code true}
     */
    public Statement getThen()
    {
        return this.then;
    }

    /**
     * Returns the {@code ELSE} branch statement, if present.
     *
     * @return the {@link Statement} executed when the condition is {@code false},
     *         or {@code null} if there is no {@code ELSE} branch
     */
    public Statement getElse()
    {
        return this.elseStmt;
    }

    /**
     * Returns a Pascal-style conditional statement representation.
     *
     * @return a string of the form {@code IF cond THEN then} or
     *         {@code IF cond THEN then ELSE else} when an else branch is present
     */
    @Override
    public String toString()
    {
        String res = "IF " + condition + " THEN " + then;
        if (elseStmt != null)
        {
            res += " ELSE " + elseStmt;
        }
        return res;
    }
}
