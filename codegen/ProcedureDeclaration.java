package codegen;
import java.util.*;

/**
 * ProcedureDeclaration type node
 * extends abstract Statement 
 *
 * @author Boxuan Shan
 * @version 03282025
 */
public class ProcedureDeclaration extends Statement 
{
    private String name;
    private HashMap<String, Expression> vars;
    private ArrayList<String> params;
    private Statement stmt;

    /**
     * ProcedureDeclaration type node constructor
     * @param name name of proc
     * @param vars hashmap representing local vars present in proc
     * @param params list of parameters
     * @param stmt statement corresponding to procedure
     */
    public ProcedureDeclaration(String name, 
            HashMap<String, Expression> vars,
            ArrayList<String> params, 
            Statement stmt) 
    {
        this.name = name;
        this.vars = vars;
        this.params = params;
        this.stmt = stmt;
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
     * gets params 
     * @return list of params 
     */
    public ArrayList<String> getParams() 
    {
        return this.params;
    }

    /**
     * gets stmt
     * @return stmt of proc
     */
    public Statement getStmt() 
    {
        return this.stmt;
    }

    /**
     * gets local vars hashmap
     * @return hashmap of local vars
     */
    public HashMap<String, Expression> getVars() 
    {
        return this.vars;
    }
}
