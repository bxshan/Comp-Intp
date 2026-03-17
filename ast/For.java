package ast;

public class For extends Statement {
    private Assignment init;
    private Expression to;
    private Statement _do;
    private Variable v;

    public For(Assignment init, Expression to, Statement _do) {
        this.init = init;
        this.to = to;
        this._do = _do;
        this.v = init.getVar();
    }

    public Assignment getInit() {
        return this.init;
    }

    public Expression getTo() {
        return this.to;
    }

    public Statement getDo() {
        return this._do;
    }

    public Variable getVar() {
        return this.v;
    }

    @Override
    public String toString() {
        String res = "FOR " + init + " TO " + to + " DO " + _do;
        return res;
    }
}

