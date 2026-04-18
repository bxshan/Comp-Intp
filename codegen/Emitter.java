package codegen;
import java.io.*;

public class Emitter
{
    static String push = "subu $sp $sp 4\nsw $v0 ($sp)\n";
    static String pop = "lw $t0 ($sp)\naddu $sp $sp 4\n";

    private PrintWriter out;

    private int ifcnt;

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
    }

    //prints one line of code to file (with non-labels indented)
    public void emit(String code)
    {
        // if (!code.endsWith(":"))
        //     code = "\t" + code;
        out.print(code);
    }

    public void push() {
        this.emit(push);
    }

    public void pop() {
        this.emit(pop);
    }

    public int nextLblId() {
        return this.ifcnt++;
    }

    //closes the file.  should be called after all calls to emit.
    public void close()
    {
        out.close();
    }
}
