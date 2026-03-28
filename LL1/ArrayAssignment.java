package LL1;

/**
 * ArrayAssignment type node
 * extends concrete Assignment 
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class ArrayAssignment extends Assignment 
{
    private Expression idx;

    /**
     * ArrayAssignment type node constructor
     * @param var name of Array var
     * @param idx idx to assign
     * @param expr expr to assign var[idx] to
     */
    public ArrayAssignment(Variable var, Expression idx, Expression expr) 
    {
        super(var, expr);
        this.idx = idx;
    }

    /**
     * gets assign idx
     * @return idx as Expression
     */
    public Expression getIdx() 
    {
        return this.idx;
    }

    /**
     * toString override
     * @return string representation of ArrayAssignment type 
     */
    @Override
    public String toString() 
    {
        return getVar().toString() + "[" + idx + "] := " + getExpression().toString() + ";";
    }
}
