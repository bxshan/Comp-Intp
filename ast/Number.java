package ast;

/**
 * Number type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 03192026
 */
public class Number extends Expression
{
    private int num;

    /**
     * Number type node constructor
     * @param num num
     */
    public Number(int num)
    {
        this.num = num;
    }

    /**
     * gets val
     * @return val as int
     */
    public int getVal()
    {
        return this.num;
    }

    /**
     * toString override
     * @return string representation of Number type 
     */
    @Override
    public String toString()
    {
        return Integer.toString(num);
    }
}
