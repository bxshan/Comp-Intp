package LL1;

/**
 * For type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class For extends Statement
{
    private Assignment init;
    private Expression to;
    private Statement _do;
    private Variable var;

    /**
     * For type node constructor
     * @param init init
     * @param to to
     * @param _do _do
     */
    public For(Assignment init, Expression to, Statement _do)
    {
        this.init = init;
        this.to = to;
        this._do = _do;
        this.var = init.getVar();
    }

    /**
     * gets init
     * @return init as Assignment
     */
    public Assignment getInit()
    {
        return this.init;
    }

    /**
     * gets to
     * @return to as Expression
     */
    public Expression getTo()
    {
        return this.to;
    }

    /**
     * gets do
     * @return do as Statement
     */
    public Statement getDo()
    {
        return this._do;
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
     * @return string representation of For type 
     */
    @Override
    public String toString()
    {
        String res = "FOR " + init + " TO " + to + " DO " + _do;
        return res;
    }
}
