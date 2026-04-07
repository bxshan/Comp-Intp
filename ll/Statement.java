package ll;

/**
 * Abstract base class for all statement AST nodes.
 * <p>
 * Every construct in the language that appears as a standalone instruction —
 * assignments, I/O operations, control-flow structures, procedure declarations,
 * and no-ops — extends {@code Statement}.  The class itself carries no data;
 * subclasses provide the specific fields and accessors relevant to each
 * statement kind.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 */
public abstract class Statement
{
}
