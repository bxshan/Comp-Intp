package parser;
import scanner.*;
import java.util.*;

/**
 * Parser.java is a simple parser for Compilers and Interpreters (2025-2026 S2) 
 * @author Boxuan Shan
 * @version 1.0
 *  
 * Usage:
 * receive a stream of tokens from scanner.Scanner, then excecutes Pascal-like statements
 */
public class Parser {
    scanner.Scanner sc;
    String ctok;
    HashMap<String, Object> v;
    // array fails on expr [*|/|mod] a[i]

    static final Set<String> KEYWORDS = Set.of(
            "BEGIN", "END", "WRITELN",
            "READLN", "EOF", "TRUE",
            "FALSE", "NOT", "AND",
            "OR", "mod", "array"
            );

    /**
     * constructor for Parser
     * instantiates scanner to read from, ctok current token to first token, v the HashMap storing var declarations
     * @param sc the scanner to take tokens from
     */
    public Parser(scanner.Scanner sc) throws ScanErrorException {
        this.sc = sc;
        this.ctok = this.sc.nextToken();
        this.v = new HashMap<String, Object>();
    }

    /**
     * eats the current token ctok only if passed in a token that matches ctok
     * @param e token that must match ctok to eat
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private void eat(String e) throws ScanErrorException {
        if (e.equals(ctok)) ctok = sc.nextToken();
        else throw new IllegalArgumentException("expected \"" + e + "\", actually ctok \"" + ctok + "\"");
    }

    /**
     * parse an and executes one statement: WRITELN, READLN, BEGIN...END block,
     *      assignment, array declaration, array element assignment, comment
     * @precondition ctok is at start of a valid statement or EOF
     * @postcondition all tokens of the statement eaten
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
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
            String tmpv = ctok;
            eat(ctok);
            if (ctok.equals(":=")) {
                eat(":=");

                if (isBool(ctok) || v.get(ctok) instanceof Boolean || ctok.equals("NOT")) {
                    boolean tmp = parseBoolExpression();
                    v.put(tmpv, tmp);
                } else if (ctok.equals("\"") || v.get(ctok) instanceof String) {
                    String lhs = parseStrExpression();
                    if (isRelOp(ctok)) v.put(tmpv, parseRelOp(lhs));
                    else v.put(tmpv, lhs);
                } else if (ctok.equals("array")) { // array declaration
                    eat("array");
                    eat("[");
                    int l = parseExpression();
                    eat(",");
                    int r = parseExpression();
                    eat("]");
                    v.put(tmpv, new HashMap<Integer, Object>());
                } else { // int
                    int lhs = parseExpression();
                    if (isRelOp(ctok)) v.put(tmpv, parseRelOp(lhs));
                    else v.put(tmpv, lhs);
                }
                eat(";");
            } else if (ctok.equals("[")) { // array element assign
                eat("[");
                int idx = parseExpression();
                eat("]");
                eat(":=");
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr = (HashMap<Integer, Object>) v.get(tmpv);

                if (isBool(ctok) || v.get(ctok) instanceof Boolean || ctok.equals("NOT")) {
                    arr.put(idx, parseBoolExpression());
                } else if (ctok.equals("\"") || v.get(ctok) instanceof String) {
                    String lhs = parseStrExpression();
                    if (isRelOp(ctok)) arr.put(idx, parseRelOp(lhs));
                    else arr.put(idx, lhs);
                } else {
                    int lhs = parseExpression();
                    if (isRelOp(ctok)) arr.put(idx, parseRelOp(lhs));
                    else arr.put(idx, lhs);
                }
                eat(";");
            }
        } 

        if (ctok.equals("(*")) {
            eat("(*");
            while (!ctok.equals("*)")) eat(ctok);
            eat("*)");
        }
    }

    /**
     * parse the current int token and returns its value
     * @precondition ctok is int 
     * @postcondition int token eaten
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

    /**
     * parse a current bool token and returns value
     * @precondition ctok is TRUE or FALSE
     * @postcondition bool token eaten
     * @return value of eaten bool
     */
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

    /**
     * parse a quoted string literal; replace '_' with ' '
     * @precondition ctok is opening "
     * @postcondition all tokens through closing " eaten
     * @return string literal value with '_' replace by ' '
     */
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

    /**
     * parse an int factor (+unary operations): number, var, negative, parenthesized expression, #sfact
     * @precondition ctok is at the start of an int factor
     * @postcondition factor tokens eaten
     * @return int value of the factor
     */
    public int parseFactor() throws ScanErrorException {
        return this.pFHelper(false);
    }

