package procedures;

/**
 * ArrayElement type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class ArrayElement extends Expression
{
    private String name;
    private Expression idx;

    /**
     * ArrayElement type node constructor
     * @param name name of Array
     * @param idx idx of Array
     */
    public ArrayElement(String name, Expression idx)
    {
        this.name = name;
        this.idx = idx;
    }

    /**
     * gets name
     * @return name of Array
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * gets idx of ArrayElement
     * @return idx as Expression
     */
    public Expression getIdx()
    {
        return this.idx;
    }

    /**
     * toString override
     * @return string representation of ArrayElement type 
     */
    @Override
    public String toString()
    {
        return name + "[" + idx + "]";
    }
}

