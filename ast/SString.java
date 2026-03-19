package ast;

/**
 * SString type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 03192026
 */
public class SString extends Expression
{
    private String str;

    /**
     * SString type node constructor
     * @param str str
     */
    public SString(String str)
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
     * @return string representation of SString type 
     */
    @Override
    public String toString()
    {
        return this.str;
    }
}