    /**
     * recursive helper for parseFactor: carries accumulated sign through unary minus chains
     * @precondition ctok is at start of int factor
     * @postcondition factor tokens eaten
     * @param sign accumulated sign; 1 -> negative, 0 -> positive
     * @return signed int value of factor
     */
    private int pFHelper(boolean sign) throws ScanErrorException {
        if (ctok.matches("\\d+")) {
            return parseNumber() * (sign ? -1 : 1);
        } else if (this.isID(ctok)) {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) { // is an array int member
                eat("[");
                int idx = parseExpression();
                eat("]");
                
                @SuppressWarnings("unchecked")
                int elm = (Integer) ((HashMap<Integer, Object>) v.get(a)).get(idx);
                return elm * (sign ? -1 : 1);
            }
            int val = (Integer) v.get(a) * (sign ? -1 : 1);
            return val;
        } else if (ctok.equals("-")) {
            eat("-");
            return pFHelper(!sign);
        } else if (ctok.equals("(")) {
            eat("(");
            int tmp = parseExpression();
            eat(")");
            return tmp * (sign ? -1 : 1);
        } else if (ctok.equals("#")) { // take string len
            eat("#");
            String s = parseStrFactor();
            return s.length() * (sign ? -1 : 1);
        }

        return -1;
    }

    /**
     * parse a bool factor: TRUE/FALSE literal, var, NOT, parentheses expr
     * @precondition ctok is at the start of a bool factor
     * @postcondition factor tokens are eaten
     * @return bool value of the factor
     */
    public boolean parseBoolFactor() throws ScanErrorException {
        return this.pBFHelper(false);
    }

    /**
     * recursive helper for parseBoolFactor, carrying accumulated NOT through negation chains
     * @precondition ctok is at start of boolean factor
     * @postcondition factor tokens eaten
     * @param sign accumulated negation; 1 -> !bool, 0 -> bool
     * @return signed bool value of factor
     */
    private boolean pBFHelper(boolean sign) throws ScanErrorException {
        if (this.isBool(ctok)) {
            return (sign ? !parseBool() : parseBool());
        } else if (this.isID(ctok)) {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) { // array bool member
                eat("[");
                int idx = parseExpression();
                eat("]");

                @SuppressWarnings("unchecked")
                boolean elm = (Boolean) ((HashMap<Integer, Object>) v.get(a)).get(idx);
                return sign ? !elm : elm;
            }
            boolean val = (sign ? !(Boolean) v.get(a) : (Boolean) v.get(a));
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

    /**
     * parse a string factor: string literal, var, parentheses expr 
     * @precondition ctok is at start of string factor
     * @postcondition factor tokens eaten
     * @return string value of the factor
     */
    public String parseStrFactor() throws ScanErrorException {
        return this.pSFHelper();
    }

    /**
     * implementation of parseStrFactor, also handles array element access
     * @precondition ctok is at start of string factor
     * @postcondition factor tokens eaten
     * @return string value of the factor, or "" if no match
     */
    private String pSFHelper() throws ScanErrorException {
        if (ctok.equals("\"")) { // at start of str
            return parseStr();
        } else if (this.isID(ctok)) {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) { // array string member
                eat("[");
                int idx = parseExpression();
                eat("]");

                @SuppressWarnings("unchecked")
                String elm = (String) ((HashMap<Integer, Object>) v.get(a)).get(idx);
                return elm;
            }
            String val = (String) v.get(a);
            return val;
        } else if (ctok.equals("(")) {
            eat("(");
            String tmp = parseStrExpression();
            eat(")");
            return tmp;
        }

        return "";
    }

    /**
     * parse an int term, handling *, /, mod --- binary operations of equal priority
     * @precondition ctok is at start of int term
     * @postcondition term tokens eaten
     * @return int value of the term 
     */
    public int parseTerm() throws ScanErrorException {
        int t1 = parseFactor();
        while(ctok.equals("*") || ctok.equals("/") || ctok.equals("mod")) {
            switch(ctok) {
                case "*" -> {eat(ctok); t1 *= p();}
                case "/" -> {eat(ctok); t1 /= p();}
                case "mod" -> {eat(ctok); t1 %= p();}
            }
        }

        return t1;
    }

    /**
     * parse a value on rhs of * / mod operation
     * handle surrounding paren and unary minus
     * @precondition ctok is at start of int factor or '('
     * @postcondition factor tokens eaten
     * @return int value of factor
     */
    private int p() throws ScanErrorException {
        boolean pflag = (ctok.equals("(") ? true : false);
        if (pflag) eat(ctok);
        int N = -1;
        boolean nflag = ctok.equals("-");
        if (nflag) eat("-");
        if (ctok.matches("\\d+")) {
            N = parseExpression();
            if (nflag) N *= -1;
        } else if (this.isID(ctok)) {
            N = (Integer) v.get(ctok) * (nflag ? -1 : 1);
            eat(ctok);
        } else if (ctok.equals("("))  {
            N = p() * (nflag ? -1 : 1);
        } else {
            System.out.println("!!uh oh!!" + ctok);
        }

        if (pflag) eat(")");

        return N;
    }

    /**
     * parse a bool term, handle AND --- binary operation
     * @precondition ctok at start of boolean term
     * @postcondition term tokens eaten
     * @return bool value of the term 
     */
    public boolean parseBoolTerm() throws ScanErrorException {
        boolean t1 = parseBoolFactor();
        while(ctok.equals("AND")) {
            switch(ctok) {
                case "AND" -> {eat(ctok); t1 &= pb();}
            }
        }

        return t1;
    }

    /**
     * parse a value on rhs of AND operation
     * handle surrounding paren and unary NOT 
     * @precondition ctok is at start of bool factor or '('
     * @postcondition factor tokens eaten
     * @return bool value of factor
     */
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

    /**
     * parse a string term, handle + concat --- binary operation
     * @precondition ctok is at start of string term
     * @postcondition term tokens eaten
     * @return string value of the term 
     */
    public String parseStrTerm() throws ScanErrorException {
        String t1 = parseStrFactor();
        while (ctok.equals("+")) { // concat
            eat("+");
            t1 += parseStrFactor();
        }
        return t1;
    }

    // public String ps() throws ScanErrorException {...} // not needed, refer above method

    /**
     * parse an int expression, handling + and - --- binary operations of equal priority (but below * / mod)
     * @precondition ctok at start of int expression
     * @postcondition expression tokens eaten
     * @return int value of the expression 
     */
    public int parseExpression() throws ScanErrorException {
        int t = parseTerm();
        while (ctok.equals("+") || ctok.equals("-")) {
            if (ctok.equals("+")) { 
                eat("+");
                t += parseTerm(); 
            } else if (ctok.equals("-")) { 
                eat("-"); 
                t -= parseTerm(); 
            }
        }

        return t;
    }

    /**
     * parse a bool expression, handle OR at lowest precedence
     * @precondition ctok at start of bool expression
     * @postcondition expression tokens eaten
     * @return bool value of the expression
     */
    public boolean parseBoolExpression() throws ScanErrorException {
        boolean t = parseBoolTerm();
        while (ctok.equals("OR")) {
            // OR is only operator impl. lower than AND.
            // otherwise switch on ctok
            eat("OR");
            t |= parseBoolTerm(); 
        }

        return t;
    }

    /**
     * parse a string expression; delegates to parseStrTerm: no more binary operations
     * @precondition ctok at start of string expression
     * @postcondition expression tokens eaten
     * @return string value of expression
     */
    public String parseStrExpression() throws ScanErrorException {
        return parseStrTerm();
    } // only 1 binary expression precedence level

    /**
     * parse and eval relational operator against int lhs
     * @precondition ctok a relational operator; lhs already parsed as int
     * @postcondition relop and rhs tokens eaten
     * @param lhs int operand on lhs
     * @return bool result of relational operator
     */
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

    /**
     * parse and eval relational operator against String lhs
     * @precondition ctok a relational operator; lhs already parsed as String 
     * @postcondition relop and rhs tokens eaten
     * @param lhs String operand on lhs
     * @return bool result of relational operator
     */
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

    /**
     * check if tok is valid var name
     * @param tok token to test
     * @return true if tok is valid id & not reserved keyword
     */
    private boolean isID(String tok) {
        // any valid var name in java that is not reserved
        return tok.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$") &&
            !KEYWORDS.contains(tok);
    }

    /**
     * checks whether tok is bool literal
     * @param tok token to test
     * @return true if tok is TRUE or FALSE
     */
    private boolean isBool(String tok) {
        return tok.equals("TRUE") || tok.equals("FALSE");
    }

    /**
     * checks whether tok is relational operator
     * @param tok token to test
     * @return true if tok is one of =, <>, <, >, <=, >=
     */
    private boolean isRelOp(String tok) {
        return tok.equals(">=") || tok.equals("<=") || tok.equals("=") ||
               tok.equals("<>") || tok.equals("<")  || tok.equals(">");
    }
}
