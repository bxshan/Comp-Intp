package codegen;
import java.util.*;
import environment.*;

/**
 * Evaluator class to execute statements and evaluate expressions
 *
 * @author Boxuan Shan
 * @version 03242025
 */
public class Evaluator
{
    private Stack<String> breaklbl = new Stack<String>();
    private Stack<String> contlbl = new Stack<String>();

    /**
     * Evaluates a program
     * @param p program to execute
     * @param env environment of vars
     * @throws Throwable for break and continue
     */
    public void exec(Program p, Environment env) throws Throwable 
    {
        // declare all in global scope
        for(Map.Entry<String, Expression> me : p.getVars().entrySet()) 
        {
            if (me.getValue() == null) env.setGlobalVar(me.getKey(), null);
            else env.setGlobalVar(me.getKey(), eval(me.getValue(), env));
        }
        ArrayList<Statement> stmts = p.getStmts();
        for (Statement stmt : stmts) exec(stmt, env);
    }

    /**
     * compiles a program with emitter e
     * @param p program to compile
     * @param e object to use to write
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Program p, Emitter e) throws Throwable 
    {
        e.emit(".data\n");
        // always push ignore variable to use proc calls as stmts
        e.emit("__ignore: .space 1024\n"); 
        for(Map.Entry<String, Expression> me : p.getVars().entrySet()) 
        {
            String name = "__var" + me.getKey();
            if (me.getValue() == null) 
            {
                // just reserve 1024 bytes: 
                // an int32 will only need 4, but just in case for a longer string
                e.emit(name + ": .space 1024\n");
                continue;
            }
            if (me.getValue() instanceof Array a) 
            { // deal with arrays
                e.emit(name + ": .space " + (a.getEnd() - a.getStart() + 1) * 4 + "\n");
                continue;
            }
            switch (me.getValue()) 
            {
                case Number n -> e.emit(name + ": .word " + n.getVal() + "\n");
                case _String ss -> e.emit(name + ": .asciiz \"" + ss.getVal() + "\"\n");
                case Boolean b -> e.emit(name + ": .word " + (b.getVal() ? 1 : 0) + "\n");
                default -> throw new RuntimeException(
                        "global init must be literal/arr/null, got: " +
                        me.getValue().getClass().getSimpleName());
            }
        }
        ArrayList<Statement> stmts = p.getStmts();
        e.emit(".text\nj main\n\n");
        // pass 1: emit all procedure declarations
        for (Statement stmt : stmts) if (stmt instanceof ProcedureDeclaration) compile(stmt, e);
        e.emit("\n.globl main\nmain:\n\n");
        // pass 2: emit all non-procedure statements
        for (Statement stmt : stmts) if (!(stmt instanceof ProcedureDeclaration)) compile(stmt, e);

        e.emit("\n# termination\nli $v0 10\nsyscall");
        e.close();
    }

    /**
     * Executes a statement
     * @param stmt statement to execute
     * @param env of vars 
     * @throws Throwable for break and continue
     */
    public void exec(Statement stmt, Environment env) throws Throwable
    {
        switch (stmt)
        {
            case Comment _ -> 
            {
                return; 
            }
            case Break _ -> throw new ThrowBreak();
            case Continue _ -> throw new ThrowContinue();
            case Writeln w -> System.out.println(eval(w.getExpression(), env));
            case ArrayAssignment aa ->
            {
                // must be before assignment
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr =
                    (HashMap<Integer, Object>) env.getObjVar(aa.getVar().getName());
                arr.put((Integer) eval(aa.getIdx(), env), eval(aa.getExpression(), env));
            }
            case Assignment a ->
            {
                env.setVar(a.getVar().getName(), eval(a.getExpression(), env)); 
            }
            case Block b ->
            {
                for(Statement s : b.getStmts())
                {
                    if (s != null) exec(s, env);
                }
            }
            case Readln r ->
            {
                Scanner s = new Scanner(System.in);
                env.setVar(r.getVar().getName(), s.nextInt());
                s.close();
            }
            case If i ->
            {
                boolean c = (java.lang.Boolean) eval(i.getCond(), env);
                if (c) exec(i.getThen(), env);
                else if (i.getElse() != null) exec(i.getElse(), env);
            }
            case While w ->
            {
                boolean c = (java.lang.Boolean) eval(w.getCond(), env);
                while(c)
                {
                    try
                    {
                        exec(w.getDo(), env);
                        c = (java.lang.Boolean) eval(w.getCond(), env);
                    }
                    catch (ThrowBreak tb)
                    {
                        break;
                    }
                    catch (ThrowContinue tc)
                    {
                        c = (java.lang.Boolean) eval(w.getCond(), env);
                        continue;
                    }
                }
            }
            case For f ->
            {
                // var must be an integer, to must be a int expr
                Assignment i = (Assignment) f.getInit();
                this.exec(i, env);
                // pascal for is inclusive
                boolean c = (java.lang.Boolean) eval(new BinOp("<=", f.getVar(), f.getTo()), env);
                while(c)
                {
                    try
                    {
                        exec(f.getDo(), env);
                        env.setVar(f.getVar().getName(), env.getVar(f.getVar().getName()) + 1);
                        c = (java.lang.Boolean) eval(new BinOp("<", f.getVar(), f.getTo()), env);
                    }
                    catch (ThrowBreak tb)
                    {
                        break;
                    }
                    catch (ThrowContinue tc)
                    {
                        // increment idx and upd cond
                        env.setVar(f.getVar().getName(), env.getVar(f.getVar().getName()) + 1);
                        c = (java.lang.Boolean) eval(new BinOp("<", f.getVar(), f.getTo()), env);
                        continue;
                    }
                }
            }
            case RepeatUntil ru ->
            {
                boolean c;
                do
                {
                    try
                    {
                        exec(ru.getRepeat(), env);
                        c = (java.lang.Boolean) eval(ru.getUntil(), env);
                    }
                    catch (ThrowBreak tb)
                    {
                        break;
                    }
                    catch (ThrowContinue tc)
                    {
                        c = (java.lang.Boolean) eval(ru.getUntil(), env);
                        continue;
                    }
                } 
                while (!c);
            }
            case ProcedureDeclaration pd -> 
            {
                env.setProc(pd.getName(), pd); 
            }

            default -> throw new RuntimeException("unknown class of stmt in Evaluator/exec");
        }
    }

