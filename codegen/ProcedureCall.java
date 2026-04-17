package procedures;
import java.util.*;

/**
 * ProcedureCall type node
 * extends abstract Expression
 *
 * @author Boxuan Shan
 * @version 03282025
 */
public class ProcedureCall extends Expression 
{
    private String name;
    private ArrayList<Expression> args;

    /**
     * ProcedureCall node constructor
     * @param name name of procedure
     * @param args list of args
     */
    public ProcedureCall(String name, ArrayList<Expression> args) 
    {
        this.name = name;
        this.args = args;
    }

    /**
     * gets name
     * @return name of proc
     */
    public String getName() 
    {
        return this.name;
    }

    /**
     * gets args
     * @return list of args
     */
    public ArrayList<Expression> getArgs() 
    {
        return this.args;
    }
}
