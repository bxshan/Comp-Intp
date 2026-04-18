package codegen;

/**
 * Readln type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Readln extends Statement
{
    private Variable var;

    /**
     * Readln type node constructor
     * @param var var
     */
    public Readln(Variable var)
    {
        this.var = var;
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
     * toString override
     * @return string representation of Readln type 
     */
    @Override
    public String toString()
    {
        return "READLN(" + var.toString() + ");";
    }
}
