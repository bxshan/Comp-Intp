package codegen;
import java.util.*;

/**
 * Evaluator class to execute statements and evaluate expressions
 *
 * @author Boxuan Shan
 * @version 03242025
 */
public class Evaluator
{
    private Stack<String> breaklbl = new Stack<String>();
    private Stack<String> contlbl = new Stack<String>();
    private ArrayList<String> strlits; // initialized in compile program
    private boolean debug = true;

    /**
     * sets debug option
     * if debug, emit begin and end comments
     * @param debug debug opt
     */
    public void debug(boolean debug) {
        this.debug = debug;
    }

    /**
     * compiles a program with emitter e
     * @param p program to compile
     * @param e object to use to write
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Program p, Emitter e) throws Throwable 
    {
        e.emit(".data\n");
        // always push ignore variable to use proc calls as stmts
        e.emit("__ignore: .space 1024\n"); 
        // now iterate through every var dec
        for(Map.Entry<String, Expression> me : p.getVars().entrySet()) 
        {
            String name = "__var" + me.getKey();
            if (me.getValue() == null) 
            {
                // no init: just reserve 1024 bytes: 
                // an int32 will only need 4, but just in case for a longer string
                e.emit(name + ": .space 1024\n");
                continue;
            }
            if (me.getValue() instanceof Array a) 
            { // deal with arrays: 1 byte for every idx in arr
                e.emit(name + ": .space " + (a.getEnd() - a.getStart() + 1) * 4 + "\n");
                continue;
            }
            switch (me.getValue()) 
            {
                case Number n -> e.emit(name + ": .word " + n.getVal() + "\n");
                case _String ss -> e.emit(name + ": .asciiz \"" + ss.getVal() + "\"\n");
                case Boolean b -> e.emit(name + ": .word " + (b.getVal() ? 1 : 0) + "\n");
                default -> throw new RuntimeException("global init var expr wrong\n");
            }
        }
        ArrayList<Statement> stmts = p.getStmts();

        this.strlits = this.collectStrLit(p); // call bfs

        for (int i = 0; i < strlits.size(); i++) 
        {
            String s = strlits.get(i);
            e.emit("__strliteral" + i + ": .asciiz " + "\"" + s + "\"\n");
        }

        e.emit(".text\nj main\n\n");
        // pass 1: emit all procedure declarations
        for (Statement stmt : stmts) if (stmt instanceof ProcedureDeclaration) compile(stmt, e);
        e.emit("\n.globl main\nmain:\n\n");
        // pass 2: emit all non-procedure statements
        for (Statement stmt : stmts) if (!(stmt instanceof ProcedureDeclaration)) compile(stmt, e);

        e.emit("\n# termination\nli $v0 10\nsyscall");
        e.close();
    }

    /**
     * perform bfs through the ast tree to collect all str literals
     * @param p root of ast tree
     * @return ordered literals as arraylist
     */
    private ArrayList<String> collectStrLit(Program p) 
    {
        ArrayList<String> ret = new ArrayList<String>();
        // queues for stmts and exprs
        ArrayDeque<Statement> qstmt = new ArrayDeque<Statement>();
        ArrayDeque<Expression> qexpr = new ArrayDeque<Expression>();

        // 1. vars 
        for (Expression initv : p.getVars().values()) if (initv != null) qexpr.add(initv);

        // 2. init stmts 
        for (Statement s : p.getStmts()) if (s != null) qstmt.add(s);

        while (!qstmt.isEmpty() || !qexpr.isEmpty()) 
        { // while at least 1 isnt empty
            while (!qstmt.isEmpty()) 
            { // go through stmts first
                Statement s = qstmt.remove(); // pop

                switch (s) 
                {
                    case Writeln w -> qexpr.add(w.getExpression());
                    case ArrayAssignment aa -> 
                    {
                        qexpr.add(aa.getIdx());
                        qexpr.add(aa.getExpression());
                    }
                    case Assignment a -> qexpr.add(a.getExpression());
                    case If i -> 
                    {
                        qexpr.add(i.getCond());
                        if (i.getThen() != null) qstmt.add(i.getThen());
                        if (i.getElse() != null) qstmt.add(i.getElse());
                    }
                    case While w -> 
                    {
                        qexpr.add(w.getCond());
                        if (w.getDo() != null) qstmt.add(w.getDo());
                    }
                    case For f -> 
                    {
                        if (f.getInit() != null) qstmt.add(f.getInit());
                        qexpr.add(f.getTo());
                        if (f.getDo() != null) qstmt.add(f.getDo()); }
                    case RepeatUntil ru -> 
                    {
                        if (ru.getRepeat() != null) qstmt.add(ru.getRepeat());
                        qexpr.add(ru.getUntil());
                    }
                    case Block b -> 
                    {
                        for (Statement child : b.getStmts()) if (child != null) qstmt.add(child);
                    }
                    case ProcedureDeclaration pd -> 
                    {
                        for (Expression init : pd.getVars().values()) 
                            if (init != null) qexpr.add(init);
                        if (pd.getStmt() != null) qstmt.add(pd.getStmt());
                    }
                    default -> 
                    {
                    } // Readln / Break / Continue / Comment: no expr children
                }
            }

            while (!qexpr.isEmpty()) 
            { // same for expr
                Expression e = qexpr.remove();

                switch (e) 
                {
                    case _String ss -> ret.add(ss.getVal());
                    case BinOp bo -> 
                    {
                        qexpr.add(bo.getExpr1());
                        qexpr.add(bo.getExpr2());
                    }
                    case ProcedureCall pc -> 
                    {
                        for (Expression arg : pc.getArgs())
                            if (arg != null) qexpr.add(arg);
                    }
                    case ArrayElement ae -> qexpr.add(ae.getIdx());
                    default -> 
                    {
                    } // Number / Boolean / Variable / Array
                }
            }
        }

        return ret;
    }

