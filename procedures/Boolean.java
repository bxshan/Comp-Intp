package procedures;

/**
 * Boolean type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Boolean extends Expression
{
    private boolean bool;

    /**
     * Boolean type node constructor
     * @param bool bool
     */
    public Boolean(boolean bool)
    {
        this.bool = bool;
    }

    /**
     * gets val
     * @return val as boolean
     */
    public boolean getVal()
    {
        return this.bool;
    }

    /**
     * toString override
     * @return string representation of Boolean type 
     */
    @Override
    public String toString()
    {
        return bool ? "TRUE" : "FALSE";
    }
}
