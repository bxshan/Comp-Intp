import java.io.*;
/**
 * A tester for the Scannerabb class. 
 * @author Boxuan Shan
 * @version 1.0
 *  
 * Usage:
 * Test different methods in the Scannerabb class
 */
public class Tester
{
    /**
     * Prints out each token from the input path until EOF. 
     * @param s Scannerabb object to test 
     */
    static void test1(Scannerabb s) 
    {
        String t = "";
        try 
        {
            while(!s.zzAtEOF)
            // while(s.hasNext())
            // while(true)
            {
                t = s.nextToken();
                System.out.println(t);
            }

            // t = s.nextToken();
            // System.out.println(t);
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
        String dir = "/Users/box/Desktop/Desktop - box mac/src/harkerCompIntp/jflex/tst1.txt";
        java.io.Reader o = new java.io.FileReader(dir);
        Scannerabb s = new Scannerabb(o);

        test1(s);
    }
}
