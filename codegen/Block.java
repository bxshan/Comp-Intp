package codegen;

import java.util.ArrayList;
import java.util.List;

/**
 * Block type node
 * extends abstract Statement
 *
 * @author Boxuan Shan
 * @version 02242025
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Block type node constructor
     * @param stmts stmts
     */
    public Block(ArrayList<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * gets stmts
     * @return stmts as List<Statement>
     */
    public List<Statement> getStmts()
    {
        return this.stmts;
    }

    /**
     * toString override
     * @return string representation of Block type 
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN\n");
        for (Statement stmt : stmts)
        {
            sb.append("  " + stmt.toString() + "\n");
        }
        sb.append("END;");
        return sb.toString();
    }
}
