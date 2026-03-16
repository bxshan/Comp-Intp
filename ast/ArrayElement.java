package ast;

public class ArrayElement extends Expression {
    private String name;
    private Expression idx;

    public ArrayElement(String name, Expression idx) {
        this.name = name;
        this.idx = idx;
    }


    public String getName() {
        return this.name;
    }

    public Expression getIdx() {
        return this.idx;
    }

    @Override
    public String toString() {
        return name + "[" + idx + "]";
    }
}

