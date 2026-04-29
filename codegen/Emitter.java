package codegen;
import java.io.*;
import java.util.*;

/**
 * Emitter.java
 * Object to write to file 
 *
 * @version 04242026
 * @author Anu Datar
 * @author Boxuan Shan
 */
public class Emitter
{
    private PrintWriter out;
    private int ifcnt;
    private ProcedureDeclaration currpd;
    private int badht;
    private ArrayList<String> lcls; // track local vars because hashmap is unordered


    /**
     * constructs emitter to write to new file with given name
     * @param outputFileName name of file to write to 
     */
    public Emitter(String outputFileName)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        ifcnt = 1;
        currpd = null;
        badht = 0;
    }

    /** 
     * writes one line of text to file
     * @param code line to write
     */
    public void emit(String code)
    {
        // if (!code.endsWith(":"))
        //     code = "\t" + code;
        out.print(code);
    }


    /**
     * default push register $v0
     */
    public void push() 
    {
        this.push("$v0");
    }

    /**
     * writes mips asm code to push some register to the stack
     * @param reg name of register to push
     */
    public void push(String reg) 
    {
        this.badht++;
        this.emit("subu $sp $sp 4\nsw " + reg + " ($sp)\n");
    }

    /**
     * default pop to register $t0
     */
    public void pop() 
    {
        this.pop("$t0");
    }

    /**
     * writes mips asm code to pop from stack to some register 
     * @param reg name of register to pop to 
     */
    public void pop(String reg) 
    {
        this.badht--;
        this.emit("lw " + reg + " ($sp)\naddu $sp $sp 4\n");
    }

    /**
     * add local variable to list of locals
     * @param name name of local var to add
     */
    public void addLcl(String name) 
    {
        lcls.add(name); 
    }

    /**
     * returns next id to use for label
     * @return integer of next id
     */
    public int nextLblId() 
    {
        return this.ifcnt++;
    }

    /**
     * inits the stack frame for a procedure to parse
     * @param pd procedure declaration currently parsing
     */
    public void setProcContext(ProcedureDeclaration pd) 
    {
        this.badht = 0;
        this.currpd = pd;
        this.lcls = new ArrayList<String>();
    }

    /**
     * ends the stack frame for a procedure parse
     */
    public void clearProcContext() 
    {
        this.currpd = null;
    }

    /**
     * determins if input is a local var
     * so if it is the name of the proc currently parsing,
     *    if it is one of the parameters, 
     * or if it is some other local var
     * @param name of var to check
     * @return if name is local var
     */
    public boolean isLocVar(String name) 
    {
        if (currpd == null) return false;
        return name.equals(currpd.getName()) ||
            currpd.getParams().contains(name) ||
            lcls.contains(name);
    }

    /**
     * returns offset from top of stack frame to the location of some local var
     * Stack format is top to bottom: local_n ... local_0, return, param_n ... param_0
     * @precondition locVarName is local var for proc currently compiling
     * @param locVarName name of locla var to find offset of 
     * @return int of offset
     */
    public int getOffset(String locVarName) 
    {
        var p = currpd.getParams();
        int nparams = p.size();
        int idx = p.indexOf(locVarName);

        if (locVarName.equals(currpd.getName()))
            // return var (proc name)
            return 4 * badht;
        if (lcls.contains(locVarName))
            // local var
            return 4 * (badht - 1 - lcls.indexOf(locVarName));
        // parameter
        return 4 * (badht + nparams - idx);
    }

    /**
     * closes file
     */
    public void close()
    {
        out.close();
    }
}
