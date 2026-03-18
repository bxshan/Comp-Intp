package ast;

import scanner.*;
import java.util.*;
import java.io.*;

public class Tester {
    static void test(scanner.Scanner s, ast.Parser p, ast.Evaluator ev) throws Throwable {
        System.out.println("=====================================");

        try {
            // Statement ast = p.parseStatement();
            // ev.exec(ast, p.getEnv());

            // System.out.println("\nAST gen: \n" + ast.toString());

            System.out.println("out:");
            p.parse();
            
            System.out.println("\n=====================================");
            System.out.println("fin vars: ");
            
            HashMap<String, Object> vm = p.getVarMap();
            for (String v : vm.keySet()) {
                System.out.println(v + "\t=\t" + vm.get(v));
            }
        } catch (Exception e) {
            System.out.println("check ast.Tester\n\t");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        for(int tc = 0; tc <= 13; tc++) {
            if (tc==4) continue;
            // if (tc==8) continue;
            try {
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
            } catch (Exception e) {
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
