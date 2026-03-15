package ast;

public class Writeln extends Statement {
    private Expression expr;

    public Writeln(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpression() {
        return this.expr;
    }

    @Override
    public String toString() {
        return "WRITELN(" + expr.toString() + ");";
    }
}