    /**
     * compiles an expression with emitter e
     * @param e expression to compile
     * @param em object to use to write
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Expression e, Emitter em) throws Throwable 
    {
        if (debug) em.emit("# begin expr " + e.getClass().getName() + "\n");
        switch (e) 
        {
            case Number n -> em.emit(
                    "li $v0, " + n.getVal() + "\n" 
                    );
            case Boolean b -> em.emit(
                    "li $v0, " + (b.getVal() ? 1 : 0) + "\n" 
                    );
            case _String ss -> 
            {
                int idx = strlits.indexOf(ss.getVal()); // ordered
                em.emit("la $v0, __strliteral" + idx + "\n");
            }
            case BinOp bo -> 
            {
                compile(bo.getExpr1(), em); // expr1 in $v0
                em.push(); // push $v0
                compile(bo.getExpr2(), em); // expr2 in $v0
                em.pop(); // $t0 = expr1, $v0 = expr2
                switch (bo.getOp()) 
                {
                    case "+" -> em.emit("addu $v0, $t0, $v0\n");
                    case "-" -> em.emit("subu $v0, $t0, $v0\n");
                    case "*" -> em.emit("multu $t0, $v0\nmflo $v0\n");
                    case "/" -> em.emit("divu $t0, $v0\nmflo $v0\n");
                    case "mod" -> em.emit("divu $t0, $v0\nmfhi $v0\n");
                    // v ret bool
                    case "<" -> em.emit("slt $v0, $t0, $v0\n");
                    case ">" -> em.emit("slt $v0, $v0, $t0\n");
                    case "=" -> em.emit("seq $v0, $t0, $v0\n");
                    case "<=" -> em.emit("sle $v0, $t0, $v0\n");
                    case ">=" -> em.emit("sle $v0, $v0, $t0\n");
                    case "<>" -> em.emit("sne $v0, $t0, $v0\n");
                    case "AND" -> em.emit("and $v0, $t0, $v0\n");
                    case "OR" -> em.emit("or $v0, $t0, $v0\n");
                    default -> throw new RuntimeException("do not recognize op: " + bo.getOp());
                }
            }
            case Variable v -> 
            {
                String vn = v.getName();
                if (em.isLocVar(vn)) 
                {
                    // local: is on stack
                    int ofst = em.getOffset(vn);
                    em.emit("lw $v0, " + ofst + "($sp)\n");
                }
                else 
                {
                    // global
                    // will always use indirect addr
                    em.emit(
                            "la $t0, __var" + vn + "\n" + 
                            "lw $v0 ($t0)\n"
                            );
                }
            }
            case ArrayElement ae -> 
            {
                String name = ae.getName();
                // addr of a[i] is base of stack + offset of arr itself + (idx - 1) * 4
                compile(ae.getIdx(), em); // idx -> $v0
                if (em.isLocVar(name)) 
                {
                    em.emit(
                            "subu $v0, $v0, 1\n" +   // idx - 1
                            "sll $v0, $v0, 2\n" +    // 4 * (idx - 1)
                            "li $t1, " + em.getOffset(name) + "\n" + // $t1 = offset
                            "addu $t1, $sp, $t1\n" + // $t1 = base + offset
                            "subu $t1, $t1, $v0\n" + // subtract because stack grows down
                            "lw $v0, ($t1)\n" // accumulate into $v0
                    );
                }
                else 
                {
                    em.emit(
                            "subu $v0, $v0, 1\n" + // idx - 1
                            "sll $v0, $v0, 2\n" + // 4 * (idx - 1)
                            "la $t0, __var" + name + "\n" + // base -> $t0, by the global var itself
                            "addu $t0, $t0, $v0\n" + // $t0 = base + 4 * (idx - 1)
                            "lw $v0, ($t0)\n" // accumulate into $v0
                    );
                }
                // too scared and confused to refactor this ^
            }
            case Array a -> {} // alr handled in compile(Program) .data section
            case ProcedureCall pc -> 
            {
                String lbl = "proc" + pc.getName();
                em.push("$ra"); // if is nested proc call not from main

                ArrayList<Expression> args = pc.getArgs();
                for(Expression arg : args) 
                {
                    compile(arg, em); // res in $v0
                    em.push(); // save args into stack: first arg is deepest
                }

                em.emit("jal " + lbl + "\n");

                // retrieve args then $ra
                for(int i = 0; i < args.size(); i++) em.pop();
                em.pop("$ra");

            }
            default -> 
                throw new RuntimeException(
                        "no expr in compile switched to: " + e.getClass().getSimpleName()
                );
        }
        if (debug) em.emit("# end expr " + e.getClass().getName() + "\n");
        // em.emit("\n");
    }
 
    /**
     * compiles a statement with emitter e
     * @param e statement to compile
     * @param em object to use to write
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Statement e, Emitter em) throws Throwable 
    {
        if (debug) em.emit("# begin stmt " + e.getClass().getName() + "\n");
        switch (e) 
        {
            case ProcedureDeclaration pd -> 
            {
                String lbl = "proc" + pd.getName();
                em.emit(lbl + ":\n");
                em.push("$zero"); // push ret var to stack as 0 init
                em.setProcContext(pd); // begin stack frame 

                int cnt = 0;
                for (Map.Entry<String, Expression> lcl : pd.getVars().entrySet())
                {
                    // for every local var, reserve corresponding badht in stack, tracked in emitter
                    int size = 
                        (lcl.getValue() instanceof Array a) ? (a.getEnd() - a.getStart() + 1) : 1;
                    em.addLcl(lcl.getKey(), size);
                    for (int i = 0; i < size; i++) em.push("$zero"); // initialize to 0
                    cnt += size;
                }

                compile(pd.getStmt(), em); // body

                // pop shallower local vars to then finally pop return val into $v0
                for (int i = 0; i < cnt; i++) em.pop(); 
                em.pop("$v0");

                em.emit("jr $ra\n");
                em.clearProcContext(); // close stack frame

            }
            case Writeln w -> 
            {
                compile(w.getExpression(), em); // expr -> $v0
                em.emit("move $a0, $v0\n"); // move to $a0 for syscall

                if ((w.getExpression() instanceof _String)) em.emit("li $v0, 4\n");
                else em.emit("li $v0, 1\n"); // normal int or bool

                em.emit(
                        "syscall\n" +
                        // emit newline, ascii code 10
                        "li $v0, 11\n" +
                        "li $a0, 10\n" +
                        "syscall\n"
                );
            }
            case Block b -> 
            {
                for(Statement s : b.getStmts()) if (s != null) compile(s, em);
            }
            case ArrayAssignment aa -> 
            {
                String vn = aa.getVar().getName(); // name of arr
                if (em.isLocVar(vn)) 
                {
                    // local
                    compile(aa.getExpression(), em); // assignee expr -> $v0
                    em.push(); 
                    compile(aa.getIdx(), em); // arr idx -> $v0
                    em.emit( // find loc = 
                            "subu $v0, $v0, 1\n" + // (idx - 1)
                            "sll $v0, $v0, 2\n" + // 4 * (idx - 1)
                            "li $t1, " + em.getOffset(vn) + "\n" + // offset -> $t1
                            "addu $t1, $sp, $t1\n" + // $t1 = offset + base
                            "subu $t1, $t1, $v0\n" // subtract location of idx bc stack grows down
                    );
                    em.pop(); // $t0 = val to assign
                    em.emit("sw $t0, ($t1)\n");
                }
                else 
                {
                    // global
                    compile(aa.getExpression(), em); 
                    em.push();
                    compile(aa.getIdx(), em);
                    em.emit(
                            "subu $v0, $v0, 1\n" +
                            "sll $v0, $v0, 2\n" + // 4 * (idx - 1)
                            "la $t1, __var" + vn + "\n" + // address of first byte of arr
                            "addu $t1, $t1, $v0\n" // add idx offset
                    );
                    em.pop(); // pop val to assign
                    em.emit("sw $t0, ($t1)\n");
                }
            }
            case Assignment a -> 
            {
                String vn = a.getVar().getName();
                compile(a.getExpression(), em); // expr in $v0
                if (em.isLocVar(vn)) em.emit("sw $v0, " + em.getOffset(vn) + "($sp)\n");
                else em.emit("la $t0, __var" + vn + "\nsw $v0, ($t0)\n");
            }
            case If i -> 
            {
                int lblid = em.nextLblId();
                String els = "else" + lblid, endif = "endif" + lblid;
                if (i.getElse() != null) 
                {
                    compile(i.getCond(), em, els);
                    compile(i.getThen(), em);
                    em.emit("j " + endif + "\n" + els + ":\n");
                    compile(i.getElse(), em);
                    em.emit(endif + ":\n");
                } 
                else 
                {
                    compile(i.getCond(), em, endif);
                    compile(i.getThen(), em);
                    em.emit(endif + ":\n");
                }
            }
            case While w -> 
            {
                int lblid = em.nextLblId();
                String whil = "while" + lblid, endwhile = "endwhile" + lblid;
                breaklbl.push(endwhile); 
                contlbl.push(whil);
                em.emit(whil + ":\n");
                compile(w.getCond(), em, endwhile);
                compile(w.getDo(), em);
                em.emit("j " + whil + "\n");
                em.emit(endwhile + ":\n");
                breaklbl.pop();
                contlbl.pop();
            }
            case For f -> 
            {
                int lblid = em.nextLblId();
                String fo = "for" + lblid, endfor = "endfor" + lblid, contfor = "contfor" + lblid;
                breaklbl.push(endfor);
                contlbl.push(contfor);
                compile(f.getInit(), em);
                em.emit(fo + ":\n");
                compile(new BinOp("<=", f.getVar(), f.getTo()), em, endfor); // inclusive pascal for
                compile(f.getDo(), em);
                em.emit(contfor + ":\n");
                // inc var
                String varn = "__var" + f.getVar().getName();
                em.emit(
                        "lw $t0, " + varn + "\n" +
                        "addi $t0, $t0, 1\n" +
                        "sw $t0, " + varn + "\n"
                );
                em.emit("j " + fo + "\n");
                em.emit(endfor + ":\n");
                breaklbl.pop();
                contlbl.pop();
            }
            case RepeatUntil ru -> 
            {
                int lblid = em.nextLblId();
                String rpt = "repeat" + lblid, endrpt = "endrpt" + lblid;
                breaklbl.push(endrpt);
                contlbl.push(rpt);
                em.emit(rpt + ":\n");
                compile(ru.getRepeat(), em);
                compile(ru.getUntil(), em, rpt);
                em.emit(endrpt + ":\n");
                breaklbl.pop();
                contlbl.pop();
            }
            case Readln rl -> 
            {
                // TODO: use rl.getType() once carries type info; default to int
                em.emit(
                        "li $v0, 5\n" +
                        "syscall\n" +
                        "la $t0, __var" + rl.getVar().getName() + "\n" +
                        "sw $v0, ($t0)\n"
                );
            }
            case Comment c -> 
            {
                return; 
            }
            case Break bk -> em.emit("j " + breaklbl.peek() + "\n"); 
            case Continue ct -> em.emit("j " + contlbl.peek() + "\n"); 
            default -> 
                throw new RuntimeException(
                        "no stmt in compile switched to: " + e.getClass().getSimpleName()
                );
        }
        if (debug) em.emit("# end stmt " + e.getClass().getName() + "\n");
        // em.emit("\n");
    }

    /**
     * compiles an expression with emitter e
     * jumps to label lbl if expression is false 
     * @param e expression to compile
     * @param em object to use to write
     * @param lbl label to jump to if expression is false
     * @throws Throwable idk why but it doesnt work otherwise
     */
    public void compile(Expression e, Emitter em, String lbl) throws Throwable 
    {
        if (debug) em.emit("# begin to lbl " + e.getClass().getName() + "\n");
        switch (e) 
        {
            case BinOp bo -> 
            {
                // if AND or OR, 
                if (bo.getOp().equals("AND") || bo.getOp().equals("OR")) 
                {
                    compile(bo, em); // eval expr to $v0
                    em.emit("beq $v0, $zero, " + lbl + "\n");
                    em.emit("# end to lbl binop\n");
                    return;
                }

                compile(bo.getExpr1(), em);
                em.push();
                compile(bo.getExpr2(), em);
                em.pop(); // $t0 = expr1, $v0 = expr2
                String inst = switch (bo.getOp()) 
                {
                    case "<" -> "bge";
                    case "<=" -> "bgt";
                    case ">" -> "ble";
                    case ">=" -> "blt";
                    case "<>" -> "beq";
                    case "=" -> "bne";
                    default -> throw new RuntimeException("not a relop: " + bo.getOp());
                };
                em.emit(inst + " $t0, $v0, " + lbl + "\n");
            }
            case Boolean b -> 
            {
                compile(b, em);
                em.emit("beq $v0, $zero, " + lbl + "\n");
            }
            case Variable v -> 
            { // todo local ? check compile var above
                compile(v, em);
                em.emit("beq $v0, $zero, " + lbl + "\n");
            }
            default -> 
            {
                // defualt eval to $v0 and branch if 0
                compile(e, em);
                em.emit("beq $v0, $zero, " + lbl + "\n");
            }
        }
        if (debug) em.emit("# end to lbl " + e.getClass().getName() + "\n");
        // em.emit("\n");
    }
}
