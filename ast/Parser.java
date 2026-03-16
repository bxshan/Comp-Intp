package ast;
import scanner.*;
import environment.*;
import java.util.*;

/**
 * @author Boxuan Shan
 * @version 1.0
 *  
 */
public class Parser 
{
    scanner.Scanner sc;
    String ctok;
    Environment env;
    Evaluator ev;
    // array fails on expr [*|/|mod] a[i]

    static final Set<String> KEYWORDS = Set.of(
            "BEGIN", "END", "WRITELN",
            "READLN", "EOF", "TRUE",
            "FALSE", "NOT", "AND",
            "OR", "mod", "array"
            );

    /**
     * constructor for Parser
     * instantiates scanner to read from, ctok current token to first token, 
     * var the HashMap storing var declarations
     * @param sc the scanner to take tokens from
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Parser(scanner.Scanner sc) throws ScanErrorException 
    {
        this.sc = sc;
        this.ctok = this.sc.nextToken();
        this.env = new Environment();
        this.ev = new Evaluator();
    }

    public HashMap<String, Object> getVarMap() {
        return env.getVars();
    }

    /**
     * eats the current token ctok only if passed in a token that matches ctok
     * @param e token that must match ctok to eat
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private void eat(String e) throws ScanErrorException 
    {
        if (e.equals(ctok)) ctok = sc.nextToken();
        else throw new IllegalArgumentException("expected \"" + e + 
                "\", actually ctok \"" + ctok + "\"");
    }
    
    public void parse() throws ScanErrorException {
        ev.exec(parseStatement(), env);
    }

    /**
     * parse an and executes one statement: WRITELN, READLN, BEGIN...END block,
     *      assignment, array declaration, array element assignment, comment
     * @precondition ctok is at start of a valid statement or EOF
     * @postcondition all tokens of the statement eaten
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Statement parseStatement() throws ScanErrorException 
    {
        if (ctok.equals("WRITELN")) 
        {
            eat("WRITELN");
            eat("(");
            Expression exp;
            HashMap<String, Object> v = env.getVars();
            if (isBool(ctok) ||
                    (v.containsKey(ctok) && v.get(ctok) instanceof java.lang.Boolean) ||
                    ctok.equals("NOT"))
            {
                boolean boolVal = parseBoolExpression();
                exp = new ast.Boolean(boolVal);
            } 
            else if (ctok.equals("\"") ||
                    (v.containsKey(ctok) && v.get(ctok) instanceof String))
            {
                String strVal = parseStrExpression();
                exp = new SString(strVal);
            }
            else
            { // num expr, or int relop (boolean result)
                Expression lhs = parseExpression();
                if (isRelOp(ctok)) {
                    boolean boolVal = parseRelOp((Integer) ev.eval(lhs, env));
                    exp = new ast.Boolean(boolVal);
                } else {
                    exp = lhs;
                }
            }
            eat(")");
            eat(";");

            return new Writeln(exp);
        }

        if (ctok.equals("READLN")) 
        {
            java.util.Scanner scn = new java.util.Scanner(System.in);
            eat("READLN");
            eat("(");
            String varName = ctok;
            System.out.print("WRITE TO " + varName + ": ");
            String in = scn.nextLine();
            Variable v = new Variable(varName);
            if (in.matches("\\d+")) env.setVar(varName, Integer.parseInt(in));
            else if (this.isBool(in)) env.setVar(varName, in.equals("TRUE"));
            else env.setVar(varName, in); // string
            eat(varName);
            eat(")");
            eat(";");

            return new Readln(v);
        }

        if (ctok.equals("BEGIN")) 
        {
            eat("BEGIN");
            ArrayList<Statement> stmts = new ArrayList<Statement>();
            while(!ctok.equals("END")) stmts.add(parseStatement());
            eat("END");
            eat(";");
            return new Block(stmts);
        } 

        if(this.isID(ctok)) 
        {
            String tmpv = ctok;
            eat(ctok);
            HashMap<String, Object> v = env.getVars();
            if (ctok.equals(":=")) 
            {
                eat(":=");
                Expression exp;

                if (!ctok.matches("\\d+") && (isBool(ctok) ||
                        (v.containsKey(tmpv) && v.get(tmpv) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT"))) 
                {
                    boolean tmp = parseBoolExpression();
                    env.setVar(tmpv, tmp);
                    exp = new ast.Boolean(tmp);
                } 
                else if (ctok.equals("\"") ||
                        (v.containsKey(tmpv) && v.get(tmpv) instanceof String) ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof String))
                {
                    String lhs = parseStrExpression();
                    env.setVar(tmpv, lhs);
                    exp = new SString(lhs);
                } 
                else if (ctok.equals("array")) 
                { 
                    eat("array");
                    eat("[");
                    Expression l = parseExpression();
                    eat("..");
                    Expression r = parseExpression();
                    eat("]");
                    env.setVar(tmpv, new HashMap<Integer, Object>());
                    exp = new Array((Integer) ev.eval(l, env), (Integer) ev.eval(r, env));
                }
                else
                { // int, or int relop (boolean result)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok)) {
                        boolean boolVal = parseRelOp((Integer) ev.eval(lhs, env));
                        env.setVar(tmpv, boolVal);
                        exp = new ast.Boolean(boolVal);
                    } else {
                        int val = (Integer) ev.eval(lhs, env);
                        env.setVar(tmpv, val);
                        exp = lhs;
                    }
                }
                eat(";");
                return new Assignment(new Variable(tmpv), exp);
            } 
            else if (ctok.equals("[")) 
            { 
                eat("[");
                Expression idx = parseExpression();
                eat("]");
                eat(":=");
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr = (HashMap<Integer, Object>) env.getObjVar(tmpv);

                Expression exp;
                if (isBool(ctok) || 
                        (v.containsKey(ctok) && env.getObjVar(ctok) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT"))
                    exp = new ast.Boolean(parseBoolExpression());
                else if (ctok.equals("\"") ||
                        (v.containsKey(ctok) && env.getObjVar(ctok) instanceof String))
                    exp = new SString(parseStrExpression());
                else
                    exp = parseExpression();
                eat(";");
                return new ArrayAssignment(new Variable(tmpv), idx, exp);
            }
        } 

        if (ctok.equals("IF")) {
            eat("IF");
            Expression c;
            HashMap<String, Object> v = env.getVars();
            if (!ctok.matches("\\d+") && (isBool(ctok) ||
                    (v.containsKey(ctok) && v.get(ctok) instanceof java.lang.Boolean) ||
                    ctok.equals("NOT"))) {
                c = new ast.Boolean(parseBoolExpression());
            } else {
                Expression lhs = parseExpression();
                if (isRelOp(ctok)) {
                    c = new ast.Boolean(parseRelOp((Integer) ev.eval(lhs, env)));
                } else {
                    c = lhs;
                }
            }
            eat("THEN");
            Statement t = parseStatement();
            return new If(c, t);
        }

        if (ctok.equals("(*")) 
        {
            eat("(*");
            while (!ctok.equals("*)")) eat(ctok);
            eat("*)");
            return null; 
        }

        return null;
    }

    /**
     * parse the current int token and returns its value
     * @precondition ctok is int 
     * @postcondition int token eaten
     * @return value of eaten int
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private Number parseNumber() throws ScanErrorException 
    {
        int n = -Integer.MAX_VALUE;
        try 
        {
            n = Integer.parseInt(ctok);
        } 
        catch (NumberFormatException e) 
        {
            System.out.println("num wrong format check parser parseNumber(): " + e);
        }
        eat(ctok);
        return new Number(n);
    }

    /**
     * parse a current bool token and returns value
     * @precondition ctok is TRUE or FALSE
     * @postcondition bool token eaten
     * @return value of eaten bool
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean parseBool() throws ScanErrorException 
    {
        boolean b = false;
        try 
        {
            b = ctok.equals("TRUE");
        }
        catch (NumberFormatException e) 
        {
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
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private String parseStr() throws ScanErrorException 
    {
        String s = "";
        eat("\"");
        while (!ctok.equals("\"")) 
        {
            s += ctok;
            eat(ctok);
        }
        eat("\"");
        return s.replace("_", " ");
    }

    /**
     * parse an int factor (+unary operations): 
     * number, var, negative, parenthesized expression, #sfact
     * @precondition ctok is at the start of an int factor
     * @postcondition factor tokens eaten
     * @return int value of the factor
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Expression parseFactor() throws ScanErrorException 
    {
        return this.pfHelper(false);
    }

    /**
     * recursive helper for parseFactor: carries accumulated sign through unary minus chains
     * @precondition ctok is at start of int factor
     * @postcondition factor tokens eaten
     * @param sign accumulated sign; 1 -> negative, 0 -> positive
     * @return signed int value of factor
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private Expression pfHelper(boolean sign) throws ScanErrorException 
    {
        Expression e;
        if (ctok.matches("\\d+")) 
        {
            e = parseNumber();
        }
        else if (this.isID(ctok))
        {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) 
            { // is an array int member
                eat("[");
                Expression idx = parseExpression();
                eat("]");

                e = new ArrayElement(a, idx);
            }
            else e = new Variable(a);
        } 
        else if (ctok.equals("-")) 
        {
            eat("-");
            return pfHelper(!sign);
        } 
        else if (ctok.equals("(")) 
        {
            eat("(");
            e = parseExpression();
            eat(")");
        } 
        else if (ctok.equals("#"))
        { // take string len
            eat("#");
            String s = parseStrFactor();
            e = new Number(s.length());
        }
        else {
            throw new IllegalArgumentException("Unexpected token in pfHelper: " + ctok);
        }

        if (sign) {
            return new BinOp("-", new Number(0), e);
        }
        return e;
    }

    /**
     * parse a bool factor: TRUE/FALSE literal, var, NOT, parentheses expr
     * @precondition ctok is at the start of a bool factor
     * @postcondition factor tokens are eaten
     * @return bool value of the factor
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public boolean parseBoolFactor() throws ScanErrorException 
    {
        return this.pbfHelper(false);
    }

    /**
     * recursive helper for parseBoolFactor, carrying accumulated NOT through negation chains
     * @precondition ctok is at start of boolean factor
     * @postcondition factor tokens eaten
     * @param sign accumulated negation; 1 -> !bool, 0 -> bool
     * @return signed bool value of factor
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean pbfHelper(boolean sign) throws ScanErrorException 
    {
        if (this.isBool(ctok)) 
        {
            return (sign ? !parseBool() : parseBool());
        }
        else if (this.isID(ctok)) 
        {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) 
            { // array bool member
                eat("[");
                Expression idx = parseExpression();
                eat("]");

                @SuppressWarnings("unchecked")
                boolean elm = (boolean) ((HashMap<Integer, Object>) env.getObjVar(a)).get(ev.eval(idx, env));
                return sign ? !elm : elm;
            }
            boolean val = (sign ? !((boolean) env.getObjVar(a)) : (boolean) env.getObjVar(a));
            return val;
        } 
        else if (ctok.equals("NOT")) 
        {
            eat("NOT");
            return pbfHelper(!sign);
        }
        else if (ctok.equals("(")) 
        {
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
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public String parseStrFactor() throws ScanErrorException 
    {
        return this.psfHelper();
    }

    /**
     * implementation of parseStrFactor, also handles array element access
     * @precondition ctok is at start of string factor
     * @postcondition factor tokens eaten
     * @return string value of the factor, or "" if no match
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private String psfHelper() throws ScanErrorException 
    {
        if (ctok.equals("\"")) 
        { // at start of str
            return parseStr();
        }
        else if (this.isID(ctok)) 
        {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) 
            { // array string member
                eat("[");
                Expression idx = parseExpression();
                eat("]");

                @SuppressWarnings("unchecked")
                String elm = (String) ((HashMap<Integer, Object>) env.getObjVar(a)).get(ev.eval(idx, env));
                return elm;
            }
            String val = (String) env.getObjVar(a);
            return val;
        } 
        else if (ctok.equals("(")) 
        {
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
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Expression parseTerm() throws ScanErrorException 
    {
        Expression e = parseFactor();
        while(ctok.equals("*") || ctok.equals("/") || ctok.equals("mod")) 
        {
            String op = ctok;
            eat(ctok);
            e = new BinOp(op, e, pi());
        }

        return e;
    }

    /**
     * parse a value on rhs of * / mod operation
     * handle surrounding paren and unary minus
     * @precondition ctok is at start of int factor or '('
     * @postcondition factor tokens eaten
     * @return int value of factor
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private Expression pi() throws ScanErrorException 
    {
        boolean pflag = ctok.equals("(");
        if (pflag) eat(ctok);
        Expression e;
        boolean nflag = ctok.equals("-");
        if (nflag) eat("-");
        if (ctok.matches("\\d+")) 
        {
            e = parseExpression();
        }
        else if (this.isID(ctok)) 
        {
            e = new Variable(ctok);
            eat(ctok);
        }
        else if (ctok.equals("("))  
        {
            e = pi();
        }
        else
        {
            throw new IllegalArgumentException("!!uh oh!!" + ctok);
        }

        if (nflag) e = new BinOp("-", new Number(0), e); // -e = 0 - e

        if (pflag) eat(")");

        return e;
    }

    /**
     * parse a bool term, handle AND --- binary operation
     * @precondition ctok at start of boolean term
     * @postcondition term tokens eaten
     * @return bool value of the term 
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public boolean parseBoolTerm() throws ScanErrorException 
    {
        boolean t1 = parseBoolFactor();
        while(ctok.equals("AND")) 
        {
            switch(ctok) 
            {
                case "AND" -> 
                    {
                        eat(ctok); 
                        t1 &= pb();
                    }
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
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean pb() throws ScanErrorException 
    {
        boolean pflag = ctok.equals("(");
        if (pflag) eat(ctok);
        boolean n = false;
        boolean nflag = ctok.equals("NOT");
        if (nflag) eat("NOT");
        if (isBool(ctok)) 
        {
            n = parseBoolExpression();
            if (nflag) n = !n;
        }
        else if (this.isID(ctok)) 
        {
            n = (nflag ? !((boolean) env.getObjVar(ctok)) : (boolean) env.getObjVar(ctok));
            eat(ctok);
        }
        else if (ctok.equals("("))  
        {
            n = (nflag ? !pb() : pb());
        }
        else 
        {
            System.out.println("!!uh oh!!" + ctok);
        }

        if (pflag) eat(")");

        return n;
    }

    /**
     * parse a string term, handle + concat --- binary operation
     * @precondition ctok is at start of string term
     * @postcondition term tokens eaten
     * @return string value of the term 
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public String parseStrTerm() throws ScanErrorException 
    {
        String t1 = parseStrFactor();
        while (ctok.equals("+")) 
        { // concat
            eat("+");
            t1 += parseStrFactor();
        }
        return t1;
    }

    // public String ps() throws ScanErrorException {...} // not needed, refer above method

    /**
     * parse an int expression, handling + and - : 
     * binary operations of equal priority (but below * / mod)
     * @precondition ctok at start of int expression
     * @postcondition expression tokens eaten
     * @return int value of the expression 
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Expression parseExpression() throws ScanErrorException 
    {
        Expression e = parseTerm();
        while (ctok.equals("+") || ctok.equals("-")) 
        {
            String op = ctok;
            eat(op);
            e = new BinOp(op, e, parseTerm());
        }

        return e;
    }

    /**
     * parse a bool expression, handle OR at lowest precedence
     * @precondition ctok at start of bool expression
     * @postcondition expression tokens eaten
     * @return bool value of the expression
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public boolean parseBoolExpression() throws ScanErrorException 
    {
        boolean t = parseBoolTerm();
        while (ctok.equals("OR")) 
        {
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
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public String parseStrExpression() throws ScanErrorException 
    {
        return parseStrTerm();
    } // only 1 binary expression precedence level

    /**
     * parse and eval relational operator against int lhs
     * @precondition ctok a relational operator; lhs already parsed as int
     * @postcondition relop and rhs tokens eaten
     * @param lhs int operand on lhs
     * @return bool result of relational operator
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean parseRelOp(int lhs) throws ScanErrorException 
    {
        String op = ctok; 
        eat(ctok);
        int rhs = (Integer) ev.eval(parseExpression(), env);
        return switch(op) 
        {
            case ">=" -> lhs >= rhs;
            case "<=" -> lhs <= rhs;
            case "="  -> lhs == rhs;
            case "<>" -> lhs != rhs;
            case "<" -> lhs < rhs;
            case ">" -> lhs > rhs;
            default -> throw new RuntimeException("no relop detected");
        };
    }

    /**
     * parse and eval relational operator against String lhs
     * @precondition ctok a relational operator; lhs already parsed as String 
     * @postcondition relop and rhs tokens eaten
     * @param lhs String operand on lhs
     * @return bool result of relational operator
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean parseRelOp(String lhs) throws ScanErrorException 
    {
        String op = ctok;
        eat(ctok);
        String rhs = parseStrExpression();
        return switch(op) 
        {
            case "=" -> lhs.equals(rhs);
            case "<>" -> !lhs.equals(rhs);
            case "<" -> lhs.compareTo(rhs) < 0;
            case ">" -> lhs.compareTo(rhs) > 0;
            case "<=" -> lhs.compareTo(rhs) <= 0;
            case ">=" -> lhs.compareTo(rhs) >= 0;
            default -> throw new RuntimeException("no relop detected");
        };
    }

    /**
     * check if tok is valid var name
     * @param tok token to test
     * @return true if tok is valid id & not reserved keyword
     */
    private boolean isID(String tok) 
    {
        // any valid var name in java that is not reserved
        return tok.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$") &&
            !KEYWORDS.contains(tok);
    }

    /**
     * checks whether tok is bool literal
     * @param tok token to test
     * @return true if tok is TRUE or FALSE
     */
    private boolean isBool(String tok) 
    {
        return tok.equals("TRUE") || tok.equals("FALSE");
    }

    /**
     * checks whether tok is relational operator
     * @param tok token to test
     * @return true if tok is one of =, <>, <, >, <=, >=
     */
    private boolean isRelOp(String tok) 
    {
        return tok.equals(">=") || tok.equals("<=") || tok.equals("=") ||
               tok.equals("<>") || tok.equals("<")  || tok.equals(">");
    }
}
