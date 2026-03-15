package ast;

public class BinOp extends Expression {
    private String op;
    private Expression expr1, expr2;

    public BinOp(String op, Expression expr1, Expression expr2) {
        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public String getOp() {
        return op;
    }

    public Expression getExpr1() {
        return expr1;
    }

    public Expression getExpr2() {
        return expr2;
    }

    @Override
    public String toString() {
        return "(" + expr1.toString() + " " + op + " " + expr2.toString() + ")";
    }
}
