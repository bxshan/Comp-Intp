package ll;

/**
 * AST node representing a {@code READLN} input statement.
 * <p>
 * At runtime the evaluator reads a line of text from standard input, converts
 * it to the appropriate type, and stores the result in the named variable,
 * matching the behaviour of Pascal's {@code Readln} procedure.
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Statement
 * @see Writeln
 */
public class Readln extends Statement
{
    /** The variable into which the user's input is stored. */
    private Variable var;

    /**
     * Constructs a {@code Readln} node that reads input into the given variable.
     *
     * @param var the variable that receives the value read from standard input;
     *            must not be {@code null}
     */
    public Readln(Variable var)
    {
        this.var = var;
    }

    /**
     * Returns the target variable for the input operation.
     *
     * @return the {@link Variable} that will hold the value read from
     *         standard input
     */
    public Variable getVar()
    {
        return this.var;
    }

    /**
     * Returns a Pascal-style {@code READLN} statement representation.
     *
     * @return a string of the form {@code READLN(var);}
     */
    @Override
    public String toString()
    {
        return "READLN(" + var.toString() + ");";
    }
}
