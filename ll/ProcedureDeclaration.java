package ll;
import java.util.ArrayList;

/**
 * AST node representing a procedure (or function) declaration.
 * <p>
 * A procedure declaration introduces a named, callable block of code with an
 * optional list of formal parameters.  At runtime the procedure is registered
 * in the environment so that subsequent {@link ProcedureCall} expressions can
 * invoke it.
 * </p>
 * <p>
 * Example source:
 * <pre>
 *   PROCEDURE swap(a, b);
 *   BEGIN
 *     ...
 *   END;
 * </pre>
 * </p>
 *
 * @author Boxuan Shan
 * @version 03242025
 * @see ProcedureCall
 * @see Statement
 */
public class ProcedureDeclaration extends Statement
{
    /** The identifier (name) of this procedure as it appears in the source. */
    private String name;

    /** Ordered list of formal parameter names. May be empty if the procedure takes no arguments. */
    private ArrayList<String> params;

    /** The body of the procedure — a single {@link Statement} (often a {@link Block}). */
    private Statement stmt;

    /**
     * Constructs a new {@code ProcedureDeclaration} node.
     *
     * @param name   the procedure's identifier as it appears in the source
     * @param params the ordered list of formal parameter names; must not be
     *               {@code null} (pass an empty list for zero-parameter procedures)
     * @param stmt   the procedure body statement; must not be {@code null}
     */
    public ProcedureDeclaration(String name, ArrayList<String> params, Statement stmt)
    {
        this.name = name;
        this.params = params;
        this.stmt = stmt;
    }

    /**
     * Returns the procedure's identifier.
     *
     * @return the procedure name as declared in the source
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the ordered list of formal parameter names.
     *
     * @return an {@link ArrayList} of parameter name strings; empty if the
     *         procedure accepts no arguments
     */
    public ArrayList<String> getParams()
    {
        return this.params;
    }

    /**
     * Returns the procedure body statement.
     *
     * @return the {@link Statement} that forms the procedure body; typically a
     *         {@link Block}
     */
    public Statement getStmt()
    {
        return this.stmt;
    }

    /**
     * Returns a human-readable representation of this procedure declaration.
     *
     * @return a string of the form {@code PROCEDURE name(p1, p2, ...) body}
     */
    @Override
    public String toString()
    {
        return "PROCEDURE " + name + "(" + String.join(", ", params) + ") " + stmt;
    }
}
