package ll;

import java.util.ArrayList;

/**
 * Root AST node representing an entire parsed program.
 * <p>
 * A {@code Program} is the top-level node returned by
 * {@link Parser#parseProgram()}.  It holds the flat, ordered sequence of
 * top-level {@link Statement} nodes (including any
 * {@link ProcedureDeclaration} nodes) that make up the source file.
 * </p>
 *
 * @author Boxuan Shan
 * @version 03242025
 * @see Parser#parseProgram()
 */
public class Program
{
    /** The ordered list of top-level statements in this program. */
    private ArrayList<Statement> stmts;

    /**
     * Constructs a new {@code Program} node.
     *
     * @param stmts the ordered list of top-level statements that constitute
     *              the program body; must not be {@code null}
     */
    public Program(ArrayList<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Returns the ordered list of top-level statements.
     *
     * @return an {@link ArrayList} of {@link Statement} nodes in source order;
     *         never {@code null}, but may be empty
     */
    public ArrayList<Statement> getStmts()
    {
        return stmts;
    }
}
