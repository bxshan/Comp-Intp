package ll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * LL(1) recursive-descent parser for the custom Pascal-like language.
 * <p>
 * The {@code Parser} receives a stream of {@link Token} objects from a
 * {@link Lexer} and builds an Abstract Syntax Tree (AST) rooted at a
 * {@link Program} node.  Each grammar rule is implemented as a private
 * {@code parse*} method that consumes the tokens it expects and returns the
 * corresponding AST node.
 * </p>
 *
 * <h2>Grammar summary</h2>
 * <pre>
 * program          → (proc_decl | statement)* EOF
 * proc_decl        → PROCEDURE ID ( param_list ) ; statement
 * statement        → WRITELN ( expression ) ;
 *                  | READLN ( ID ) ;
 *                  | BEGIN statement* END ;
 *                  | IF expression THEN statement ( ELSE statement )?
 *                  | WHILE expression DO statement
 *                  | REPEAT block UNTIL expression ;
 *                  | FOR ID := expression TO expression DO statement
 *                  | ID assignment_tail
 *                  | BREAK ;  |  CONTINUE ;  |  EXIT ;
 * assignment_tail  → := expression ;
 *                  | [ expression ] := expression ;
 *                  | ( arg_list ) ;
 * expression       → bool_expression
 * bool_expression  → bool_term ( OR bool_term )*
 * bool_term        → bool_factor ( AND bool_factor )*
 * bool_factor      → NOT bool_factor
 *                  | TRUE | FALSE
 *                  | primary_expression ( rel_op primary_expression )?
 * primary_expression → NUMBER | STRING | ID [ access ] | ( expression )
 *                    | - primary_expression
 * </pre>
 *
 * @author Boxuan Shan
 * @version 03242025
 * @see Lexer
 * @see Program
 */
public class Parser
{
    /** The lexer that supplies tokens on demand. */
    private Lexer lexer;

    /** The token most recently consumed from the lexer; the parser's look-ahead. */
    private Token currentToken;

    /**
     * Set of token types that represent relational (comparison) operators.
     * Used in {@link #parseBoolFactor()} to decide whether an arithmetic
     * sub-expression is followed by a comparison.
     */
    private static final Set<Token.TokenType> RELATIONAL_OPS = new HashSet<>(Arrays.asList(
            Token.TokenType.EQ, Token.TokenType.NE, Token.TokenType.LT,
            Token.TokenType.GT, Token.TokenType.LE, Token.TokenType.GE
    ));

