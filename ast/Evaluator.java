package ast;

import java.util.*;
import environment.*;

public class Evaluator {
    public void exec(Statement stmt, Environment env) {
        switch (stmt) {
            case Writeln w -> System.out.println(eval(w.getExpression(), env));
            case Assignment a -> env.setVar(a.getVar().getName(), eval(a.getExpression(), env));
            case Block b -> { for(Statement s : b.getStmts()) { if (s != null) exec(s, env); } }
            case Readln r -> {
                Scanner s = new Scanner(System.in);
                env.setVar(r.getVar().getName(), s.nextInt());
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
            default -> throw new RuntimeException("unknown class of e in Evaluator/eval");
        }
    }
}