    /**
     * Evaluates an expression
     * @param e expression to evaluate
     * @param env of vars 
     * @return Object that e evaluates to
     */
    public Object eval(Expression e, Environment env) throws Throwable
    {
        switch (e)
        {
            case Number n ->
            {
                return n.getVal();
            }
            case Boolean b ->
            {
                return b.getVal();
            }
            case _String ss ->
            {
                return ss.getVal();
            }
            case Variable v ->
            {
                return env.getObjVar(v.getName());
            }
            case Array _ ->
            {
                return new HashMap<Integer, Object>();
            }
            case ArrayElement ae ->
            { 
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr =
                    (HashMap<Integer, Object>) env.getObjVar(ae.getName());
                Object val = arr.get((Integer) eval(ae.getIdx(), env));
                return val == null ? 0 : val;
            }
            case BinOp bo ->
            {
                Object v1 = eval(bo.getExpr1(), env);
                switch(v1)
                {
                    case Integer i1 ->
                    {
                        int v2 = (Integer) eval(bo.getExpr2(), env);

                        return switch (bo.getOp())
                        {
                            case "+" -> i1 + v2;
                            case "-" -> i1 - v2;
                            case "*" -> i1 * v2;
                            case "/" -> i1 / v2;
                            case "mod" -> i1 % v2;

                            case ">=" -> i1 >= v2;
                            case "<=" -> i1 <= v2;
                            case "="  -> i1 == v2;
                            case "<>" -> i1 != v2;
                            case "<"  -> i1 < v2;
                            case ">"  -> i1 > v2;
                            default -> throw new RuntimeException("check op " + bo.getOp());
                        };
                    }
                    case String s1 ->
                    {
                        String v2 = (String) eval(bo.getExpr2(), env);

                        return switch (bo.getOp())
                        {
                            case "+" -> s1 + v2;
                            default -> throw new RuntimeException("check op " + bo.getOp());
                        };
                    }
                    case java.lang.Boolean b ->
                    {
                        java.lang.Boolean b1 = (java.lang.Boolean) eval(bo.getExpr2(), env);

                        return switch (bo.getOp())
                        {
                            case "AND" -> b && b1;
                            case "OR"  -> b || b1;
                            case "="   -> b.equals(b1);
                            case "<>"  -> !b.equals(b1);
                            default -> throw new RuntimeException("check bool op " + bo.getOp());
                        };
                    }
                    default -> 
                        throw new RuntimeException("eval binop v1 " + v1 + " type not recognized");
                }
            }
            case ProcedureCall pc -> 
            {
                String name = pc.getName();
                ProcedureDeclaration pd = (ProcedureDeclaration) env.getProc(name);
                ArrayList<String> params = pd.getParams();
                ArrayList<Expression> args = pc.getArgs();
                
                Environment localEnv = new Environment(env); // hand off of parent env
                for (int i = 0; i < params.size(); i++) 
                    localEnv.setLocalVar(params.get(i), eval(args.get(i), env));

                // for returns
                localEnv.setLocalVar(name, 0);
                
                try 
                {
                    this.exec(pd.getStmt(), localEnv);
                } 
                catch (ThrowBreak tb)
                { // EXIT
                    // caught EXIT signal, stop executing procedure body
                }

                return localEnv.getObjVar(name);
                // only support integer returns for now
            }
            default -> throw new RuntimeException(
                    "unknown class of e in Evaluator/eval: " + e.getClass());
        }
    }

