package procedures;

import scanner.*;
import java.util.*;
import java.io.*;

/**
 * Tester for procedures/Parser.java
 *
 * @author Boxuan Shan
 * @version 03242025
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
    static void test(
            scanner.Scanner s, 
            procedures.Parser p, 
            procedures.Evaluator ev
    ) throws Throwable
    {
        System.out.println("=====================================");

        try
        {
            // Statement procedures = p.parseStatement();
            // ev.exec(procedures, p.getEnv());

            // System.out.println("\nAST gen: \n" + procedures.toString());

            System.out.println("out:");
            Program pg = p.parseProgram();
            ev.exec(pg, p.getEnv());
            
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
            System.out.println("check procedures.Tester\n\t");
            e.printStackTrace();
        }
    }

    /**
     * main function
     * iterates and tests tc in order, 
     * also tests impl. of specific algs
     * @param args cli args
     * @throws Throwable for break and continue
     */
    public static void main(String[] args) throws Throwable
    {
        // int tcR = 17;
        // for(int tc = 0; tc <= tcR; tc++)
        // {
        //     if (tc==4) continue; // skip the READLN case so to not interrupt testing
        //     try
        //     {
        //         System.out.println("tc " + tc + ":");
        //         String dir = 
        //             "/Users/box/Desktop/src/HarkerCompIntp/procedures/tst"+tc+".txt";
        //         FileInputStream fis = new FileInputStream(dir);
        //         scanner.Scanner scanner = new scanner.Scanner(fis);
        //         procedures.Parser parser = new procedures.Parser(scanner);
        //         procedures.Evaluator ev = new procedures.Evaluator();
        //         test(scanner, parser, ev);
        //         System.out.println("=====================================");
        //         System.out.println("=====================================\n\n");
        //     }
        //     catch (Exception e)
        //     {
        //         // e.printStackTrace();
        //         System.out.println("skipping tc " + tc + "...\n");
        //         continue;
        //     }
        // }

        String dir = 
            "/Users/box/Desktop/src/HarkerCompIntp/procedures/tc/sort.txt";
        FileInputStream fis = new FileInputStream(dir);
        scanner.Scanner scanner = new scanner.Scanner(fis);
        procedures.Parser parser = new procedures.Parser(scanner);
        procedures.Evaluator ev = new procedures.Evaluator();
        test(scanner, parser, ev);
        System.out.println("=====================================");
        System.out.println("=====================================\n\n");
    }
}
