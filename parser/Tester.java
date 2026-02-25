package parser;
import java.io.*;
import scanner.*;
/**
 * A tester for the Parser class. 
 * @author Boxuan Shan
 * @version 1.0
 *  
 * Usage:
 * Test different methods in the Parser class
 */
public class Tester
{
    /**
     * Prints out each token from the input path until EOF. 
     * @param s Scanner object to test 
     */
    static void test1(Parser p) 
    {
        try 
        {
            p.parseStatement();
        }
        catch (Exception e) 
        {
            System.out.println("!!!uh oh: " + e);
            e.printStackTrace();
        }
        finally
        {
        }
    }

    /**
     * Main method
     * @param args cmd line args
     * @throws FileNotFoundException if specified file in dir does not exist
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        try {
            String dir = "/Users/box/Desktop/Desktop - box mac/src/harkerCompIntp/Parser/tst6.txt";
            FileInputStream fis = new FileInputStream(dir);
            Scanner s = new Scanner(fis);
            Parser p = new Parser(s);
            test1(p);
        } catch (Exception e) {
            System.out.println("err at parser/Tester.java 48:" + e);
        }
    }
}

