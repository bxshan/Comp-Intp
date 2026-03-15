package ast;

public class Boolean extends Expression {
    private boolean b;

    public boolean getVal() {
        return this.b;
    }

    public Boolean(boolean b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return b ? "TRUE" : "FALSE";
    }
}
