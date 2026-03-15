package environment;

import java.util.*;

public class Environment {
    private HashMap<String, Object> var;

    public Environment() {
        var = new HashMap<>();
    }

    public void setVar(String v, Object value) {
        var.put(v, value);
    }

    public int getVar(String v) { // for int only
        Object value = getObjVar(v);
        if (value instanceof Integer) return (Integer) value;
        throw new IllegalArgumentException("var " + var + " is not int\n");
    }

    public Object getObjVar(String v) { // for Object subtypes
        if (var.containsKey(v)) return var.get(v);
        throw new IllegalArgumentException("var " + var + "not found or is not Obj\n");
    }

    public HashMap<String, Object> getVars() {
        return var;
    }
}
