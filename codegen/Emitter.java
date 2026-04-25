package codegen;
import java.io.*;
import java.util.*;

public class Emitter
{
    private PrintWriter out;

    private int ifcnt;
    private ProcedureDeclaration currpd;
    private int badht;
    private ArrayList<String> lcls; // track local vars because hashmap is unordered


    //creates an emitter for writing to a new file with given name
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

    //prints one line of code to file 
    public void emit(String code)
    {
        // if (!code.endsWith(":"))
        //     code = "\t" + code;
        out.print(code);
    }

    public void push() {
        this.push("$v0");
    }

    public void push(String reg) {
        this.badht++;
        this.emit("subu $sp $sp 4\nsw " + reg + " ($sp)\n");
    }

    public void pop() {
        this.pop("$t0");
    }

    public void pop(String reg) {
        this.badht--;
        this.emit("lw " + reg + " ($sp)\naddu $sp $sp 4\n");
    }

    public void addLcl(String name) {
        lcls.add(name); 
    }

    public int nextLblId() {
        return this.ifcnt++;
    }

    public void setProcContext(ProcedureDeclaration pd) {
        this.badht = 0;
        this.currpd = pd;
        this.lcls = new ArrayList<String>();
    }

    public void clearProcContext() {
        this.currpd = null;
    }

    public boolean isLocVar(String name) {
        if (currpd == null) return false;
        return name.equals(currpd.getName()) ||
            currpd.getParams().contains(name) ||
            lcls.contains(name);
    }

    /**
     * Stack format is top to bottom: local_n ... local_0, return, param_n ... param_0
     * @precondition locVarName is local var for proc currently compiling
     */
    public int getOffset(String locVarName) {
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

    //closes the file.  should be called after all calls to emit.
    public void close()
    {
        out.close();
    }
}
