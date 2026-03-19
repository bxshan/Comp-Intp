package environment;

import java.util.*;

/**
 * Environemnt class to manage variables in a scope
 *
 * @author Boxuan Shan
 * @version 03192026
 */
public class Environment 
{
    private HashMap<String, Object> var;

    /**
     * snvironment constructor, initializes var
     */
    public Environment() 
    {
        var = new HashMap<>();
    }

    /**
     * sets a variable in this env
     * @param v name of var to set
     * @param value value of var to set to
     */
    public void setVar(String v, Object value) 
    {
        var.put(v, value);
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
        throw new IllegalArgumentException("var " + var + " is not int\n");
    }

    /**
     * gets a variable in this env, if it is an object
     * @param v name of var to get
     * @return Obj value of var
     */
    public Object getObjVar(String v)
    { // for Object subtypes
        if (var.containsKey(v)) return var.get(v);
        throw new IllegalArgumentException("var " + var + "not found or is not Obj\n");
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
