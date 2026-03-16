package ast;

import java.util.*;
import environment.*;

public class Evaluator {
    public void exec(Statement stmt, Environment env) {
        // System.out.println(stmt);
        switch (stmt) {
            case Writeln w -> System.out.println(eval(w.getExpression(), env));
            case ArrayAssignment aa -> { // must be before assignment
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr = (HashMap<Integer, Object>) env.getObjVar(aa.getVar().getName());
                arr.put((Integer) eval(aa.getIdx(), env), eval(aa.getExpression(), env));
            }
            case Assignment a -> env.setVar(a.getVar().getName(), eval(a.getExpression(), env));
            case Block b -> { for(Statement s : b.getStmts()) { if (s != null) exec(s, env); } }
            case Readln r -> {
                Scanner s = new Scanner(System.in);
                env.setVar(r.getVar().getName(), s.nextInt());
            }
            case If i -> {
                boolean c = (java.lang.Boolean) eval(i.getCond(), env);
                if (c) exec(i.getThen(), env);
                else if (i.getElse() != null) exec(i.getElse(), env);
            }
            case While w -> {
                boolean c = (java.lang.Boolean) eval(w.getCond(), env);
                while(c) {
                    exec(w.getDo(), env);
                    c = (java.lang.Boolean) eval(w.getCond(), env);
                }
            }
            default -> throw new RuntimeException("unknown class of stmt in Evaluator/exec");
        }
    }

    public Object eval(Expression e, Environment env) {
        switch (e) {
            case Number n -> { return n.getVal(); }
            case Boolean b -> { return b.getVal(); }
            case SString ss -> { return ss.getVal(); }
            case Variable v -> { return env.getObjVar(v.getName()); }
            case Array a -> { return new HashMap<Integer, Object>(); }
            case ArrayElement ae -> { 
                @SuppressWarnings("unchecked")
                HashMap<Integer, Object> arr = (HashMap<Integer, Object>) env.getObjVar(ae.getName());
                return arr.get((Integer) eval(ae.getIdx(), env));
            }
            case BinOp bo -> {
                Object v1 = eval(bo.getExpr1(), env);
                switch(v1) {
                    case Integer i1 -> {
                        int v2 = (Integer) eval(bo.getExpr2(), env);

                        return switch (bo.getOp()) {
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
                    case String s1 -> {
                        String v2 = (String) eval(bo.getExpr2(), env);

                        return switch (bo.getOp()) {
                            case "+" -> s1 + v2;
                                default -> throw new RuntimeException("check op " + bo.getOp());
                        };
                    }
                    default -> { throw new RuntimeException("eval binop v1 " + v1 + " type not recognized"); }
                }
            }
            default -> throw new RuntimeException("unknown class of e in Evaluator/eval: " + e.getClass());
        }
    }
}
