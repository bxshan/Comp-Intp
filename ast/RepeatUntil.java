package ast;

public class RepeatUntil extends Statement {
    private Statement repeat;
    private Expression until;

    public RepeatUntil(Statement repeat, Expression until) {
        this.repeat = repeat;
        this.until = until;
    }

    public Statement getRepeat() {
        return this.repeat;
    }

    public Expression getUntil() {
        return this.until;
    }

    @Override
    public String toString() {
        String res = "REPEAT " + repeat + " UNTIL " + until;
        return res;
    }
}

