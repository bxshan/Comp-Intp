package procedures;

/**
 * If type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class If extends Statement
{
    private Expression condition;
    private Statement then;
    private Statement _else;

    /**
     * If type node constructor
     * @param condition condition
     * @param then then
     */
    public If(Expression condition, Statement then)
    {
        this.condition = condition;
        this.then = then;
        this._else = null;
    }

    /**
     * If type node constructor
     * @param condition condition
     * @param then then
     * @param _else _else
     */
    public If(Expression condition, Statement then, Statement _else)
    {
        this.condition = condition;
        this.then = then;
        this._else = _else;
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
     * gets then
     * @return then as Statement
     */
    public Statement getThen()
    {
        return this.then;
    }

    /**
     * gets else
     * @return else as Statement
     */
    public Statement getElse()
    {
        return this._else;
    }

    /**
     * toString override
     * @return string representation of If type 
     */
    @Override
    public String toString()
    {
        String res = "IF " + condition + " THEN " + then;
        if (_else != null) res += " ELSE " + _else;
        return res;
    }
}
