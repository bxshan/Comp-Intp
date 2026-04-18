package environment;
import codegen.*;

import java.util.*;

/**
 * Environment class to manage variables in some scope
 *
 * @author Boxuan Shan
 * @version 03242026
 */
public class Environment 
{
    private HashMap<String, Object> var;
    private HashMap<String, ProcedureDeclaration> proc;
    private Environment parent;

    /**
     * default Environment constructor
     */
    public Environment() 
    {
        this(null);
    }

    /**
     * Environment constructor with parent scope
     * @param parent parent environment
     */
    public Environment(Environment parent)
    {
        var = new HashMap<String, Object>();
        proc = new HashMap<String, ProcedureDeclaration>();
        this.parent = parent;
    }

    /**
     * sets a variable in this env or parent env
     * @param v name of var to set
     * @param value value of var to set to
     */
    public void setVar(String v, Object value) 
    {
        if (var.containsKey(v) || parent == null) var.put(v, value); // prioritize local change before parent
        else parent.setVar(v, value);
    }

    /**
     * sets a variable specifically in this local scope
     * @param v name of var
     * @param value value
     */
    public void setLocalVar(String v, Object value) 
    {
        var.put(v, value); 
    }

    
    public void setGlobalVar(String v, Object value) {
        if (parent != null) parent.setVar(v, value);
        else var.put(v, value);
    }

    /**
     * sets a procedure in this env
     * @param v name of proc to set
     * @param stmt body of proc to set to
     */
    public void setProc(String v, ProcedureDeclaration stmt) 
    {
        if (parent != null) parent.setProc(v, stmt);
        else proc.put(v, stmt); 
    }

    /**
     * gets a procedure in this env
     * @param v name of proc to get
     * @return Statement representing proc body
     */
    public ProcedureDeclaration getProc(String v) {
        if (parent != null) return parent.getProc(v);
        else if (proc.containsKey(v)) return proc.get(v);
        else throw new IllegalArgumentException("proc " + v + " is not declared\n");
    }

    /**
     * gets a variable in this env, if it is an int 
     * @param v name of var to get
     * @return int value of var
     */
    public int getVar(String v)
    { // for int only
        Object value = getObjVar(v);
        if (value instanceof Integer) return (Integer) value;
        throw new IllegalArgumentException("var " + v + " is not int\n");
    }

    /**
     * gets a variable in this env, if it is an object
     * @param v name of var to get
     * @return Obj value of var
     */
    public Object getObjVar(String v)
    { // for Object subtypes
        if (var.containsKey(v)) return var.get(v);
        if (parent != null) return parent.getObjVar(v);
        throw new IllegalArgumentException("var " + v + " not found\n");
    }

    /**
     * gets the entire HashMap representing vars
     * @return HashMap of vars
     */
    public HashMap<String, Object> getVars() 
    {
        return var;
    }
}
