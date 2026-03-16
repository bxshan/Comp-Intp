package ast;

public class While extends Statement {
    private Expression condition;
    private Statement _do;

    public While(Expression condition, Statement _do) {
        this.condition = condition;
        this._do = _do;
    }

    public Expression getCond() {
        return this.condition;
    }

    public Statement getDo() {
        return this._do;
    }

    @Override
    public String toString() {
        String res = "WHILE " + condition + " DO " + _do;
        return res;
    }
}
