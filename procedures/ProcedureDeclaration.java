package procedures;
import java.util.ArrayList;

public class ProcedureDeclaration extends Statement {
    private String name;
    private ArrayList<String> params;
    private Statement stmt;

    public ProcedureDeclaration(String name, ArrayList<String> params, Statement stmt) {
        this.name = name;
        this.params = params;
        this.stmt = stmt;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getParams() {
        return this.params;
    }

    public Statement getStmt() {
        return this.stmt;
    }
}