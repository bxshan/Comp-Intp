package ast;

import java.util.List;

public class Array extends Expression {
    private int start;
    private int end;

    public Array(int start, int end) {
        this.start = start;
        this.end = end;
    }
}