import java.io.*;
/**
 * A tester for the Scanner class. 
 * @author Boxuan Shan
 * @date 01 23 2026
 *  
 * Usage:
 * Test different methods in the Scanner class
 */
public class Tester {
  /**
   * Prints out each token from the input path until EOF. 
   * @param s Scanner object to test 
   */
  static void test1(Scanner s) {
    String t = "";
    try {
      while(t != "EOF") {
        t = s.nextToken();
        System.out.println(t);
      }
    } catch (Exception e) {
      System.out.println("!!!uh oh: " + e);
    }
  }

  /**
   * tests some static methods on scanner.
   * everything should return true.
   */
  static void test2() {                                            
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
   */
  public static void main(String[] args) throws FileNotFoundException {
    String dir = "/Users/box/Desktop/Desktop - box mac/src/harkerCompIntp/Scanner/tst5.txt";
    FileInputStream fis = new FileInputStream(dir);
    Scanner s = new Scanner(fis);

    test1(s);
  }
}
