package ll;
import java.util.ArrayList;

/**
 * AST node representing a procedure (or function) call expression.
 * <p>
 * A {@code ProcedureCall} encapsulates the callee's name and the list of
 * actual argument expressions supplied at the call site.  It extends
 * {@link Expression} because calls may appear in expression context (e.g.
 * on the right-hand side of an assignment or inside a larger expression).
 * </p>
 * <p>
 * When a call is used as a statement it is wrapped in an {@link Assignment}
 * to the synthetic variable {@code __ignore} by the parser.
 * </p>
 * <p>
 * Example source:
 * <pre>
 *   result := gcd(a, b);
 *   swap(x, y);
 * </pre>
 * </p>
 *
 * @author Boxuan Shan
 * @version 03242025
 * @see ProcedureDeclaration
 * @see Expression
 */
public class ProcedureCall extends Expression
{
    /** The identifier of the procedure or function being called. */
    private String name;

    /**
     * The ordered list of actual argument expressions passed to the procedure.
     * May be empty for zero-argument calls.
     */
    private ArrayList<Expression> args;

    /**
     * Constructs a new {@code ProcedureCall} node.
     *
     * @param name the identifier of the procedure or function to call;
     *             must not be {@code null}
     * @param args the ordered list of actual argument expressions; must not be
     *             {@code null} (pass an empty list for zero-argument calls)
     */
    public ProcedureCall(String name, ArrayList<Expression> args)
    {
        this.name = name;
        this.args = args;
    }

    /**
     * Returns the name of the procedure or function being called.
     *
     * @return the callee identifier string
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the ordered list of actual argument expressions.
     *
     * @return an {@link ArrayList} of {@link Expression} nodes, one per
     *         argument; empty if the call has no arguments
     */
    public ArrayList<Expression> getArgs()
    {
        return this.args;
    }

    /**
     * Returns a human-readable representation of this procedure call.
     *
     * @return a string of the form {@code name(arg1, arg2, ...)}
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(name).append("(");
        for (int i = 0; i < args.size(); i++)
        {
            sb.append(args.get(i));
            if (i < args.size() - 1) sb.append(", ");
        }
        return sb.append(")").toString();
    }
}
