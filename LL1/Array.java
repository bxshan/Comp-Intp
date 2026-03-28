package LL1;

/**
 * Array type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Array extends Expression 
{
    private int start;
    private int end;

    /**
     * Array type node constructor
     * @param start start idx
     * @param end end idx
     */
    public Array(int start, int end) 
    {
        this.start = start;
        this.end = end;
    }

    /**
     * gets start idx
     * @return start idx as int
     */
    public int getStart() 
    {
        return this.start;
    }

    /**
     * gets end idx
     * @return end idx as int
     */
    public int getEnd() 
    {
        return this.end;
    }

    /**
     * toString override
     * @return string representation of Array type 
     */
    @Override
    public String toString() 
    {
        return "array[" + start + ".." + end + "]";
    }
}
