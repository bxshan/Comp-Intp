package parser;
import scanner.*;
import java.util.*;

public class Parser {
    scanner.Scanner sc;
    String ctok;
    HashMap<String, Integer> vint;
    HashMap<String, Boolean> vbool;

    static final Set<String> KEYWORDS = Set.of("BEGIN", "END", "WRITELN", "READLN", "EOF", "TRUE", "FALSE");

    public Parser(scanner.Scanner sc) throws ScanErrorException {
        this.sc = sc;
        this.ctok = this.sc.nextToken();
        this.vint = new HashMap<String, Integer>();
        this.vbool = new HashMap<String, Boolean>();
    }

    private void eat(String e) throws ScanErrorException {
        if (e.equals(ctok)) {ctok = sc.nextToken(); /*System.out.println("!EAT" + ctok);*/}
        else throw new IllegalArgumentException("expected \"" + e + "\", actually ctok \"" + ctok + "\"");
    }

    /**
     * @precondition current tok is an integer
     * @postcondition integer token is eaten
     * @return value of eaten int
     */
    private int parseNumber() throws ScanErrorException {
        int n = -Integer.MAX_VALUE;
        try {
            n = Integer.parseInt(ctok);
        } catch (NumberFormatException e) {
            System.out.println("num wrong format check parser parseNumber(): " + e);
        }
        eat(ctok);
        return n;
    }

    public void parseStatement() throws ScanErrorException {
        // System.out.println("stmt" + ctok);
        while (ctok.equals("WRITELN")) {
            eat("WRITELN");
            eat("(");
            System.out.println(parseExpression());
            eat(")");
            eat(";");
        }
        if (ctok.equals("READLN")) {
            java.util.Scanner scn = new java.util.Scanner(System.in);
            eat("READLN");
            eat("(");
            System.out.print("WRITE TO " + ctok + ":\t");
            String in = scn.nextLine();
            if (in.matches("\\d+")) {
                vint.put(ctok, Integer.parseInt(in));
            } else if (in.equals("TRUE") || in.equals("FALSE")) {
                vbool.put(ctok, (in.equals("TRUE") ? true : false));
            }
            eat(ctok);
            eat(")");
            eat(";");
        }
        if (ctok.equals("BEGIN")) {
            eat("BEGIN");
            while(!ctok.equals("END")) parseStatement();
            eat("END");
            eat(";");
        } 
        if(this.isID(ctok)) {
            // System.out.println("var" + ctok);
            String tmpv = ctok;
            eat(ctok);
            eat(":=");
            //try {
                // v.put(tmpv, Integer.parseInt(ctok));
            
            int tmp = parseExpression();
            // System.out.println("==" + tmpv + " " + tmp);
                vint.put(tmpv, tmp);
            // } catch (NumberFormatException e) {
            //     System.out.println("var dec format check: " + e);
            //     return;
            // }
                // System.out.println("--" + ctok);
            // eat(ctok);
            eat(";");
        } 
        if (ctok.equals("(*")) {
            eat("(*");
            while (!ctok.equals("*)")) eat(ctok);
            eat("*)");
        }
    }


    public int parseFactor() throws ScanErrorException {
        return this.pFHelper(false);
    }

    private int pFHelper(boolean sign) throws ScanErrorException {
        if (ctok.matches("\\d+")) {
            return parseNumber() * (sign ? -1 : 1);
        } else if (this.isID(ctok)) {
            int val = vint.get(ctok) * (sign ? -1 : 1);
            eat(ctok);
            return val;
        } else if (ctok.equals("-")) {
            eat("-");
            return pFHelper(!sign);
        } else if (ctok.equals("(") || ctok.equals(")")) {
            // eat("(");
            // int tmp = pFHelper(sign);
            // eat(")");
            // return tmp;
            eat(ctok);
            return pFHelper(sign);
        }
        // System.out.println("!?uh oh: " + ctok);
        return -1;
    }

    // 6 * 2 / 3
    public int parseTerm() throws ScanErrorException {
        // boolean pflag = (ctok == "(" ? true : false);
        // if (pflag) eat(ctok);
        // if (ctok.matches("\\d+")) {
        //     return parseNumber();
        // }

        int t1 = parseFactor();
        // eat(ctok); // t1
        while(ctok.equals("*") || ctok.equals("/") || ctok.equals("mod") || ctok.equals("(")) {
            // System.out.println("!!!" + ctok);
            switch(ctok) {
                case "*" -> {eat(ctok); t1 *= p();}
                case "/" -> {eat(ctok); t1 /= p();}
                case "mod" -> {eat(ctok); t1 %= p();}
            }
        }
        // if (pflag) eat(")");
        return t1;
    }

    private int p() throws ScanErrorException {
        boolean pflag = (ctok.equals("(") ? true : false);
        if (pflag) eat(ctok);
        // System.out.println("--" + ctok + pflag);
        int N = -1;
        boolean nflag = ctok.equals("-");
        if (nflag) eat("-");
        if (ctok.matches("\\d+")) {
            N = parseExpression();
            if (nflag) N *= -1;
            // System.out.println("---" + N);
        } else if (this.isID(ctok)) {
            N = vint.get(ctok) * (nflag ? -1 : 1);
            eat(ctok);
        } else if (ctok.equals("("))  {
            N = p() * (nflag ? -1 : 1);
        } else {
            System.out.println("!!uh oh!!" + ctok);
        }

        if (pflag) eat(")");
        // System.out.println("!!" + N);
        return N;
    }

    public int parseExpression() throws ScanErrorException {
        // System.out.println("__" + ctok);
        int t = parseTerm();
        // System.out.println("t1 " + t);
        // System.out.println("__"+ ctok);
        while (ctok.equals("+") || ctok.equals("-")) {
            if (ctok.equals("+")) { 
                eat("+");
                t += parseTerm(); 
            } else if (ctok.equals("-")) { 
                // System.out.println("------");
                eat("-"); 
                t -= parseTerm(); 
            }
        }
        return t;
    }

    private boolean isID(String tok) {
        return tok.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$") &&
            !KEYWORDS.contains(ctok);
    }
}
