package scanner;
import java.io.*;
/**
 * A tester for the Scanner class. 
 * @author Boxuan Shan
 * @version 1.0
 *  
 * Usage:
 * Test different methods in the Scanner class
 */
public class Tester
{
    /**
     * Prints out each token from the input path until EOF. 
     * @param s Scanner object to test 
     */
    static void test1(Scanner s) 
    {
        String t = "";
        try 
        {
            // while(!t.equals("EOF"))
            while(s.hasNext())
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
     * tests some static methods on scanner.
     * everything should print true.
     */
    static void test2() 
    {                                            
        System.out.println(Scanner.isSpace('\n'));
        System.out.println(Scanner.isSpace('\t'));
        System.out.println(Scanner.isSpace('\r'));
        System.out.println(Scanner.isDigit('6'));
        System.out.println(Scanner.isDigit('1'));
        System.out.println(Scanner.isDigit('0'));
        System.out.println(Scanner.isDigit('9'));
        System.out.println(Scanner.isLetter('A'));
        System.out.println(Scanner.isLetter('c'));
        System.out.println(Scanner.isLetter('d'));
        System.out.println(Scanner.isLetter('L'));
        System.out.println(Scanner.isLetter('Z'));
    }

    /**
     * Main method
     * @param args cmd line args
     * @throws FileNotFoundException if specified file in dir does not exist
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        // String dir = "/Users/box/Desktop/Desktop - box mac/src/harkerCompIntp/Scanner/tst5.txt";
        String dir = "x = x + 3 / 5 - y";
        // FileInputStream fis = new FileInputStream(dir);
        // Scanner s = new Scanner(fis);
        Scanner s = new Scanner(dir);

        test1(s);
    }
}
