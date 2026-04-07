package ll;

/**
 * AST node representing a variable reference expression.
 * <p>
 * {@code Variable} stores only the identifier name of the variable; value
 * lookup is deferred to the evaluator, which resolves the name against the
 * current environment at runtime.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 * @see Assignment
 */
public class Variable extends Expression
{
    /** The identifier name of the variable as it appears in the source. */
    private String name;

    /**
     * Constructs a {@code Variable} node for the given identifier.
     *
     * @param name the variable's identifier string; must not be {@code null}
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Returns the variable's identifier name.
     *
     * @return the identifier string of this variable reference
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the identifier name of this variable.
     *
     * @return the variable's identifier string
     */
    @Override
    public String toString()
    {
        return name;
    }
}
