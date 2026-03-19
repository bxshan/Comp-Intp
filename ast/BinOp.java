package ast;

/**
 * BinOp type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 03192026
 */
public class BinOp extends Expression
{
    private String op;
    private Expression expr1;
    private Expression expr2;

    /**
     * BinOp type node constructor
     * @param op op
     * @param expr1 expr1
     * @param expr2 expr2
     */
    public BinOp(String op, Expression expr1, Expression expr2)
    {
        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    /**
     * gets op
     * @return op as String
     */
    public String getOp()
    {
        return op;
    }

    /**
     * gets expr1
     * @return expr1 as Expression
     */
    public Expression getExpr1()
    {
        return expr1;
    }

    /**
     * gets expr2
     * @return expr2 as Expression
     */
    public Expression getExpr2()
    {
        return expr2;
    }

    /**
     * toString override
     * @return string representation of BinOp type 
     */
    @Override
    public String toString()
    {
        return "(" + expr1.toString() + " " + op + " " + expr2.toString() + ")";
    }
}
