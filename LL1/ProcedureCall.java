package LL1;
import java.util.ArrayList;

public class ProcedureCall extends Expression {
    private String name;
    private ArrayList<Expression> args;

    public ProcedureCall(String name, ArrayList<Expression> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Expression> getArgs() {
        return this.args;
    }
}
