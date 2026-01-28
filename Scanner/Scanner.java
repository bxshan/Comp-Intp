package scanner;
import java.io.*;
/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Boxuan Shan
 * @version 1.0
 *  
 * Usage:
 * Used to scan an input file and sequentially output tokens present in that file
 *
 * TODO:
 * support NESTED block comments /**\/
 * method descriptions
 * what should include in OPS?
 */
public class Scanner 
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    // excluding '/': is handled specially in nextToken() to check for comments
    // must exclude '$' and '^'
    private static final String OPS = "=+-*%()@;:<>#'\\\",{}[]&";

    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream) 
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString) 
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Returns next character in input, sets this.eof true if reach eof
     */
    private void getNextChar() 
    {
        try
        {
            int r = in.read();
            if (r == -1 || r == '.')  this.eof = true;
            else this.currentChar = (char) r;
        } 
        catch (Exception e) 
        {
            // catch IOException
            System.out.println("input oopsie!!");
            e.printStackTrace();
            return;
        }
        finally
        {
            // TODO
        }
    }

    /**
     * Scan to next char if currentChar matches expected
     * @param expected next character expected in input
     * @precondition currentChar is not last character, e.g. this.eof is false
     * @postcondition advances currentChar by one char, if expected char matches currentChar
     * @throws ScanErrorException if expected char does not match currentChar
     */
    private void eat(char expected) throws ScanErrorException 
    {
        if (expected == this.currentChar)
        {
            this.getNextChar();
        }
        else
        {
            throw new ScanErrorException("illegal char: exp. " + 
                    this.currentChar + 
                    " found " + 
                    expected);
        }
    }

    /**
     * Returns true if not at end of file
     * @return true if not at end of file
     */
    public boolean hasNext() 
    {
        return !this.eof;
    }

    /**
     * Checks if character is a digit
     * @param c character to check
     * @return true if c is a digit between 0 and 9, inclusive
     */
    public static boolean isDigit(char c) 
    {
        return c >= '0' && c <= '9';
    }


    /**
     * Checks if character is a letter
     * @param c character to check
     * @return true if c is an upper/lowercase letter
     */
    public static boolean isLetter(char c) 
    {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Checks if character is a type of space
     * @param c character to check
     * @return true if c is any type of whitespace: '\n', '\r', '\t' and ' '
     */
    // hopefully this casts lol
    public static boolean isSpace(char c) 
    {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    /**
     * Used for debug
     * @return the current character being read 
     */
    public char getCurrentChar() 
    {
        return this.currentChar;
    }

    /**
     * Checks if character is an identifier: letter or digit or '_'
     * @param c character to check
     * @return true if c is either a letter, digit, or an underline
     */
    private static boolean isIdentifier(char c) 
    {
        return isLetter(c) || isDigit(c) || c == '_';
    }

    /**
     * Checks if character is an operator; defined by this.OPS
     * @param c character to check
     * @return true if c is an operator, defined by the instance String OPS 
     */
    private static boolean isOperator(char c) 
    {
        return OPS.indexOf(c) > -1;
    }

    /**
     * Scans the next number that currentChar is on
     * @return the number that currentChar is currently on
     * @precondition isDigit(currentChar) is true, currentChar is on first digit of number
     * @postcondition currentChar is on the character right after the number
     * @throws ScanErrorException if currentChar does not represent a digit 
     */
    private String scanNumber() throws ScanErrorException 
    {
        String ret = "";
        if (!isDigit(currentChar)) 
        {
            throw new ScanErrorException("current char not digit: " + currentChar);
        }

        while(this.hasNext() && isDigit(this.currentChar)) 
        {
            ret += currentChar;
            eat(currentChar);
        }

        return ret;
    }

    /**
     * Scans the next identifier that currentChar is on
     * @return the identifier that currentChar is currently on
     * @precondition isIdentifier(currentChar) is true, currentChar is on first digit of identifier 
     * @postcondition currentChar is on the character right after the identifier 
     * @throws ScanErrorException if currentChar does not represent an identifier 
     */
    private String scanIdentifier() throws ScanErrorException 
    {
        String ret = "";
        if (!isIdentifier(currentChar)) 
        {
            throw new ScanErrorException("current char not identifier: " + currentChar);
        }

        while(this.hasNext() && isIdentifier(this.currentChar)) 
        {
            ret += currentChar;
            eat(currentChar);
        }

        return ret;
    }

    /**
     * Scans the next operator that currentChar is on
     * @return the operator that currentChar is currently on
     * @precondition isOperator(currentChar) is true, currentChar is on first digit of operator 
     * @postcondition currentChar is on the character right after the operator 
     * @throws ScanErrorException if currentChar does not represent an operator 
     */
    private String scanOperator() throws ScanErrorException 
    {
        String ret = "";
        if (!isOperator(currentChar)) 
        {
            throw new ScanErrorException("current char not operator: " + currentChar);
        }

        while(this.hasNext() && isOperator(this.currentChar)) 
        {
            ret += currentChar;
            eat(currentChar);
        }

        return ret;
    }

    /**
     * Scans the next token in input, throwing away spaces and comments
     * @return next token in input: number, identifier or operator
     * @throws ScanErrorException if expected does not match currentChar
     */
    public String nextToken() throws ScanErrorException 
    {
        // eat all spaces before checking for comment
        while(this.hasNext() && isSpace(currentChar)) eat(currentChar);

        // deal with comments: start at // and end with \n
        // start at /* and end with */
        if (this.hasNext() && currentChar == '/') 
        {
            eat(currentChar);

            if (this.hasNext()) 
            { 
                switch (currentChar) 
                {
                    case '/':
                        // begin single line comment
                        while(this.hasNext() && currentChar != '\n') 
                        {
                            eat(currentChar);
                        }
                        break;

                    case '*':
                        // begin block comment /* */
                        eat(currentChar); // eat * after opening /
                        while(this.hasNext()) 
                        {
                            if (currentChar == '*') 
                            {
                                eat(currentChar);
                                if (this.hasNext() && currentChar == '/')
                                {
                                    eat(currentChar); // eat the closing /
                                    break;
                                }
                            } 
                            else 
                            {
                                eat(currentChar);
                            }
                        }
                        break;

                    default:
                        return "/";
                }
            } 
            else return "/";
        }

        // maybe there are some spaces after comment
        while(this.hasNext() && isSpace(currentChar))
        {
            eat(currentChar);
        }

        if (!this.hasNext()) 
        {
            return "EOF";
        }

        if (isDigit(currentChar)) 
        {
            return scanNumber();
        }
        else if (isLetter(currentChar)) 
        {
            return scanIdentifier();
        } 
        else 
        {
            return scanOperator(); // op
        }
    }    
}
