package ast;

/**
 * RepeatUntil type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 03192026
 */
public class RepeatUntil extends Statement
{
    private Statement repeat;
    private Expression until;

    /**
     * RepeatUntil type node constructor
     * @param repeat repeat
     * @param until until
     */
    public RepeatUntil(Statement repeat, Expression until)
    {
        this.repeat = repeat;
        this.until = until;
    }

    /**
     * gets repeat
     * @return repeat as Statement
     */
    public Statement getRepeat()
    {
        return this.repeat;
    }

    /**
     * gets until
     * @return until as Expression
     */
    public Expression getUntil()
    {
        return this.until;
    }

    /**
     * toString override
     * @return string representation of RepeatUntil type 
     */
    @Override
    public String toString()
    {
        String res = "REPEAT " + repeat + " UNTIL " + until;
        return res;
    }
}
