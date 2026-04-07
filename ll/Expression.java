package ll;

/**
 * Abstract base class for all expression AST nodes.
 * <p>
 * Every construct in the language that produces a value — numeric or string
 * literals, boolean values, variable references, binary operations, array
 * element accesses, and procedure calls — extends {@code Expression}.  The
 * class itself carries no data; subclasses supply the fields and accessors
 * specific to each expression kind.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 */
public abstract class Expression
{
}