    /**
     * Constructs a new {@code Parser} backed by the given {@link Lexer}.
     * <p>
     * The constructor immediately reads the first token from the lexer so that
     * {@link #currentToken} is always valid before any parse method is called.
     * </p>
     *
     * @param lexer the lexer that will supply tokens; must not be {@code null}
     */
    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
        advance();
    }

    /**
     * Advances {@link #currentToken} to the next token from the lexer.
     */
    private void advance()
    {
        currentToken = lexer.getNextToken();
    }

    /**
     * Verifies that {@link #currentToken} has the expected type and advances
     * past it.
     *
     * @param expectedType the {@link Token.TokenType} that the current token
     *                     must have
     * @throws RuntimeException if the current token's type does not match
     *                          {@code expectedType}
     */
    private void eat(Token.TokenType expectedType)
    {
        if (currentToken.type == expectedType)
        {
            advance();
        }
        else
        {
            throw new RuntimeException(
                "Expected " + expectedType
                + " but found " + currentToken.type
                + " at line " + currentToken.line);
        }
    }

    // --- Grammar Rules ---

    /**
     * Parses the top-level program.
     * <p>
     * Grammar: {@code program → (proc_decl | statement)* EOF}
     * </p>
     *
     * @return the root {@link Program} AST node containing all top-level
     *         statements and procedure declarations
     * @throws RuntimeException if a syntax error is encountered
     */
    public Program parseProgram()
    {
        ArrayList<Statement> statements = new ArrayList<>();
        while (currentToken.type != Token.TokenType.EOF)
        {
            if (currentToken.type == Token.TokenType.PROCEDURE)
            {
                statements.add(parseProcDecl());
            }
            else
            {
                statements.add(parseStatement());
            }
        }
        return new Program(statements);
    }

    /**
     * Parses a procedure (or function) declaration.
     * <p>
     * Grammar: {@code proc_decl → PROCEDURE ID ( param_list ) ; statement}
     * </p>
     *
     * @return a {@link ProcedureDeclaration} node with the procedure's name,
     *         formal parameter list, and body statement
     * @throws RuntimeException if a syntax error is encountered
     */
    private ProcedureDeclaration parseProcDecl()
    {
        eat(Token.TokenType.PROCEDURE);
        String name = currentToken.lexeme;
        eat(Token.TokenType.ID);
        eat(Token.TokenType.LPAREN);
        ArrayList<String> params = new ArrayList<>();
        if (currentToken.type == Token.TokenType.ID)
        {
            params.add(currentToken.lexeme);
            eat(Token.TokenType.ID);
            while (currentToken.type == Token.TokenType.COMMA)
            {
                eat(Token.TokenType.COMMA);
                params.add(currentToken.lexeme);
                eat(Token.TokenType.ID);
            }
        }
        eat(Token.TokenType.RPAREN);
        eat(Token.TokenType.SEMI);
        Statement body = parseStatement();
        return new ProcedureDeclaration(name, params, body);
    }

    /**
     * Parses a single statement.
     * <p>
     * Grammar:
     * <pre>
     * statement → WRITELN ( expression ) ;
     *           | READLN ( ID ) ;
     *           | BEGIN block END ;
     *           | IF expression THEN statement ( ELSE statement )?
     *           | WHILE expression DO statement
     *           | REPEAT block UNTIL expression ;
     *           | FOR ID := expression TO expression DO statement
     *           | ID assignment_tail
     *           | BREAK ;  |  CONTINUE ;  |  EXIT ;
     * </pre>
     * </p>
     * <p>
     * {@code EXIT} is treated as equivalent to {@code BREAK} and produces a
     * {@link Break} node.
     * </p>
     *
     * @return the AST {@link Statement} node for the parsed statement
     * @throws RuntimeException if the current token does not begin a valid
     *                          statement
     */
    private Statement parseStatement()
    {
        Statement stmt;
        switch (currentToken.type)
        {
            case WRITELN:
                eat(Token.TokenType.WRITELN);
                eat(Token.TokenType.LPAREN);
                Expression expr = parseExpression();
                eat(Token.TokenType.RPAREN);
                eat(Token.TokenType.SEMI);
                stmt = new Writeln(expr);
                break;
            case READLN:
                eat(Token.TokenType.READLN);
                eat(Token.TokenType.LPAREN);
                String varName = currentToken.lexeme;
                eat(Token.TokenType.ID);
                eat(Token.TokenType.RPAREN);
                eat(Token.TokenType.SEMI);
                stmt = new Readln(new Variable(varName));
                break;
            case BEGIN:
                stmt = parseBlock();
                break;
            case IF:
                eat(Token.TokenType.IF);
                Expression condition = parseExpression();
                eat(Token.TokenType.THEN);
                Statement thenStmt = parseStatement();
                Statement elseStmt = null;
                if (currentToken.type == Token.TokenType.ELSE)
                {
                    eat(Token.TokenType.ELSE);
                    elseStmt = parseStatement();
                }
                stmt = new If(condition, thenStmt, elseStmt);
                break;
            case WHILE:
                eat(Token.TokenType.WHILE);
                Expression whileCond = parseExpression();
                eat(Token.TokenType.DO);
                Statement whileBody = parseStatement();
                stmt = new While(whileCond, whileBody);
                break;
            case REPEAT:
                eat(Token.TokenType.REPEAT);
                Statement repeatBody = parseBlock();
                eat(Token.TokenType.UNTIL);
                Expression untilCond = parseExpression();
                eat(Token.TokenType.SEMI);
                stmt = new RepeatUntil(repeatBody, untilCond);
                break;
            case FOR:
                eat(Token.TokenType.FOR);
                String forVar = currentToken.lexeme;
                eat(Token.TokenType.ID);
                eat(Token.TokenType.ASSIGN);
                Expression initExpr = parseExpression();
                eat(Token.TokenType.TO);
                Expression toExpr = parseExpression();
                eat(Token.TokenType.DO);
                Statement forBody = parseStatement();
                stmt = new For(new Assignment(new Variable(forVar), initExpr), toExpr, forBody);
                break;
            case ID:
                String idName = currentToken.lexeme;
                eat(Token.TokenType.ID);
                stmt = parseAssignmentOrProcedureCallTail(idName);
                break;
            case BREAK:
                eat(Token.TokenType.BREAK);
                eat(Token.TokenType.SEMI);
                stmt = new Break();
                break;
            case CONTINUE:
                eat(Token.TokenType.CONTINUE);
                eat(Token.TokenType.SEMI);
                stmt = new Continue();
                break;
            case EXIT:
                eat(Token.TokenType.EXIT);
                eat(Token.TokenType.SEMI);
                stmt = new Break();
                break;
            case COMMENT:
                eat(Token.TokenType.COMMENT);
                stmt = new Comment();
                break;
            default:
                throw new RuntimeException(
                    "Unexpected token at start of statement: "
                    + currentToken.type + " at line " + currentToken.line);
        }
        return stmt;
    }

    /**
     * Parses the tail of an identifier-started statement, which can be a
     * simple assignment, an array element assignment, or a procedure call used
     * as a statement.
     * <p>
     * Grammar:
     * <pre>
     * assignment_tail → := expression ;
     *                 | [ expression ] := expression ;
     *                 | ( arg_list ) ;
     * </pre>
     * </p>
     * <p>
     * Procedure calls used as statements are wrapped in an
     * {@link Assignment} to the synthetic variable {@code __ignore} so that
     * the evaluator can discard the return value uniformly.
     * </p>
     *
     * @param idName the identifier lexeme already consumed before this method
     *               was called
     * @return an {@link Assignment} or {@link ArrayAssignment} statement node
     * @throws RuntimeException if neither {@code :=}, {@code [}, nor {@code (}
     *                          follows the identifier
     */
    private Statement parseAssignmentOrProcedureCallTail(String idName)
    {
        if (currentToken.type == Token.TokenType.ASSIGN)
        {
            eat(Token.TokenType.ASSIGN);
            Expression expr = parseExpression();
            eat(Token.TokenType.SEMI);
            return new Assignment(new Variable(idName), expr);
        }
        else if (currentToken.type == Token.TokenType.LBRACK)
        {
            eat(Token.TokenType.LBRACK);
            Expression indexExpr = parseExpression();
            eat(Token.TokenType.RBRACK);
            eat(Token.TokenType.ASSIGN);
            Expression valueExpr = parseExpression();
            eat(Token.TokenType.SEMI);
            return new ArrayAssignment(new Variable(idName), indexExpr, valueExpr);
        }
        else if (currentToken.type == Token.TokenType.LPAREN)
        {
            eat(Token.TokenType.LPAREN);
            ArrayList<Expression> args = parseArgList();
            eat(Token.TokenType.RPAREN);
            eat(Token.TokenType.SEMI);
            return new Assignment(new Variable("__ignore"), new ProcedureCall(idName, args));
        }
        else
        {
            throw new RuntimeException(
                "Unexpected token after ID in assignment or procedure call: "
                + currentToken.type + " at line " + currentToken.line);
        }
    }

    /**
     * Parses a {@code BEGIN … END ;} block of statements.
     * <p>
     * Grammar: {@code block → BEGIN statement* END ;}
     * </p>
     *
     * @return a {@link Block} node containing all parsed statements
     * @throws RuntimeException if a syntax error is encountered inside the block
     */
    private Block parseBlock()
    {
        eat(Token.TokenType.BEGIN);
        ArrayList<Statement> statements = new ArrayList<>();
        while (currentToken.type != Token.TokenType.END)
        {
            statements.add(parseStatement());
        }
        eat(Token.TokenType.END);
        eat(Token.TokenType.SEMI);
        return new Block(statements);
    }

    /**
     * Parses a general expression, delegating to the boolean expression rule.
     * <p>
     * Grammar: {@code expression → bool_expression}
     * </p>
     *
     * @return the root {@link Expression} node of the parsed expression
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseExpression()
    {
        return parseBoolExpression();
    }

    /**
     * Parses a boolean expression composed of OR-connected boolean terms.
     * <p>
     * Grammar: {@code bool_expression → bool_term ( OR bool_term )*}
     * </p>
     *
     * @return the root {@link Expression} node; a chain of {@link BinOp}
     *         nodes with operator {@code "OR"} if more than one term is present
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseBoolExpression()
    {
        Expression left = parseBoolTerm();
        while (currentToken.type == Token.TokenType.OR)
        {
            eat(Token.TokenType.OR);
            Expression right = parseBoolTerm();
            left = new BinOp("OR", left, right);
        }
        return left;
    }

    /**
     * Parses a boolean term composed of AND-connected boolean factors.
     * <p>
     * Grammar: {@code bool_term → bool_factor ( AND bool_factor )*}
     * </p>
     *
     * @return the root {@link Expression} node; a chain of {@link BinOp}
     *         nodes with operator {@code "AND"} if more than one factor is present
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseBoolTerm()
    {
        Expression left = parseBoolFactor();
        while (currentToken.type == Token.TokenType.AND)
        {
            eat(Token.TokenType.AND);
            Expression right = parseBoolFactor();
            left = new BinOp("AND", left, right);
        }
        return left;
    }

    /**
     * Parses a boolean factor, which may be a negation, a boolean literal,
     * or a primary expression optionally followed by a relational operator.
     * <p>
     * Grammar:
     * <pre>
     * bool_factor → NOT bool_factor
     *             | ( bool_expression )
     *             | TRUE | FALSE
     *             | primary_expression ( rel_op primary_expression )?
     * </pre>
     * </p>
     * <p>
     * {@code NOT} is represented as a {@link BinOp} with operator {@code "NOT"}
     * and a dummy left operand of {@code false}.
     * </p>
     *
     * @return the parsed boolean {@link Expression} node
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseBoolFactor()
    {
        Expression result;
        if (currentToken.type == Token.TokenType.NOT)
        {
            eat(Token.TokenType.NOT);
            Expression factor = parseBoolFactor();
            result = new BinOp("NOT", new Boolean(false), factor);
        }
        else if (currentToken.type == Token.TokenType.LPAREN)
        {
            eat(Token.TokenType.LPAREN);
            result = parseBoolExpression();
            eat(Token.TokenType.RPAREN);
        }
        else if (currentToken.type == Token.TokenType.TRUE)
        {
            eat(Token.TokenType.TRUE);
            result = new Boolean(true);
        }
        else if (currentToken.type == Token.TokenType.FALSE)
        {
            eat(Token.TokenType.FALSE);
            result = new Boolean(false);
        }
        else
        {
            Expression left = parsePrimaryExpression();
            if (RELATIONAL_OPS.contains(currentToken.type))
            {
                String op = currentToken.lexeme;
                eat(currentToken.type);
                Expression right = parsePrimaryExpression();
                result = new BinOp(op, left, right);
            }
            else
            {
                result = left;
            }
        }
        return result;
    }

    /**
     * Parses a primary expression — the highest-precedence expression construct.
     * <p>
     * Grammar:
     * <pre>
     * primary_expression → NUMBER
     *                    | STRING
     *                    | ID ( [ expression ] | ( arg_list ) | ε )
     *                    | ( expression )
     *                    | - primary_expression
     * </pre>
     * </p>
     * <p>
     * Unary minus is lowered to {@code 0 - expr} using a {@link BinOp} node.
     * </p>
     *
     * @return the parsed {@link Expression} node
     * @throws RuntimeException if the current token cannot begin a primary
     *                          expression
     */
    private Expression parsePrimaryExpression()
    {
        Expression expr;
        if (currentToken.type == Token.TokenType.NUMBER)
        {
            expr = new Number((Integer) currentToken.literal);
            eat(Token.TokenType.NUMBER);
        }
        else if (currentToken.type == Token.TokenType.STRING)
        {
            expr = new SString((String) currentToken.literal);
            eat(Token.TokenType.STRING);
        }
        else if (currentToken.type == Token.TokenType.ID)
        {
            String name = currentToken.lexeme;
            eat(Token.TokenType.ID);
            if (currentToken.type == Token.TokenType.LBRACK)
            {
                eat(Token.TokenType.LBRACK);
                Expression index = parseExpression();
                eat(Token.TokenType.RBRACK);
                expr = new ArrayElement(name, index);
            }
            else if (currentToken.type == Token.TokenType.LPAREN)
            {
                eat(Token.TokenType.LPAREN);
                ArrayList<Expression> args = parseArgList();
                eat(Token.TokenType.RPAREN);
                expr = new ProcedureCall(name, args);
            }
            else
            {
                expr = new Variable(name);
            }
        }
        else if (currentToken.type == Token.TokenType.LPAREN)
        {
            eat(Token.TokenType.LPAREN);
            expr = parseExpression();
            eat(Token.TokenType.RPAREN);
        }
        else if (currentToken.type == Token.TokenType.MINUS)
        {
            eat(Token.TokenType.MINUS);
            Expression subExpr = parsePrimaryExpression();
            expr = new BinOp("-", new Number(0), subExpr);
        }
        else
        {
            throw new RuntimeException(
                "Unexpected token at primary expression: "
                + currentToken.type + " at line " + currentToken.line);
        }
        return expr;
    }

    /**
     * Parses a numeric expression of additive precedence.
     * <p>
     * Grammar: {@code numeric_expression → term ( ( + | - ) term )*}
     * </p>
     *
     * @return the root {@link Expression} node for the numeric expression
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseNumericExpression()
    {
        Expression left = parseTerm();
        while (currentToken.type == Token.TokenType.PLUS
                || currentToken.type == Token.TokenType.MINUS)
        {
            String op = currentToken.lexeme;
            eat(currentToken.type);
            Expression right = parseTerm();
            left = new BinOp(op, left, right);
        }
        return left;
    }

    /**
     * Parses a term of multiplicative precedence.
     * <p>
     * Grammar: {@code term → factor ( ( * | / | mod ) factor )*}
     * </p>
     *
     * @return the root {@link Expression} node for the term
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseTerm()
    {
        Expression left = parseFactor();
        while (currentToken.type == Token.TokenType.STAR
                || currentToken.type == Token.TokenType.SLASH
                || currentToken.type == Token.TokenType.MOD)
        {
            String op = currentToken.lexeme;
            eat(currentToken.type);
            Expression right = parseFactor();
            left = new BinOp(op, left, right);
        }
        return left;
    }

    /**
     * Parses a factor — the highest-precedence arithmetic construct.
     * <p>
     * Grammar:
     * <pre>
     * factor → NUMBER
     *        | STRING
     *        | ID ( [ expression ] | ( arg_list ) | ε )
     *        | ( expression )
     *        | - factor
     * </pre>
     * </p>
     * <p>
     * Unary minus is lowered to {@code 0 - expr} using a {@link BinOp} node.
     * </p>
     *
     * @return the parsed {@link Expression} node for the factor
     * @throws RuntimeException if the current token cannot begin a factor
     */
    private Expression parseFactor()
    {
        Expression expr;
        if (currentToken.type == Token.TokenType.NUMBER)
        {
            expr = new Number((Integer) currentToken.literal);
            eat(Token.TokenType.NUMBER);
        }
        else if (currentToken.type == Token.TokenType.ID)
        {
            String name = currentToken.lexeme;
            eat(Token.TokenType.ID);
            if (currentToken.type == Token.TokenType.LBRACK)
            {
                eat(Token.TokenType.LBRACK);
                Expression index = parseExpression();
                eat(Token.TokenType.RBRACK);
                expr = new ArrayElement(name, index);
            }
            else if (currentToken.type == Token.TokenType.LPAREN)
            {
                eat(Token.TokenType.LPAREN);
                ArrayList<Expression> args = parseArgList();
                eat(Token.TokenType.RPAREN);
                expr = new ProcedureCall(name, args);
            }
            else
            {
                expr = new Variable(name);
            }
        }
        else if (currentToken.type == Token.TokenType.LPAREN)
        {
            eat(Token.TokenType.LPAREN);
            expr = parseExpression();
            eat(Token.TokenType.RPAREN);
        }
        else if (currentToken.type == Token.TokenType.MINUS)
        {
            eat(Token.TokenType.MINUS);
            Expression subExpr = parseFactor();
            expr = new BinOp("-", new Number(0), subExpr);
        }
        else if (currentToken.type == Token.TokenType.STRING)
        {
            expr = new SString((String) currentToken.literal);
            eat(Token.TokenType.STRING);
        }
        else
        {
            throw new RuntimeException(
                "Unexpected token at factor: "
                + currentToken.type + " at line " + currentToken.line);
        }
        return expr;
    }

    /**
     * Parses a string expression of concatenation (additive) precedence.
     * <p>
     * Grammar: {@code str_expression → str_term ( + str_term )*}
     * </p>
     *
     * @return the root {@link Expression} node for the string expression
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseStrExpression()
    {
        Expression left = parseStrTerm();
        while (currentToken.type == Token.TokenType.PLUS)
        {
            String op = currentToken.lexeme;
            eat(Token.TokenType.PLUS);
            Expression right = parseStrTerm();
            left = new BinOp(op, left, right);
        }
        return left;
    }

    /**
     * Parses a string term, delegating directly to {@link #parseStrFactor()}.
     * <p>
     * Grammar: {@code str_term → str_factor}
     * </p>
     *
     * @return the {@link Expression} node returned by {@link #parseStrFactor()}
     * @throws RuntimeException if a syntax error is encountered
     */
    private Expression parseStrTerm()
    {
        return parseStrFactor();
    }

    /**
     * Parses a string factor — a string literal, identifier, array element,
     * procedure call, or parenthesised string expression.
     * <p>
     * Grammar:
     * <pre>
     * str_factor → STRING
     *            | ID ( [ expression ] | ( arg_list ) | ε )
     *            | ( str_expression )
     * </pre>
     * </p>
     *
     * @return the parsed string {@link Expression} node
     * @throws RuntimeException if the current token cannot begin a string factor
     */
    private Expression parseStrFactor()
    {
        Expression expr;
        if (currentToken.type == Token.TokenType.STRING)
        {
            expr = new SString((String) currentToken.literal);
            eat(Token.TokenType.STRING);
        }
        else if (currentToken.type == Token.TokenType.ID)
        {
            String name = currentToken.lexeme;
            eat(Token.TokenType.ID);
            if (currentToken.type == Token.TokenType.LBRACK)
            {
                eat(Token.TokenType.LBRACK);
                Expression index = parseExpression();
                eat(Token.TokenType.RBRACK);
                expr = new ArrayElement(name, index);
            }
            else if (currentToken.type == Token.TokenType.LPAREN)
            {
                eat(Token.TokenType.LPAREN);
                ArrayList<Expression> args = parseArgList();
                eat(Token.TokenType.RPAREN);
                expr = new ProcedureCall(name, args);
            }
            else
            {
                expr = new Variable(name);
            }
        }
        else if (currentToken.type == Token.TokenType.LPAREN)
        {
            eat(Token.TokenType.LPAREN);
            expr = parseStrExpression();
            eat(Token.TokenType.RPAREN);
        }
        else
        {
            throw new RuntimeException(
                "Unexpected token at string factor: "
                + currentToken.type + " at line " + currentToken.line);
        }
        return expr;
    }

    /**
     * Parses a comma-separated list of actual arguments for a procedure or
     * function call.
     * <p>
     * Grammar: {@code arg_list → ( expression ( , expression )* )? }
     * </p>
     * <p>
     * Returns an empty list if the next token is {@code )} (i.e., no arguments).
     * </p>
     *
     * @return an {@link ArrayList} of {@link Expression} nodes, one per argument;
     *         empty if the argument list is absent
     * @throws RuntimeException if a syntax error is encountered within the list
     */
    private ArrayList<Expression> parseArgList()
    {
        ArrayList<Expression> args = new ArrayList<>();
        if (currentToken.type != Token.TokenType.RPAREN)
        {
            args.add(parseExpression());
            while (currentToken.type == Token.TokenType.COMMA)
            {
                eat(Token.TokenType.COMMA);
                args.add(parseExpression());
            }
        }
        return args;
    }
}