    /**
     * compiles an expression with emitter e
     * @param e expression to compile
     * @param em object to use to write
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Expression e, Emitter em) throws Throwable 
    {
        switch (e) 
        {
            case Number n -> em.emit(
                    "# begin expr num\n" +
                    "li $v0, " + n.getVal() + "\n" +
                    "# end expr num\n"
                    );
            case Boolean b -> em.emit(
                    "# begin expr bool\n" +
                    "li $v0, " + (b.getVal() ? 1 : 0) + "\n" +
                    "# end expr bool\n"
                    );
            case _String ss -> em.emit( // TODO Strings cooked: use 0 as filler val
                    "# begin expr string\n" +
                    "li $v0, 0\n" +
                    "# end expr string\n"
                    );
            case BinOp bo -> 
            {
                em.emit("# begin expr binop\n");
                compile(bo.getExpr1(), em); // expr1 in $v0
                em.push();
                compile(bo.getExpr2(), em); // expr2 in $v0
                em.pop(); // $t0 = expr1, $v0 = expr2
                switch (bo.getOp()) 
                {
                    case "+" -> em.emit("addu $v0, $t0, $v0\n");
                    case "-" -> em.emit("subu $v0, $t0, $v0\n");
                    case "*" -> em.emit("multu $t0, $v0\nmflo $v0\n");
                    case "/" -> em.emit("divu $t0, $v0\nmflo $v0\n");
                    case "mod" -> em.emit("divu $t0, $v0\nmfhi $v0\n");
                    // v ret bool
                    case "<" -> em.emit("slt $v0, $t0, $v0\n");
                    case ">" -> em.emit("slt $v0, $v0, $t0\n");
                    case "=" -> em.emit("seq $v0, $t0, $v0\n");
                    case "<=" -> em.emit("sle $v0, $t0, $v0\n");
                    case ">=" -> em.emit("sle $v0, $v0, $t0\n");
                    case "<>" -> em.emit("sne $v0, $t0, $v0\n");
                    case "AND" -> em.emit("and $v0, $t0, $v0\n");
                    case "OR" -> em.emit("or $v0, $t0, $v0\n");
                    default -> throw new RuntimeException("do not recognize op: " + bo.getOp());
                }
                em.emit("# end expr binop\n");
            }
            case Variable v -> 
            {
                em.emit("# begin expr var\n");
                String vn = v.getName();
                if (em.isLocVar(vn)) 
                {
                    // local: is on stack
                    int ofst = em.getOffset(vn);
                    em.emit("lw $v0, " + ofst + "($sp)\n");
                }
                else 
                {
                    // global
                    em.emit(
                            "la $t0, __var" + vn + "\n" + 
                            "lw $v0 ($t0)\n"
                            );
                }
                em.emit("# end expr var\n");
            }
            case ArrayElement ae -> 
            {
                em.emit("# begin expr array elem\n");
                // addr of a[i] is base + (idx - 1) * 4
                compile(ae.getIdx(), em); // idx -> $v0
                em.emit(
                        "subu $v0, $v0, 1\n" + // $v0 -= 1
                        "sll $v0, $v0, 2\n" + // $v0 *= 4
                        "la $t0, __var" + ae.getName() + "\n" + // base -> $t0
                        "addu $t0, $t0, $v0\n" + // compute base + (idx - 1) * 4
                        "lw $v0, ($t0)\n" // put a[i] into $v0
                );
                em.emit("# end expr array elem\n");
            }
            case Array a -> {} // alr handled in compile(Program) .data section
            case ProcedureCall pc -> 
            {
                em.emit("# begin proc call\n");
                String lbl = "proc" + pc.getName();
                em.push("$ra"); 

                ArrayList<Expression> args = pc.getArgs();
                for(Expression arg : args) 
                {
                    compile(arg, em); // res in $v0
                    em.push();
                }

                em.emit("jal " + lbl + "\n");

                for(int i = 0; i < args.size(); i++) em.pop();

                em.pop("$ra");
                em.emit("# endproc call\n");
            }
            default -> 
                throw new RuntimeException(
                        "no expr in compile switched to: " + e.getClass().getSimpleName()
                );
        }
        // em.emit("\n");
    }
 
    /**
     * compiles a statement with emitter e
     * @param e statement to compile
     * @param em object to use to write
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Statement e, Emitter em) throws Throwable 
    {
        switch (e) 
        {
            case ProcedureDeclaration pd -> 
            {
                em.emit("# begin stmt proc dec\n");
                String lbl = "proc" + pd.getName();
                em.emit(lbl + ":\n");
                em.push("$zero"); // push ret var to stack as 0 init
                em.setProcContext(pd);

                for (String lcl : pd.getVars().keySet()) 
                {
                    em.addLcl(lcl);
                    em.push("$zero");
                }

                compile(pd.getStmt(), em);

                for (int i = 0; i < pd.getVars().size(); i++) em.pop();

                em.pop("$v0");
                em.emit("jr $ra\n");
                em.clearProcContext();
                em.emit("# end stmt proc dev\n");
            }
            case Writeln w -> 
            {
                em.emit("# begin stmt writeln\n");
                if (!(w.getExpression() instanceof _String)) 
                { // TODO Strings are cooked
                    compile(w.getExpression(), em);
                    em.emit(
                            "move $a0, $v0\n" +
                            "li $v0, 1\n" +
                            "syscall\n" +
                            "li $v0, 11\n" +
                            "li $a0, 10\n" +
                            "syscall\n\n"
                            );
                }
                em.emit("# end stmt writeln\n");
            }
            case Block b -> 
            {
                em.emit("# begin stmt block\n");
                for(Statement s : b.getStmts()) if (s != null) compile(s, em);
                em.emit("# end stmt block\n");
            }
            case ArrayAssignment aa -> 
            {
                em.emit("# begin stmt arr assign\n");
                String vn = aa.getVar().getName();
                if (em.isLocVar(vn)) 
                {
                    // TODO
                } 
                else 
                {
                    // global
                    compile(aa.getExpression(), em);
                    em.push();
                    compile(aa.getIdx(), em);
                    em.emit(
                            "subu $v0, $v0, 1\n" +
                            "sll $v0, $v0, 2\n" +
                            "la $t1, __var" + vn + "\n" +
                            "addu $t1, $t1, $v0\n"
                    );
                    em.pop();
                    em.emit("sw $t0, ($t1)\n");
                }
                em.emit("# end stmt arr assign\n");
            }
            case Assignment a -> 
            {
                em.emit("# begin stmt assign\n");
                String vn = a.getVar().getName();
                compile(a.getExpression(), em); // expr in $v0
                if (em.isLocVar(vn)) 
                {
                    int ofst = em.getOffset(vn);
                    em.emit("sw $v0, " + ofst + "($sp)\n");
                } 
                else em.emit("la $t0, __var" + vn + "\nsw $v0, ($t0)\n");
                em.emit("# end stmt assign\n");
            }
            case If i -> 
            {
                em.emit("# begin stmt if\n");
                int lblid = em.nextLblId();
                String els = "else" + lblid, endif = "endif" + lblid;
                if (i.getElse() != null) 
                {
                    compile(i.getCond(), em, els);
                    compile(i.getThen(), em);
                    em.emit("j " + endif + "\n" + els + ":\n");
                    compile(i.getElse(), em);
                    em.emit(endif + ":\n");
                } 
                else 
                {
                    compile(i.getCond(), em, endif);
                    compile(i.getThen(), em);
                    em.emit(endif + ":\n");
                }
                em.emit("# end stmt if\n");
            }
            case While w -> 
            {
                em.emit("# begin stmt while\n");
                int lblid = em.nextLblId();
                String whil = "while" + lblid, endwhile = "endwhile" + lblid;
                breaklbl.push(endwhile); 
                contlbl.push(whil);
                em.emit(whil + ":\n");
                compile(w.getCond(), em, endwhile);
                compile(w.getDo(), em);
                em.emit("j " + whil + "\n");
                em.emit(endwhile + ":\n");
                breaklbl.pop();
                contlbl.pop();
                em.emit("# end stmt while\n");
            }
            case For f -> 
            {
                em.emit("# begin stmt for\n");
                int lblid = em.nextLblId();
                String fo = "for" + lblid, endfor = "endfor" + lblid, contfor = "contfor" + lblid;
                breaklbl.push(endfor);
                contlbl.push(contfor);
                compile(f.getInit(), em);
                em.emit(fo + ":\n");
                compile(new BinOp("<=", f.getVar(), f.getTo()), em, endfor); // inclusive pascal for
                compile(f.getDo(), em);
                em.emit(contfor + ":\n");
                // inc var
                String varn = "__var" + f.getVar().getName();
                em.emit(
                        "lw $t0, " + varn + "\n" +
                        "addi $t0, $t0, 1\n" +
                        "sw $t0, " + varn + "\n"
                        );
                em.emit("j " + fo + "\n");
                em.emit(endfor + ":\n");
                breaklbl.pop();
                contlbl.pop();
                em.emit("# end stmt for\n");
            }
            case RepeatUntil ru -> 
            {
                em.emit("# begin stmt rep until\n");
                int lblid = em.nextLblId();
                String rpt = "repeat" + lblid, endrpt = "endrpt" + lblid;
                breaklbl.push(endrpt);
                contlbl.push(rpt);
                em.emit(rpt + ":\n");
                compile(ru.getRepeat(), em);
                compile(ru.getUntil(), em, rpt);
                em.emit(endrpt + ":\n");
                breaklbl.pop();
                contlbl.pop();
                em.emit("# end stmt rep until\n");
            }
            case Readln rl -> 
            {
                em.emit("# begin stmt readln\n");
                // TODO: use rl.getType() once Readln carries type info; default to int
                em.emit(
                        "li $v0, 5\n" +
                        "syscall\n" +
                        "la $t0, __var" + rl.getVar().getName() + "\n" +
                        "sw $v0, ($t0)\n"
                );
                em.emit("# end stmt readln\n");
            }
            case Comment c -> 
            {
                return; 
            }
            case Break bk -> em.emit("j " + breaklbl.peek() + "\n"); 
            case Continue ct -> em.emit("j " + contlbl.peek() + "\n"); 
            default -> 
                throw new RuntimeException(
                        "no stmt in compile switched to: " + e.getClass().getSimpleName()
                );
        }
        // em.emit("\n");
    }

    /**
     * compiles an expression with emitter e
     * jumps to label lbl if expression is false 
     * @param e expression to compile
     * @param em object to use to write
     * @param lbl label to jump to if expression is false
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Expression e, Emitter em, String lbl) throws Throwable 
    {
        switch (e) 
        {
            case BinOp bo -> 
            {
                em.emit("# begin to lbl binop\n");

                // if AND or OR, 
                if (bo.getOp().equals("AND") || bo.getOp().equals("OR")) 
                {
                    compile(bo, em); // eval expr to $v0
                    em.emit("beq $v0, $zero, " + lbl + "\n");
                    em.emit("# end to lbl binop\n");
                    return;
                }

                compile(bo.getExpr1(), em);
                em.push();
                compile(bo.getExpr2(), em);
                em.pop(); // $t0 = expr1, $v0 = expr2
                String inst = switch (bo.getOp()) 
                {
                    case "<" -> "bge";
                    case "<=" -> "bgt";
                    case ">" -> "ble";
                    case ">=" -> "blt";
                    case "<>" -> "beq";
                    case "=" -> "bne";
                    default -> throw new RuntimeException("not a relop: " + bo.getOp());
                };
                em.emit(inst + " $t0, $v0, " + lbl + "\n");
                em.emit("# end to lbl binop\n");
            }
            case Boolean b -> 
            {
                em.emit("# begin to lbl bool\n");
                compile(b, em);
                em.emit("beq $v0, $zero, " + lbl + "\n");
                em.emit("# end to lbl bool\n");
            }
            case Variable v -> 
            { // todo local ? check compile var above
                em.emit("# begin to lbl var\n");
                compile(v, em);
                em.emit("beq $v0, $zero, " + lbl + "\n");
                em.emit("# end to lbl var\n");
            }
            default -> 
            {
                // defualt eval to $v0 and branch if 0
                compile(e, em);
                em.emit("beq $v0, $zero, " + lbl + "\n");
            }
        }
        // em.emit("\n");
    }
}
