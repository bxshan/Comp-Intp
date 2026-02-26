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

    // Claude Opus v
    // ------------------------------------------------------------------ helpers
    static int passed = 0, failed = 0;

    /** Run src through the parser and return everything printed to stdout. */
    static String run(String src) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(baos));
        try {
            Scanner sc = new Scanner(src + " ");
            Parser p  = new Parser(sc);
            while (!p.ctok.equals("EOF")) p.parseStatement();
        } finally {
            System.setOut(old);
        }
        return baos.toString().trim();
    }

    static void check(String label, String expected, String actual) {
        if (expected.equals(actual)) {
            System.out.println("[PASS] " + label);
            passed++;
        } else {
            System.out.println("[FAIL] " + label
                    + " | expected: \"" + expected
                    + "\" | got: \"" + actual + "\"");
            failed++;
        }
    }

    static void chk(String label, String expected, String src) {
        try { check(label, expected, run(src)); }
        catch (Exception e) {
            System.out.println("[FAIL] " + label + " | threw: " + e);
            failed++;
        }
    }

    // ------------------------------------------------------------------ suite
    static void testAll() {
        System.out.println("=== integer literals & arithmetic ===");
        chk("literal",            "5",     "WRITELN(5);");
        chk("addition",           "7",     "WRITELN(3 + 4);");
        chk("subtraction",        "6",     "WRITELN(10 - 4);");
        chk("multiplication",     "12",    "WRITELN(3 * 4);");
        chk("division",           "5",     "WRITELN(15 / 3);");
        chk("mod",                "2",     "WRITELN(17 mod 3);");
        chk("unary minus",        "-7",    "WRITELN(-7);");
        chk("precedence */+",     "14",    "WRITELN(2 + 3 * 4);");
        chk("parens override",    "20",    "WRITELN((2 + 3) * 4);");
        chk("double neg",         "5",     "WRITELN(-(-5));");
        chk("nested arith",       "1",     "WRITELN((10 - 4) / 3 * 2 mod 4 + 1);");

        System.out.println("\n=== integer variables ===");
        chk("int assign+read",    "5",     "x := 5; WRITELN(x);");
        chk("int var in expr",    "7",     "x := 3; y := 4; WRITELN(x + y);");
        chk("int var chain",      "20",    "x := 10; y := x * 2; WRITELN(y);");
        chk("int var unary",      "-3",    "x := 3; WRITELN(-x);");

        System.out.println("\n=== boolean literals ===");
        chk("TRUE",               "true",  "WRITELN(TRUE);");
        chk("FALSE",              "false", "WRITELN(FALSE);");

        System.out.println("\n=== boolean operators ===");
        chk("AND tt",             "true",  "WRITELN(TRUE AND TRUE);");
        chk("AND tf",             "false", "WRITELN(TRUE AND FALSE);");
        chk("OR ft",              "true",  "WRITELN(FALSE OR TRUE);");
        chk("OR ff",              "false", "WRITELN(FALSE OR FALSE);");
        chk("NOT TRUE",           "false", "WRITELN(NOT TRUE);");
        chk("NOT FALSE",          "true",  "WRITELN(NOT FALSE);");
        chk("AND prec over OR",   "true",  "WRITELN(TRUE OR FALSE AND FALSE);");
        chk("parens override AND","false", "x := TRUE OR FALSE; WRITELN(x AND FALSE);");
        chk("NOT NOT",            "true",  "WRITELN(NOT NOT TRUE);");

        System.out.println("\n=== boolean variables ===");
        chk("bool assign+read",   "true",  "x := TRUE; WRITELN(x);");
        chk("bool var in OR",     "true",  "x := FALSE; y := TRUE; WRITELN(x OR y);");
        chk("bool var NOT",       "false", "x := TRUE; WRITELN(NOT x);");
        chk("bool var AND",       "false", "x := TRUE; y := FALSE; WRITELN(x AND y);");

        System.out.println("\n=== relational operators (int) ===");
        chk("= true",             "true",  "WRITELN(1 = 1);");
        chk("= false",            "false", "WRITELN(1 = 2);");
        chk("<> true",            "true",  "WRITELN(1 <> 2);");
        chk("<> false",           "false", "WRITELN(2 <> 2);");
        chk("< true",             "true",  "WRITELN(1 < 2);");
        chk("< false",            "false", "WRITELN(2 < 1);");
        chk("> true",             "true",  "WRITELN(2 > 1);");
        chk("> false",            "false", "WRITELN(1 > 2);");
        chk("<= eq",              "true",  "WRITELN(2 <= 2);");
        chk("<= lt",              "true",  "WRITELN(1 <= 2);");
        chk("<= false",           "false", "WRITELN(3 <= 2);");
        chk(">= eq",              "true",  "WRITELN(2 >= 2);");
        chk(">= gt",              "true",  "WRITELN(3 >= 2);");
        chk(">= false",           "false", "WRITELN(1 >= 2);");
        chk("relop in assign",    "false", "x := 2 >= 3; WRITELN(x);");
        chk("relop expr lhs",     "true",  "x := 3; WRITELN(x > 1);");

        System.out.println("\n=== string literals & variables ===");
        chk("str assign+read",    "hello", "x := \"hello\"; WRITELN(x);");
        chk("str literal WRITELN","world", "WRITELN(\"world\");");
        chk("str concat",         "foobar","x := \"foo\" + \"bar\"; WRITELN(x);");
        chk("str concat vars",    "ab",    "x := \"a\"; y := \"b\"; WRITELN(x + y);");

        System.out.println("\n=== string relational operators ===");
        chk("str = true",         "true",  "WRITELN(\"abc\" = \"abc\");");
        chk("str = false",        "false", "WRITELN(\"abc\" = \"abd\");");
        chk("str <> true",        "true",  "WRITELN(\"abc\" <> \"abd\");");
        chk("str < true",         "true",  "WRITELN(\"abc\" < \"abd\");");
        chk("str > true",         "true",  "WRITELN(\"xyz\" > \"abc\");");
        chk("str <= eq",          "true",  "WRITELN(\"abc\" <= \"abc\");");
        chk("str >= gt",          "true",  "WRITELN(\"b\" >= \"a\");");

        System.out.println("\n=== BEGIN...END blocks ===");
        chk("simple block",       "5",     "BEGIN x := 5; WRITELN(x); END;");
        chk("nested block",       "3",     "BEGIN BEGIN x := 3; END; WRITELN(x); END;");
        chk("multi-stmt block",   "3\n4",  "BEGIN x := 3; y := 4; WRITELN(x); WRITELN(y); END;");

        System.out.println("\n=== comments ===");
        chk("inline comment",     "5",     "(* hi *) WRITELN(5);");
        chk("comment in block",   "true",  "BEGIN (* skip *) WRITELN(TRUE); END;");
        chk("comment w/ ops",     "7",     "(* 1 != 2 *) WRITELN(7);");

        System.out.println("\n=== multiple WRITELN ===");
        chk("multi WRITELN",      "1\n2\n3","WRITELN(1); WRITELN(2); WRITELN(3);");

        System.out.println("\n=== summary ===");
        System.out.println("passed: " + passed + " / " + (passed + failed));
    }
    // Claude Opus ^

    /**
     * Main method
     * @param args cmd line args
     * @throws FileNotFoundException if specified file in dir does not exist
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        // testAll();

        try {
            String dir = "/Users/box/Desktop/Desktop - box mac/src/harkerCompIntp/Parser/tst9.txt";
            FileInputStream fis = new FileInputStream(dir);
            Scanner s = new Scanner(fis);
            Parser p = new Parser(s);
            test1(p);
        } catch (Exception e) {
            System.out.println("err at parser/Tester.java 48:" + e);
        }
    }
}

