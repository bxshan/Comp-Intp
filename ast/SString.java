package ast;

public class SString extends Expression {
    private String s;

    public String getVal() {
        return this.s;
    }

    public SString(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return this.s;
    }
}
