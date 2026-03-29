package procedures;

/**
 * _String type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class _String extends Expression
{
    private String str;

    /**
     * _String type node constructor
     * @param str str
     */
    public _String(String str)
    {
        this.str = str;
    }

    /**
     * gets val
     * @return val as String
     */
    public String getVal()
    {
        return this.str;
    }

    /**
     * toString override
     * @return string representation of _String type 
     */
    @Override
    public String toString()
    {
        return this.str;
    }
}
