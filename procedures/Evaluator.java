package procedures;

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
    public void exec(Program p, Environment env) throws Throwable 
    {
        ArrayList<Statement> stmts = p.getStmts();
        for(Statement stmt : stmts) exec(stmt, env);
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
                    default ->
                    {
                        throw new RuntimeException("eval binop v1 " + v1 + " type not recognized");
                    }
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
}
