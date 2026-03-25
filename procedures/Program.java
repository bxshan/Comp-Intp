package procedures;

import java.util.ArrayList;

/**
 * Program class 
 *
 * @author Boxuan Shan
 * @version 03242025
 */
public class Program 
{
    private ArrayList<Statement> stmts;

    /**
     * Program constructor
     * @param stmts list of statements in the program
     */
    public Program(ArrayList<Statement> stmts) 
    {
        this.stmts = stmts;
    }

    /**
     * gets the list of statements
     * @return ArrayList of statements
     */
    public ArrayList<Statement> getStmts() 
    {
        return stmts;
    }
}
