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
    static void test(
            scanner.Scanner s, 
            codegen.Parser p, 
            codegen.Evaluator ev,
            codegen.Emitter em 
    ) throws Throwable
    {
        Program pg = p.parseProgram();
        ev.compile(pg, em);
    }

    public static void main(String[] args) throws Throwable {
        String tcdir = "/Users/box/Desktop/src/HarkerCompIntp/codegen/tc/"; 
        File folder = new File(tcdir);
        File[] files = folder.listFiles((d, name) -> name.endsWith(".txt"));
        Arrays.sort(files);
        for (File f : files) {
            String name = f.getName();
            String outName = name.replace(".txt", ".s");
            try {
                FileInputStream fis = new FileInputStream(f);
                scanner.Scanner sc = new scanner.Scanner(fis);
                codegen.Parser parser = new codegen.Parser(sc);
                codegen.Evaluator ev = new codegen.Evaluator();
                codegen.Emitter em = new codegen.Emitter(tcdir + outName);
                test(sc, parser, ev, em);
            } catch (Throwable t) {
                System.out.println("not compiled " + t.getMessage());
                t.printStackTrace(System.out);
            }
        }

        // String dir = 
        //     "/Users/box/Desktop/src/HarkerCompIntp/codegen/tst1.txt";
        // FileInputStream fis = new FileInputStream(dir);
        // scanner.Scanner scanner = new scanner.Scanner(fis);
        // codegen.Parser parser = new codegen.Parser(scanner);
        // codegen.Evaluator ev = new codegen.Evaluator();
        // codegen.Emitter em = new codegen.Emitter("foo.s");
        // test(scanner, parser, ev, em);
    }
}
