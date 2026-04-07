package ll;

import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a {@code BEGIN … END} compound statement (block).
 * <p>
 * A {@code Block} groups zero or more sequential statements between the
 * {@code BEGIN} and {@code END} delimiters.  It is used as the body of
 * control-flow constructs ({@link While}, {@link For}, {@link RepeatUntil})
 * and procedure declarations, as well as at the top level when the programmer
 * explicitly writes {@code BEGIN … END}.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 */
public class Block extends Statement
{
    /** The ordered list of statements enclosed by this block. */
    private List<Statement> stmts;

    /**
     * Constructs a {@code Block} node containing the given statements.
     *
     * @param stmts the ordered list of statements within the block;
     *              must not be {@code null}, but may be empty
     */
    public Block(ArrayList<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Returns the ordered list of statements in this block.
     *
     * @return a {@link List} of {@link Statement} nodes in source order;
     *         never {@code null}, but may be empty
     */
    public List<Statement> getStmts()
    {
        return this.stmts;
    }

    /**
     * Returns a multi-line {@code BEGIN … END} representation of this block.
     *
     * @return a string that starts with {@code BEGIN\n}, lists each statement
     *         indented by two spaces, and ends with {@code END;}
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN\n");
        for (Statement stmt : stmts)
        {
            sb.append("  " + stmt.toString() + "\n");
        }
        sb.append("END;");
        return sb.toString();
    }
}
