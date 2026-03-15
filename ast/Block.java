package ast;

import java.util.ArrayList;
import java.util.List;

public class Block extends Statement {
    private List<Statement> stmts;

    public Block(ArrayList<Statement> stmts) {
        this.stmts = stmts;
    }

    public List<Statement> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN\n");
        for (Statement stmt : stmts) {
            sb.append("  " + stmt.toString() + "\n");
        }
        sb.append("END;");
        return sb.toString();
    }
}
