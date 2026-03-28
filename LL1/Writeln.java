package LL1;

/**
 * Writeln type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Writeln extends Statement
{
    private Expression expr;

    /**
     * Writeln type node constructor
     * @param expr expr
     */
    public Writeln(Expression expr)
    {
        this.expr = expr;
    }

    /**
     * gets expression
     * @return expression as Expression
     */
    public Expression getExpression()
    {
        return this.expr;
    }

    /**
     * toString override
     * @return string representation of Writeln type 
     */
    @Override
    public String toString()
    {
        return "WRITELN(" + expr.toString() + ");";
    }
}
