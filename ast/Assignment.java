package ast;

public class Assignment extends Statement {
    private Variable var;
    private Expression expr;

    public Assignment(Variable var, Expression expr) {
        this.var = var;
        this.expr = expr;
    }

    public Variable getVar() {
        return this.var;
    }

    public Expression getExpression() {
        return this.expr;
    }

    @Override
    public String toString() {
        return var.toString() + " := " + expr.toString() + ";";
    }
}
