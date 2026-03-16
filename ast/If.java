package ast;

public class If extends Statement {
    private Expression condition;
    private Statement then;
    private Statement _else;

    public If(Expression condition, Statement then) {
        this.condition = condition;
        this.then = then;
        this._else = null;
    }

    public If(Expression condition, Statement then, Statement _else) {
        this.condition = condition;
        this.then = then;
        this._else = _else;
    }

    public Expression getCond() {
        return this.condition;
    }

    public Statement getThen() {
        return this.then;
    }

    public Statement getElse() {
        return this._else;
    }

    @Override
    public String toString() {
        String res = "IF " + condition + " THEN " + then;
        if (_else != null) res += " ELSE " + _else;
        return res;
    }
}
