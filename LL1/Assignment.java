package LL1;

/**
 * Assignment type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Assignment extends Statement
{
    private Variable var;
    private Expression expr;

    /**
     * Assignment type node constructor
     * @param var var
     * @param expr expr
     */
    public Assignment(Variable var, Expression expr)
    {
        this.var = var;
        this.expr = expr;
    }

    /**
     * gets var
     * @return var as Variable
     */
    public Variable getVar()
    {
        return this.var;
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
     * @return string representation of Assignment type 
     */
    @Override
    public String toString()
    {
        return var.toString() + " := " + expr.toString() + ";";
    }
}
