package procedures;

/**
 * Variable type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Variable type node constructor
     * @param name name
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * gets name
     * @return name as String
     */
    public String getName()
    {
        return name;
    }

    /**
     * toString override
     * @return string representation of Variable type 
     */
    @Override
    public String toString()
    {
        return name;
    }
}
