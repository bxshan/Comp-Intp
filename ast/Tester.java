package ast;

import scanner.*;
import java.util.*;
import java.io.*;

/**
 * Tester for ast/parser.java
 *
 * @author Boxuan Shan
 * @version 03192024
 */
public class Tester
{
    /**
     * tests parser on one test case txt file
     * @param s Scanner to test
     * @param p Parse to test
     * @param ev Evaluator to test
     * @throws Throwable for break and continue
     */
    static void test(scanner.Scanner s, ast.Parser p, ast.Evaluator ev) throws Throwable
    {
        System.out.println("=====================================");

        try
        {
            // Statement ast = p.parseStatement();
            // ev.exec(ast, p.getEnv());

            // System.out.println("\nAST gen: \n" + ast.toString());

            System.out.println("out:");
            p.parse();
            
            System.out.println("\n=====================================");
            System.out.println("fin vars: ");
            
            HashMap<String, Object> vm = p.getVarMap();
            for (String v : vm.keySet())
            {
                System.out.println(v + "\t=\t" + vm.get(v));
            }
        }
        catch (Exception e)
        {
            System.out.println("check ast.Tester\n\t");
            e.printStackTrace();
        }
    }

    /**
     * main function
     * iterates and tests tc 0 to 13 in order, 
     * then tests impl. of sieve of eratosthenes
     * @param args cli args
     * @throws Throwable for break and continue
     */
    public static void main(String[] args) throws Throwable
    {
        int tcR = 13;
        for(int tc = 0; tc <= tcR; tc++)
        {
            if (tc==4) continue; // skip the READLN case so to not interrupt testing
            try
            {
                System.out.println("tc " + tc + ":");
                String dir = 
                    "/Users/box/Desktop/src/HarkerCompIntp/ast/tst"+tc+".txt";
                FileInputStream fis = new FileInputStream(dir);
                scanner.Scanner scanner = new scanner.Scanner(fis);
                ast.Parser parser = new ast.Parser(scanner);
                ast.Evaluator ev = new ast.Evaluator();
                test(scanner, parser, ev);
                System.out.println("=====================================");
                System.out.println("=====================================\n\n");
            }
            catch (Exception e)
            {
                System.out.println("skipping tc " + tc + "...\n");
                continue;
            }
        }

        System.out.println("sieve of eratosthenes: ");
        String dir = 
            "/Users/box/Desktop/src/HarkerCompIntp/ast/sieve.txt";
        FileInputStream fis = new FileInputStream(dir);
        scanner.Scanner scanner = new scanner.Scanner(fis);
        ast.Parser parser = new ast.Parser(scanner);
        ast.Evaluator ev = new ast.Evaluator();
        test(scanner, parser, ev);
        System.out.println("=====================================");
        System.out.println("=====================================\n\n");
    }
}
