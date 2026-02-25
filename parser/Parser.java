package parser;
import scanner.*;
import java.util.*;

public class Parser {
    scanner.Scanner sc;
    String ctok;
    HashMap<String, Object> v;

    static final Set<String> KEYWORDS = Set.of(
            "BEGIN", "END", "WRITELN", "READLN", "EOF", 
            "TRUE", "FALSE", "NOT", "AND", "OR", "mod"
            );

    public Parser(scanner.Scanner sc) throws ScanErrorException {
        this.sc = sc;
        this.ctok = this.sc.nextToken();
        this.v = new HashMap<String, Object>();
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

    private boolean parseBool() throws ScanErrorException {
        boolean b = false;
        try {
            b = (ctok.equals("TRUE") ? true : false);
        } catch (NumberFormatException e) {
            System.out.println("bool wrong format check parser parseBool(): " + e);
        }
        eat(ctok);
        return b;
    }

    private String parseStr() throws ScanErrorException {
        String s = "";
        eat("\"");
        while (!ctok.equals("\"")) {
            s += ctok;
            eat(ctok);
        }
        eat("\"");
        return s.replace("_", " ");
    }

    public void parseStatement() throws ScanErrorException {
        while (ctok.equals("WRITELN")) {
            eat("WRITELN");
            eat("(");
            if (isBool(ctok) || v.get(ctok) instanceof Boolean || ctok.equals("NOT")) {
                System.out.println(parseBoolExpression());
            } else if (ctok.equals("\"") || v.get(ctok) instanceof String) {
                String lhs = parseStrExpression();
                if (isRelOp(ctok)) System.out.println(parseRelOp(lhs));
                else System.out.println(lhs);
            } else { // num expr
                int lhs = parseExpression();
                if (isRelOp(ctok)) System.out.println(parseRelOp(lhs));
                else System.out.println(lhs);
            }
            eat(")");
            eat(";");
        }

        if (ctok.equals("READLN")) {
            java.util.Scanner scn = new java.util.Scanner(System.in);
            eat("READLN");
            eat("(");
            System.out.print("WRITE TO " + ctok + ":\t");
            String in = scn.nextLine();
            if (in.matches("\\d+")) v.put(ctok, Integer.parseInt(in));
            else if (this.isBool(in)) v.put(ctok, in.equals("TRUE"));
            else v.put(ctok, in); // string
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

            if (isBool(ctok) || v.get(ctok) instanceof Boolean || ctok.equals("NOT")) {
                boolean tmp = parseBoolExpression();
                // System.out.println("==" + tmpv + " " + tmp);
                v.put(tmpv, tmp);
            } else if (ctok.equals("\"") || v.get(ctok) instanceof String) {
                String lhs = parseStrExpression();
                if (isRelOp(ctok)) v.put(tmpv, parseRelOp(lhs));
                else v.put(tmpv, lhs);
            } else { // string
                int lhs = parseExpression();
                if (isRelOp(ctok)) v.put(tmpv, parseRelOp(lhs));
                else v.put(tmpv, lhs);
            }
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
            int val = (Integer) v.get(ctok) * (sign ? -1 : 1);
            eat(ctok);
            return val;
        } else if (ctok.equals("-")) {
            eat("-");
            return pFHelper(!sign);
        } else if (ctok.equals("(")) {
            eat("(");
            int tmp = parseExpression();
            eat(")");
            return tmp * (sign ? -1 : 1);
        }
        // System.out.println("!?uh oh: " + ctok);
        return -1;
    }

    public boolean parseBoolFactor() throws ScanErrorException {
        return this.pBFHelper(false);
    }

    private boolean pBFHelper(boolean sign) throws ScanErrorException {
        if (this.isBool(ctok)) {
            return (sign ? !parseBool() : parseBool());
        } else if (this.isID(ctok)) {
            boolean val = (sign ? !(Boolean) v.get(ctok) : (Boolean) v.get(ctok));
            eat(ctok);
            return val;
        } else if (ctok.equals("NOT")) {
            eat("NOT");
            return pBFHelper(!sign);
        } else if (ctok.equals("(")) {
            eat("(");
            boolean tmp = parseBoolExpression();
            eat(")");
            return sign ? !tmp : tmp;
        }
        return false;
    }

    public String parseStrFactor() throws ScanErrorException {
        return this.pSFHelper();
    }

    private String pSFHelper() throws ScanErrorException {
        if (ctok.equals("\"")) { // at start of str
            return parseStr();
        } else if (this.isID(ctok)) {
            String val = (String) v.get(ctok);
            eat(ctok);
            return val;
        } /* else if (ctok.equals("#")) { // get len TODO
        eat("#");
        return pBFHelper(!sign);
        }*/ else if (ctok.equals("(")) {
            eat("(");
            String tmp = parseStrExpression();
            eat(")");
            return tmp;
        }
    return "";
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
        while(ctok.equals("*") || ctok.equals("/") || ctok.equals("mod")) {
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
            N = (Integer) v.get(ctok) * (nflag ? -1 : 1);
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

    public boolean parseBoolTerm() throws ScanErrorException {
        boolean t1 = parseBoolFactor();
        // eat(ctok); // t1
        while(ctok.equals("AND") /*|| ctok.equals("OR")*/) {
            // OR is strictly lower precedence than AND
            // System.out.println("!!!" + ctok);
            switch(ctok) {
                case "AND" -> {eat(ctok); t1 &= pb();}
            }
        }
        // if (pflag) eat(")");
        return t1;
    }

    private boolean pb() throws ScanErrorException {
        boolean pflag = (ctok.equals("(") ? true : false);
        if (pflag) eat(ctok);
        boolean N = false;
        boolean nflag = ctok.equals("NOT");
        if (nflag) eat("NOT");
        if (isBool(ctok)) {
            N = parseBoolExpression();
            if (nflag) N = !N;
        } else if (this.isID(ctok)) {
            N = (nflag ? !(Boolean) v.get(ctok) : (Boolean) v.get(ctok));
            eat(ctok);
        } else if (ctok.equals("("))  {
            N = (nflag ? !pb() : pb());
        } else {
            System.out.println("!!uh oh!!" + ctok);
        }

        if (pflag) eat(")");
        return N;
    }

    public String parseStrTerm() throws ScanErrorException {
        String t1 = parseStrFactor();
        while (ctok.equals("+")) { // concat
            eat("+");
            t1 += parseStrFactor();
        }
        return t1;
    }

    // public String ps() throws ScanErrorException {...} // not needed, refer above method


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

    public boolean parseBoolExpression() throws ScanErrorException {
        // System.out.println("__" + ctok);
        boolean t = parseBoolTerm();
        // System.out.println("t1 " + t);
        // System.out.println("__"+ ctok);
        while (ctok.equals("OR")) {
            // OR is only operator impl. lower than AND.
            // otherwise switch on ctok
            eat("OR");
            t |= parseBoolTerm(); 
        }
        return t;
    }

    public String parseStrExpression() throws ScanErrorException {
        return parseStrTerm();
    } // only 1 binary expression precedence level

    private boolean parseRelOp(int lhs) throws ScanErrorException {
        String op = ctok; 
        eat(ctok);
        int rhs = parseExpression();
        return switch(op) {
            case ">=" -> lhs >= rhs;
            case "<=" -> lhs <= rhs;
            case "="  -> lhs == rhs;
            case "<>" -> lhs != rhs;
            case "<" -> lhs < rhs;
            case ">" -> lhs > rhs;
            default -> false;
        };
    }

    private boolean parseRelOp(String lhs) throws ScanErrorException {
        String op = ctok;
        eat(ctok);
        String rhs = parseStrExpression();
        return switch(op) {
            case "=" -> lhs.equals(rhs);
            case "<>" -> !lhs.equals(rhs);
            case "<" -> lhs.compareTo(rhs) < 0;
            case ">" -> lhs.compareTo(rhs) > 0;
            case "<=" -> lhs.compareTo(rhs) <= 0;
            case ">=" -> lhs.compareTo(rhs) >= 0;
            default -> false;
        };
    }

    private boolean isID(String tok) {
        // any valid variable name in java that is not reserved
        return tok.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$") &&
            !KEYWORDS.contains(tok);
    }

    private boolean isBool(String tok) {
        return tok.equals("TRUE") || tok.equals("FALSE");
    }

    private boolean isRelOp(String tok) {
        return tok.equals(">=") || tok.equals("<=") || tok.equals("=") ||
            tok.equals("<>") || tok.equals("<")  || tok.equals(">");
    }
}
