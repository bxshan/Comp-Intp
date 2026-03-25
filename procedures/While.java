package procedures;

/**
 * While type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class While extends Statement
{
    private Expression condition;
    private Statement _do;

    /**
     * While type node constructor
     * @param condition condition
     * @param _do _do
     */
    public While(Expression condition, Statement _do)
    {
        this.condition = condition;
        this._do = _do;
    }

    /**
     * gets cond
     * @return cond as Expression
     */
    public Expression getCond()
    {
        return this.condition;
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
     * toString override
     * @return string representation of While type 
     */
    @Override
    public String toString()
    {
        String res = "WHILE " + condition + " DO " + _do;
        return res;
    }
}
