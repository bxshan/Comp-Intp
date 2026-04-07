package ll;

/**
 * AST node representing a binary (or unary) operation expression.
 * <p>
 * {@code BinOp} covers all two-operand operations in the language:
 * <ul>
 *   <li>Arithmetic: {@code +}, {@code -}, {@code *}, {@code /}, {@code mod}</li>
 *   <li>Relational: {@code =}, {@code <>}, {@code <}, {@code >},
 *       {@code <=}, {@code >=}</li>
 *   <li>Logical: {@code AND}, {@code OR}</li>
 *   <li>Unary NOT (encoded as {@code NOT} with a dummy {@code false} left
 *       operand)</li>
 *   <li>Unary minus (encoded as {@code -} with a {@code 0} left operand)</li>
 * </ul>
 * </p>
 *
 * @author Boxuan Shan
 * @version 02242025
 * @see Expression
 */
public class BinOp extends Expression
{
    /** The operator symbol or keyword (e.g. {@code "+"}, {@code "AND"}, {@code "NOT"}). */
    private String op;

    /** The left operand expression. */
    private Expression expr1;

    /** The right operand expression. */
    private Expression expr2;

    /**
     * Constructs a {@code BinOp} node with the given operator and operands.
     *
     * @param op    the operator symbol or keyword string (e.g. {@code "+"},
     *              {@code "AND"}, {@code "NOT"}); must not be {@code null}
     * @param expr1 the left operand; must not be {@code null}
     * @param expr2 the right operand; must not be {@code null}
     */
    public BinOp(String op, Expression expr1, Expression expr2)
    {
        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    /**
     * Returns the operator of this binary operation.
     *
     * @return the operator symbol or keyword string (e.g. {@code "+"}, {@code "AND"})
     */
    public String getOp()
    {
        return op;
    }

    /**
     * Returns the left operand of this binary operation.
     *
     * @return the left-hand {@link Expression} operand
     */
    public Expression getExpr1()
    {
        return expr1;
    }

    /**
     * Returns the right operand of this binary operation.
     *
     * @return the right-hand {@link Expression} operand
     */
    public Expression getExpr2()
    {
        return expr2;
    }

    /**
     * Returns a parenthesised infix representation of this operation.
     *
     * @return a string of the form {@code (left op right)}
     */
    @Override
    public String toString()
    {
        return "(" + expr1.toString() + " " + op + " " + expr2.toString() + ")";
    }
}
