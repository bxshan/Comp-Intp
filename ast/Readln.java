package ast;

public class Readln extends Statement {
    private Variable var;

    public Readln(Variable var) {
        this.var = var;
    }

    public Variable getVar() {
        return this.var;
    }

    @Override
    public String toString() {
        return "READLN(" + var.toString() + ");";
    }
}
