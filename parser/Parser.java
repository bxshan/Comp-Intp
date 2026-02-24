package parser;
import scanner.*;
import java.util.HashMap;
import java.util.Set;

public class Parser {
    Scanner sc;
    String ctok;
    HashMap<String, Integer> v;

    static final Set<String> KEYWORDS = Set.of("BEGIN", "END");

    public Parser(Scanner sc) throws ScanErrorException {
        this.sc = sc;
        this.ctok = this.sc.nextToken();
        this.v = new HashMap<>();
    }

    private void eat(String e) throws ScanErrorException {
        if (e.equals(ctok)) ctok = sc.nextToken();
        else throw new IllegalArgumentException("expected \"" + e + "\", actually ctok \"" + ctok + "\"");
    }

    /**
     * @precondition current tok is an integer
     * @postcondition integer token is eaten
     * @return value of eaten int
     */
    private int parseNumber() throws ScanErrorException {
        try {
            int n = Integer.parseInt(ctok);
        } catch (NumberFormatException e) {
            System.out.println("num wrong format check parser parseNumber(): " + e);
            return -Integer.MAX_VALUE;
        }
        eat(ctok);
        return n;
    }

    public void parseStatement() throws ScanErrorException {
        while (ctok.equals("WRITELN")) {
            eat("WRITELN");
            eat("(");
            System.out.println(parseExpression());
            eat(")");
            eat(";");
        }
        if (ctok.equals("BEGIN")) {
            eat("BEGIN");
            parseStatement();
            eat("END");
            eat(";");
        } else if (this.isID(ctok)) {
            String tmpv = ctok;
            eat(ctok);
            eat(":=");
            //try {
                // v.put(tmpv, Integer.parseInt(ctok));
                v.put(tmpv, parseNumber());
            // } catch (NumberFormatException e) {
            //     System.out.println("var dec format check: " + e);
            //     return;
            // }
            eat(ctok);
            eat(";");
        }
    }


    public int parseFactor() throws ScanErrorException {
        return this.pFHelper(false);
    }

    private int pFHelper(boolean sign) throws ScanErrorException {
        if (ctok.matches("\\d+")) {
            return parseNumber() * (sign ? -1 : 1);
        } else if (this.isID(ctok)) {
            return v.get(ctok) * (sign ? -1 : 1);
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
        System.out.println("!?uh oh: " + ctok);
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
        while(ctok.equals("*") || ctok.equals("/") || ctok.equals("(")) {
            // System.out.println("!!!" + ctok);
            char op = ctok.charAt(0);
            eat(ctok); // op
            if (op == '*') t1 *= p();
            else if (op == '/') t1 /= p();
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
        int t = parseTerm();
        while (ctok.equals("+") || ctok.equals("-")) {
            if (ctok.equals("+")) { 
                eat("+");
                t += parseTerm(); 
            } else { 
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
