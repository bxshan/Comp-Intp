package codegen;
import java.util.*;

/**
 * Program class
 *
 * @author Boxuan Shan
 * @version 04172026
 */
public class Program
{
    private HashMap<String, Expression> vars;
    private ArrayList<Statement> stmts;

    /**
     * Program constructor
     * @param vars list of global variable names
     * @param stmts list of statements in the program
     */
    public Program(HashMap<String, Expression> vars, ArrayList<Statement> stmts)
    {
        this.vars = vars;
        this.stmts = stmts;
    }

    public HashMap<String, Expression> getVars() { 
        return vars; 
    }

    public ArrayList<Statement> getStmts() { 
        return stmts; 
    }
}
