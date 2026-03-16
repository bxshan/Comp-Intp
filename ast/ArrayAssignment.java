package ast;

public class ArrayAssignment extends Assignment {
    private Expression idx;

    public ArrayAssignment(Variable var, Expression idx, Expression expr) {
        super(var, expr);
        this.idx = idx;
    }

    public Expression getIdx() {
        return this.idx;
    }

    @Override
    public String toString() {
        return getVar().toString() + "[" + idx + "] := " + getExpression().toString() + ";";
    }
}
