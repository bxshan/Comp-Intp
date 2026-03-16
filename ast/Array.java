package ast;

public class Array extends Expression {
    private int start;
    private int end;

    public Array(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "array[" + start + ".." + end + "]";
    }
}
