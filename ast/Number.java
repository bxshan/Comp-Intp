package ast;

public class Number extends Expression {
    private int n;

    public Number(int n) {
        this.n = n;
    }

    public int getVal() {
        return this.n;
    }

    @Override
    public String toString() {
        return Integer.toString(n);
    }
}