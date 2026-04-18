package codegen;

import scanner.*;
import java.util.*;
import java.io.*;

/**
 * Tester for codegen/Parser.java
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
            codegen.Parser p, 
            codegen.Evaluator ev,
            codegen.Emitter e
    ) throws Throwable
    {
        Program pg = p.parseProgram();
        ev.compile(pg, p.getEnv(), e); 
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
        String dir = 
            "/Users/box/Desktop/src/HarkerCompIntp/codegen/tst1.txt";
        FileInputStream fis = new FileInputStream(dir);
        scanner.Scanner scanner = new scanner.Scanner(fis);
        codegen.Parser parser = new codegen.Parser(scanner);
        codegen.Evaluator ev = new codegen.Evaluator();
        codegen.Emitter e = new codegen.Emitter("foo.s");
        test(scanner, parser, ev, e);
        System.out.println("=====================================");
        System.out.println("=====================================\n\n");
    }
}
