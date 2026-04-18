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
    /**
     * Evaluates a program
     * @param p program to execute
     * @param env environment of vars
     * @throws Throwable for break and continue
     */
    public void exec(Program p, Environment env) throws Throwable {
        // declare all in global scope
        for(Map.Entry<String, Expression> me : p.getVars().entrySet()) {
            if (me.getValue() == null) env.setGlobalVar(me.getKey(), null);
            else env.setGlobalVar(me.getKey(), eval(me.getValue(), env));
        }
        ArrayList<Statement> stmts = p.getStmts();
        for (Statement stmt : stmts) exec(stmt, env);
    }

    public void compile(Program p, Environment env, Emitter e) throws Throwable {
        e.emit(".data\n");
        for(Map.Entry<String, Expression> me : p.getVars().entrySet()) {
            String name = "__var" + me.getKey();
            if (me.getValue() == null) {
                // just reserve 1024 bytes: an int32 will only need 4, but just in case for a longer string
                e.emit(name + ": .space 1024\n");
                continue;
            }
            if (me.getValue() instanceof Array a) { // deal with arrays
                e.emit(name + ": .space " + (a.getEnd() - a.getStart() + 1) * 4 + "\n");
                continue;
            }
            Object val = eval(me.getValue(), env);
            String type = switch (val) {
                case Integer i -> ".word";
                case String s -> ".asciiz";
                case java.lang.Boolean b -> { val = b ? 1 : 0; yield ".word"; }
                default -> { throw new RuntimeException("not recognize var type dec in globl"); }
            };
            e.emit(name + ": " + type + " " + val + "\n");
        }
        e.emit(".text\n.globl main\nmain: #Mars will automatically look for main\n\n");
        // main:
        ArrayList<Statement> stmts = p.getStmts();
        for(Statement stmt : stmts) compile(stmt, env, e);
        e.emit("\n\nli $v0 10 # normal termination\nsyscall");
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
            case Comment c ->
            {
                return;
            }
            case Break bk -> throw new ThrowBreak();
            case Continue ctn -> throw new ThrowContinue();
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
                boolean c = (java.lang.Boolean) eval(new BinOp("<", f.getVar(), f.getTo()), env);
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
            case Array a ->
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
                    default -> throw new RuntimeException("eval binop v1 " + v1 + " type not recognized");
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

    public void compile(Expression e, Environment env, Emitter em) throws Throwable {
        switch (e) {
            case Number n -> em.emit("li $v0, " + n.getVal() + "\n");
            case BinOp bo -> { // TODO only supporting ints rn; string concat later
                compile(bo.getExpr1(), env, em); // expr1 in $v0
                em.push(); // push $v0 to stack
                compile(bo.getExpr2(), env, em); // expr2 in $v0
                em.pop(); // pop stack into $t0
                switch (bo.getOp()) {
                    case "+" -> em.emit("addu $v0, $t0, $v0\n");
                    case "-" -> em.emit("subu $v0, $t0, $v0\n");
                    case "*" -> em.emit(
                        "multu $t0, $v0\n" +
                        "mflo $v0\n"
                        );
                    case "/" -> em.emit(
                        "divu $t0, $v0\n" +
                        "mflo $v0\n"
                        );
                    case "mod" -> em.emit(
                        "divu $t0, $v0\n" +
                        "mfhi $v0\n" // rem in hi register
                        );
                    default -> throw new RuntimeException("do not recognize op");
                }
            }
            case Variable v -> {
                em.emit(
                        "la $t0, __var" + v.getName() + "\n" +
                        "lw $v0 ($t0)\n"
                      );
            }
            case ArrayElement ae -> {
                // TODO implement this!!!!!
                System.out.println("array element compile is not impl yet!!!!");
            }
            default -> throw new RuntimeException("no expr in compile switched to");
        }
        em.emit("\n");
    }

    public void compile(Statement e, Environment env, Emitter em) throws Throwable {
        switch (e) {
            case Writeln w -> {
                // TODO currently only writeln nums
                compile(w.getExpression(), env, em); // put into $v0
                em.emit(
                        "# begin writeln\n" +
                        // "li $v0, " + val + "\n" + // should alr be in $v0
                        "move $a0, $v0\n" +
                        "li $v0, 1\n" + 
                        "syscall\n" +
                        "li $v0, 11\n" +
                        "li $a0, 10\n" +
                        "syscall\n\n" 
                        ); // only works if val is int
            }
            case Block b -> {
                for(Statement s : b.getStmts()) if (s != null) compile(s, env, em);
            }
            case Assignment a -> {
                compile(a.getExpression(), env, em); // assign to in $v0
                em.emit("la $t0, __var" + a.getVar().getName() + "\n" +
                        "sw $v0, ($t0)\n");
            }
            case If i -> {
                int lblid = em.nextLblId();
                String els = "else" + lblid, endif = "endif" + lblid;

                if (i.getElse() != null) {
                    compile(i.getCond(), env, em, els);
                    compile(i.getThen(), env, em);
                    em.emit(
                            "j " + endif + "\n" + 
                            els + ":\n" 
                           );
                    compile(i.getElse(), env, em);
                    em.emit(endif + ":\n");
                } else {
                    compile(i.getCond(), env, em, endif);
                    compile(i.getThen(), env, em);
                    em.emit(endif + ":\n");
                }
            }
            case While w -> {
                int lblid = em.nextLblId();
                String whil = "while" + lblid, endwhile = "endwhile" + lblid;
                em.emit(whil + ":\n"); // j back here
                compile(w.getCond(), env, em, endwhile);
                compile(w.getDo(), env, em);
                em.emit("j " + whil + "\n");
                em.emit(endwhile + ":\n");
            }
            default -> throw new RuntimeException("no stmt in compile switched to");
        }
        em.emit("\n");
    }

    public void compile(Expression e, Environment env, Emitter em, String lbl) throws Throwable {
        switch (e) {
            case BinOp bo -> {
                compile(bo.getExpr1(), env, em); // expr1 -> $v0
                em.push(); // sto $v0
                compile(bo.getExpr2(), env, em); // expr2 → $v0
                em.pop(); // now $t0 = expr1, $v0 = expr2
                String inst = switch (bo.getOp()) {
                    case "<" -> "bge";
                    case "<=" -> "bgt";
                    case ">" -> "ble";
                    case ">=" -> "blt";
                    case "<>" -> "beq";
                    case "=" -> "bne";
                    default -> throw new RuntimeException("not a relop; why are you here");
                };
                em.emit(inst + " $t0, $v0, " + lbl + "\n");
            }
            default -> throw new RuntimeException("probably incorrect overload of compile with expr and lbl");
        }
        em.emit("\n");
    }
}
