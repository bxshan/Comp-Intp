package procedures;
import scanner.*;
import environment.*;
import java.util.*;

/**
 * PROC LAB
 *
 * @author Boxuan Shan
 * @version 03242025
 */
public class Parser 
{
    scanner.Scanner sc;
    String ctok;
    Environment env;
    Evaluator ev;
    // array fails on expr [*|/|mod] a[i]
    // TODO refactor to return obj not primitive

    static final Set<String> KEYWORDS = Set.of(
            "BEGIN", "END", "WRITELN",
            "READLN", "EOF", "TRUE",
            "FALSE", "NOT", "AND",
            "OR", "mod", "array",
            "IF", "THEN", "ELSE",
            "WHILE", "FOR", "REPEAT",
            "UNTIL", "BREAK", "CONTINUE", 
            "PROCEDURE", "__ignore", "EXIT"
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

    /**
     * gets variable map in this environment
     * @return HashMap of vars in this env
     */
    public HashMap<String, Object> getVarMap()
    {
        return env.getVars();
    }

    /**
     * gets global environment
     * @return golbal env
     */
    public Environment getEnv() 
    {
        return this.env;
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
    
    /**
     * parses entire program, returns program object
     * parses any number of procedure declarations, 
     * then any number of statements
     * @return Program object parsed
     * @throws Throwable for break and continue
     */
    public Program parseProgram() throws Throwable
    {
        ArrayList<Statement> s = new ArrayList<Statement>();
        while (ctok != null && !ctok.equals("$") && !ctok.equals("EOF")) 
        {
            if (ctok.equals("PROCEDURE")) s.add(parseProcDec());
            else s.add(parseStatement());
        }
        return new Program(s);
    }

    /**
     * parse a procedure declaration
     * @return ProcedureDeclaration parsed
     * @throws Throwable if parse fails
     */
    public ProcedureDeclaration parseProcDec() throws Throwable 
    {
        eat("PROCEDURE");
        String name = ctok;
        eat(ctok);
        eat("(");

        ArrayList<String> params = new ArrayList<>();
        while (!ctok.equals(")")) 
        {
            params.add(ctok);
            eat(ctok);
            if (ctok.equals(",")) eat(",");
        }

        eat(")");
        eat(";");
        Statement stmt = parseStatement();
        return new ProcedureDeclaration(name, params, stmt);
    }

    /**
     * parse an and executes one statement: WRITELN, READLN, BEGIN...END block,
     *      assignment, array declaration, array element assignment, comment
     * @precondition ctok is at start of a valid statement or EOF
     * @postcondition all tokens of the statement eaten
     * @return Statement parsed
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Statement parseStatement() throws Throwable 
    {
        switch (ctok)
        {
            case "WRITELN" ->
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
                    exp = new procedures.Boolean(boolVal);
                } 
                else if (ctok.equals("\"") ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof String))
                {
                    String strVal = parseStrExpression();
                    exp = new _String(strVal);
                }
                else
                {
                    // num expr, or int relop (boolean result)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok))
                    {
                        boolean boolVal = parseRelOp((Integer) ev.eval(lhs, env));
                        exp = new procedures.Boolean(boolVal);
                    }
                    else
                    {
                        exp = lhs;
                    }
                }
                eat(")");
                eat(";");
                return new Writeln(exp);
            }
            case "READLN" ->
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
            case "BEGIN" ->
            {
                eat("BEGIN");
                ArrayList<Statement> stmts = new ArrayList<Statement>();
                while(!ctok.equals("END")) stmts.add(parseStatement());
                eat("END");
                eat(";");
                return new Block(stmts);
            }
            case "IF" ->
            {
                eat("IF");
                Expression c;
                HashMap<String, Object> v = env.getVars();
                if (!ctok.matches("\\d+") && (isBool(ctok) ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT")))
                {
                    // just a normal bool
                    c = new procedures.Boolean(parseBoolExpression());
                }
                else
                {
                    // check lhs relop rhs (expr)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok))
                    {
                        String op = ctok;
                        eat(ctok);
                        c = new BinOp(op, lhs, parseExpression());
                    }
                    else c = lhs; 
                }
                eat("THEN");
                Statement t = parseStatement();

                if (ctok.equals("ELSE"))
                {
                    Statement e;
                    eat("ELSE");
                    e = parseStatement();

                    return new If(c, t, e);
                }
                return new If(c, t);
            }
            case "WHILE" ->
            {
                eat("WHILE");
                HashMap<String, Object> v = env.getVars();
                Expression c;
                if (!ctok.matches("\\d+") && (isBool(ctok) ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT")))
                {
                    // just a normal bool
                    c = new procedures.Boolean(parseBoolExpression());
                }
                else
                {
                    // check lhs relop rhs (expr)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok))
                    {
                        String op = ctok;
                        eat(ctok);
                        c = new BinOp(op, lhs, parseExpression());
                    }
                    else c = lhs;
                }
                eat("DO");
                Statement d = parseStatement();

                return new While(c, d);
            }
            case "FOR" ->
            {
                eat("FOR");
                HashMap<String, Object> v = env.getVars();
                Assignment i = (Assignment) parseStatement(true);

                eat("TO");

                Expression t;

                if (!ctok.matches("\\d+") && (isBool(ctok) ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT")))
                {
                    // just a normal bool
                    t = new procedures.Boolean(parseBoolExpression());
                }
                else
                {
                    // check lhs relop rhs (expr)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok))
                    {
                        String op = ctok;
                        eat(ctok);
                        t = new BinOp(op, lhs, parseExpression());
                    }
                    else t = lhs;
                }

                eat("DO");
                Statement d = parseStatement();

                return new For(i, t, d);
            }
            case "REPEAT" ->
            {
                eat("REPEAT");
                HashMap<String, Object> v = env.getVars();
                Statement r = parseStatement();

                eat("UNTIL");

                Expression u;

                if (!ctok.matches("\\d+") && (isBool(ctok) ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT")))
                {
                    // just a normal bool
                    u = new procedures.Boolean(parseBoolExpression());
                }
                else
                {
                    // check lhs relop rhs (expr)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok))
                    {
                        String op = ctok;
                        eat(ctok);
                        u = new BinOp(op, lhs, parseExpression());
                    }
                    else u = lhs;
                }

                return new RepeatUntil(r, u);
            }
            case "BREAK", "EXIT" ->
            {
                if (ctok.equals("BREAK")) eat("BREAK");
                else eat("EXIT");
                eat(";");
                return new Break();
            }
            case "CONTINUE" ->
            {
                eat("CONTINUE");
                eat(";");
                return new Continue();
            }
            case "EOF", "$" ->
            {
                return null;
            }
            case "(*" ->
            {
                eat("(*");
                while (!ctok.equals("*)")) eat(ctok);
                eat("*)");
                return new Comment();
            }
            default ->
            {
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
                                    (v.containsKey(tmpv) && 
                                     v.get(tmpv) instanceof java.lang.Boolean) ||
                                    ctok.equals("NOT"))) 
                        {
                            boolean tmp = parseBoolExpression();
                            if (!v.containsKey(tmpv)) env.setVar(tmpv, tmp);
                            exp = new procedures.Boolean(tmp);
                        } 
                        else if (ctok.equals("\"") ||
                                (v.containsKey(tmpv) && v.get(tmpv) instanceof String) ||
                                (v.containsKey(ctok) && v.get(ctok) instanceof String))
                        {
                            String lhs = parseStrExpression();
                            if (!v.containsKey(tmpv)) env.setVar(tmpv, lhs);
                            exp = new _String(lhs);
                        } 
                        else if (ctok.equals("array")) 
                        {
                            eat("array");
                            eat("[");
                            Expression l = parseExpression();
                            eat("..");
                            Expression r = parseExpression();
                            eat("]");
                            if (!v.containsKey(tmpv))
                                env.setVar(tmpv, new HashMap<Integer, Object>());
                            exp = new Array((Integer) ev.eval(l, env), (Integer) ev.eval(r, env));
                        }
                        else
                        {
                            // int, or int relop (boolean result)
                            Expression lhs = parseExpression();
                            if (isRelOp(ctok))
                            {
                                boolean boolVal = parseRelOp((Integer) ev.eval(lhs, env));
                                if (!v.containsKey(tmpv)) env.setVar(tmpv, boolVal);
                                exp = new procedures.Boolean(boolVal);
                            }
                            else
                            {
                                if (!v.containsKey(tmpv)) env.setVar(tmpv, 0);
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

                        Expression exp;
                        if (isBool(ctok) || 
                                (v.containsKey(ctok) && 
                                 env.getObjVar(ctok) instanceof java.lang.Boolean) ||
                                ctok.equals("NOT"))
                            exp = new procedures.Boolean(parseBoolExpression());
                        else if (ctok.equals("\"") ||
                                (v.containsKey(ctok) && env.getObjVar(ctok) instanceof String))
                            exp = new _String(parseStrExpression());
                        else
                            exp = parseExpression();
                        eat(";");
                        return new ArrayAssignment(new Variable(tmpv), idx, exp);
                    }
                    else if (ctok.equals("(")) // proc call as stmt
                    {
                        eat("(");
                        ArrayList<Expression> args = new ArrayList<Expression>();
                        while(!ctok.equals(")")) 
                        {
                            args.add(parseExpression());
                            if(ctok.equals(",")) eat(",");
                        }
                        eat(")");
                        eat(";");
                        // throw the output away into a dummy variable
                        return new Assignment(
                                new Variable("__ignore"), 
                                new ProcedureCall(tmpv, args)
                                );
                    }
                }
                else throw new IllegalArgumentException(
                        "Unrecognized statement or syntax error at token: " + ctok);
            }
        }
        return null;
    }

    /**
     * parseStatement() overload to parse the variable declaration in a FOR 
     * only difference is that it does not eat a ';' after declaration
     * @precondition ctok is at start of var declaration in FOR 
     * @postcondition all tokens of the var declaration are eaten
     * @param inFor true if in a FOR: either one calls this method overload
     * @return var declaration parsed
     * @throws ScanErrorException if scanner encounters an illegal character
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Statement parseStatement(boolean inFor) throws Throwable
    {
        // System.out.println("in special");
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
                        (v.containsKey(tmpv) && 
                         v.get(tmpv) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT"))) 
                {
                    boolean tmp = parseBoolExpression();
                    if (!v.containsKey(tmpv)) env.setVar(tmpv, tmp);
                    exp = new procedures.Boolean(tmp);
                } 
                else if (ctok.equals("\"") ||
                        (v.containsKey(tmpv) && v.get(tmpv) instanceof String) ||
                        (v.containsKey(ctok) && v.get(ctok) instanceof String))
                {
                    String lhs = parseStrExpression();
                    if (!v.containsKey(tmpv)) env.setVar(tmpv, lhs);
                    exp = new _String(lhs);
                } 
                else if (ctok.equals("array")) 
                {
                    eat("array");
                    eat("[");
                    Expression l = parseExpression();
                    eat("..");
                    Expression r = parseExpression();
                    eat("]");
                    if (!v.containsKey(tmpv)) env.setVar(tmpv, new HashMap<Integer, Object>());
                    exp = new Array((Integer) ev.eval(l, env), (Integer) ev.eval(r, env));
                }
                else
                {
                    // int, or int relop (boolean result)
                    Expression lhs = parseExpression();
                    if (isRelOp(ctok))
                    {
                        boolean boolVal = parseRelOp((Integer) ev.eval(lhs, env));
                        if (!v.containsKey(tmpv)) env.setVar(tmpv, boolVal);
                        exp = new procedures.Boolean(boolVal);
                    }
                    else
                    {
                        if (!v.containsKey(tmpv)) env.setVar(tmpv, 0);
                        exp = lhs;
                    }
                }
                if (!inFor) eat(";");
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
                        (v.containsKey(ctok) && 
                         env.getObjVar(ctok) instanceof java.lang.Boolean) ||
                        ctok.equals("NOT"))
                    exp = new procedures.Boolean(parseBoolExpression());
                else if (ctok.equals("\"") ||
                        (v.containsKey(ctok) && env.getObjVar(ctok) instanceof String))
                    exp = new _String(parseStrExpression());
                else
                    exp = parseExpression();
                return new ArrayAssignment(new Variable(tmpv), idx, exp);
            }
        } 
        else throw new RuntimeException("false call of parsestatement with param in if");
        return null;
    }

    /**
     * parse the current int token and returns its value
     * @precondition ctok is int 
     * @postcondition int token eaten
     * @return value of eaten int
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private Number parseNumber() throws Throwable 
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean parseBool() throws Throwable 
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private String parseStr() throws Throwable 
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Expression parseFactor() throws Throwable 
    {
        return this.pfHelper(false);
    }

    /**
     * recursive helper for parseFactor: carries accumulated sign through unary minus chains
     * @precondition ctok is at start of int factor
     * @postcondition factor tokens eaten
     * @param sign accumulated sign; 1 -> negative, 0 -> positive
     * @return signed int value of factor
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private Expression pfHelper(boolean sign) throws Throwable 
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
            {
                // is an array int member
                eat("[");
                Expression idx = parseExpression();
                eat("]");

                e = new ArrayElement(a, idx);
            } 
            else if (ctok.equals("(")) 
            { // proc
                eat("(");
                ArrayList<Expression> args = new ArrayList<Expression>();
                while(!ctok.equals(")")) 
                {
                    args.add(parseExpression());
                    if(ctok.equals(",")) eat(",");
                }
                eat(")");
                e = new ProcedureCall(a, args);
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
        {
            // take string len
            eat("#");
            String s = parseStrFactor();
            e = new Number(s.length());
        }
        else
        {
            throw new IllegalArgumentException("Unexpected token in pfHelper: " + ctok);
        }

        if (sign)
        {
            return new BinOp("-", new Number(0), e);
        }
        return e;
    }

    /**
     * parse a bool factor: TRUE/FALSE literal, var, NOT, parentheses expr
     * @precondition ctok is at the start of a bool factor
     * @postcondition factor tokens are eaten
     * @return bool value of the factor
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public boolean parseBoolFactor() throws Throwable 
    {
        return this.pbfHelper(false);
    }

    /**
     * recursive helper for parseBoolFactor, carrying accumulated NOT through negation chains
     * @precondition ctok is at start of boolean factor
     * @postcondition factor tokens eaten
     * @param sign accumulated negation; 1 -> !bool, 0 -> bool
     * @return signed bool value of factor
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean pbfHelper(boolean sign) throws Throwable 
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
            {
                // array bool member
                eat("[");
                Expression idx = parseExpression();
                eat("]");

                boolean elm = false;
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr = (HashMap<Integer, Object>) env.getObjVar(a);
                Object valObj = arr.get(ev.eval(idx, env));
                if (valObj != null) elm = (boolean) valObj;

                return sign ? !elm : elm;
            } 
            else if (ctok.equals("(")) 
            {
                eat("(");
                ArrayList<Expression> args = new ArrayList<Expression>();
                while(!ctok.equals(")")) 
                {
                    args.add(parseExpression());
                    if(ctok.equals(',')) eat(",");
                }
                eat(")");
                ev.eval(new ProcedureCall(a, args), env);
                return false; // TODO default return
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public String parseStrFactor() throws Throwable 
    {
        return this.psfHelper();
    }

    /**
     * implementation of parseStrFactor, also handles array element access
     * @precondition ctok is at start of string factor
     * @postcondition factor tokens eaten
     * @return string value of the factor, or "" if no match
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private String psfHelper() throws Throwable 
    {
        if (ctok.equals("\"")) 
        {
            // at start of str
            return parseStr();
        }
        else if (this.isID(ctok)) 
        {
            String a = ctok;
            eat(ctok);
            if (ctok.equals("[")) 
            {
                // array string member
                eat("[");
                Expression idx = parseExpression();
                eat("]");

                String elm = "";
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr = (HashMap<Integer, Object>) env.getObjVar(a);
                Object valObj = arr.get(ev.eval(idx, env));
                if (valObj != null) elm = (String) valObj;

                return elm;
            } 
            else if (ctok.equals("(")) 
            {
                eat("(");
                ArrayList<Expression> args = new ArrayList<Expression>();
                while(!ctok.equals(")")) 
                {
                    args.add(parseExpression());
                    if(ctok.equals(',')) eat(",");
                }
                eat(")");
                ev.eval(new ProcedureCall(a, args), env);
                return ""; // TODO default return
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Expression parseTerm() throws Throwable 
    {
        Expression e = parseFactor();
        while(ctok.equals("*") || ctok.equals("/") || ctok.equals("mod")) 
        {
            String op = ctok;
            eat(ctok);
            e = new BinOp(op, e, parseFactor());
        }

        return e;
    }



    /**
     * parse a bool term, handle AND --- binary operation
     * @precondition ctok at start of boolean term
     * @postcondition term tokens eaten
     * @return bool value of the term 
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public boolean parseBoolTerm() throws Throwable 
    {
        boolean t1 = parseBoolFactor();
        while(ctok.equals("AND")) 
        {
            eat("AND"); 
            t1 = t1 && parseBoolFactor();
        }

        return t1;
    }



    /**
     * parse a string term, handle + concat --- binary operation
     * @precondition ctok is at start of string term
     * @postcondition term tokens eaten
     * @return string value of the term 
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public String parseStrTerm() throws Throwable 
    {
        String t1 = parseStrFactor();
        while (ctok.equals("+")) 
        {
            // concat
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public Expression parseExpression() throws Throwable 
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public boolean parseBoolExpression() throws Throwable 
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
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    public String parseStrExpression() throws Throwable 
    {
        return parseStrTerm();
    } // only 1 binary expression precedence level

    /**
     * parse and eval relational operator against int lhs
     * @precondition ctok a relational operator; lhs already parsed as int
     * @postcondition relop and rhs tokens eaten
     * @param lhs int operand on lhs
     * @return bool result of relational operator
     * @throws IllegalArgumentException if token sequence does not match grammar
     */
    private boolean parseRelOp(int lhs) throws Throwable
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
    private boolean parseRelOp(String lhs) throws Throwable 
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
