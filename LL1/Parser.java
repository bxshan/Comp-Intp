package LL1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        advance(); // Initialize currentToken
    }

    // Advances the currentToken to the next token from the lexer
    private void advance() {
        currentToken = lexer.getNextToken();
        // System.out.println("Advanced to: " + currentToken); // For debugging
    }

    // Consumes the currentToken if its type matches the expectedType
    private void eat(Token.TokenType expectedType) {
        if (currentToken.type == expectedType) {
            advance();
        } else {
            throw new RuntimeException("Expected " + expectedType + " but found " + currentToken.type +
                                       " at line " + currentToken.line);
        }
    }

    // --- Grammar Rules ---

    // program -> (statement | proc_decl)* EOF
    public Program parseProgram() {
        ArrayList<Statement> statements = new ArrayList<>();
        while (currentToken.type != Token.TokenType.EOF) {
            if (currentToken.type == Token.TokenType.PROCEDURE) {
                statements.add(parseProcDecl());
            } else {
                statements.add(parseStatement());
            }
        }
        return new Program(statements);
    }

    // proc_decl -> PROCEDURE ID ( param_list ) ; statement
    private ProcedureDeclaration parseProcDecl() {
        eat(Token.TokenType.PROCEDURE);
        String name = currentToken.lexeme;
        eat(Token.TokenType.ID);
        eat(Token.TokenType.LPAREN);
        ArrayList<String> params = new ArrayList<>();
        if (currentToken.type == Token.TokenType.ID) {
            params.add(currentToken.lexeme);
            eat(Token.TokenType.ID);
            while (currentToken.type == Token.TokenType.COMMA) {
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

    // statement -> WRITELN ( expression ) ;
    //            | READLN ( ID ) ;
    //            | BEGIN block END ;
    //            | IF bool_expression THEN statement ( ELSE statement )?
    //            | WHILE bool_expression DO statement
    //            | REPEAT block UNTIL bool_expression ;
    //            | FOR ID := expression TO expression DO statement
    //            | ID assignment_tail
    //            | BREAK ;
    //            | CONTINUE ;
    //            | EXIT ;
    //            | COMMENT ;
    private Statement parseStatement() {
        Statement stmt;
        switch (currentToken.type) {
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
                Expression condition = parseExpression(); // Should be boolean
                eat(Token.TokenType.THEN);
                Statement thenStmt = parseStatement();
                Statement elseStmt = null;
                if (currentToken.type == Token.TokenType.ELSE) {
                    eat(Token.TokenType.ELSE);
                    elseStmt = parseStatement();
                }
                stmt = new If(condition, thenStmt, elseStmt);
                break;
            case WHILE:
                eat(Token.TokenType.WHILE);
                Expression whileCondition = parseExpression(); // Should be boolean
                eat(Token.TokenType.DO);
                Statement whileDoStmt = parseStatement();
                stmt = new While(whileCondition, whileDoStmt);
                break;
            case REPEAT:
                eat(Token.TokenType.REPEAT);
                Statement repeatStmt = parseBlock(); // Block of statements
                eat(Token.TokenType.UNTIL);
                Expression untilCondition = parseExpression(); // Should be boolean
                eat(Token.TokenType.SEMI);
                stmt = new RepeatUntil(repeatStmt, untilCondition);
                break;
            case FOR:
                eat(Token.TokenType.FOR);
                String forVarName = currentToken.lexeme;
                eat(Token.TokenType.ID);
                eat(Token.TokenType.ASSIGN); // :=
                Expression initExpr = parseExpression();
                eat(Token.TokenType.TO);
                Expression toExpr = parseExpression();
                eat(Token.TokenType.DO);
                Statement forDoStmt = parseStatement();
                stmt = new For(new Assignment(new Variable(forVarName), initExpr), toExpr, forDoStmt);
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
            case EXIT: // Treated as BREAK in the original parser
                eat(Token.TokenType.EXIT);
                eat(Token.TokenType.SEMI);
                stmt = new Break(); // Create a Break AST node for EXIT
                break;
            case COMMENT: // Lexer already handles removing, just consume token
                eat(Token.TokenType.COMMENT);
                // Comments are handled as a no-op statement in the original parser
                stmt = new Comment();
                break;
            default:
                throw new RuntimeException("Unexpected token at start of statement: " + currentToken.type +
                                           " at line " + currentToken.line);
        }
        return stmt;
    }

    // assignment_tail -> := expression ;
    //                 | [ expression ] := expression ;
    //                 | ( arg_list ) ; (for procedure call as statement)
    private Statement parseAssignmentOrProcedureCallTail(String idName) {
        if (currentToken.type == Token.TokenType.ASSIGN) { // :=
            eat(Token.TokenType.ASSIGN);
            Expression expr = parseExpression();
            eat(Token.TokenType.SEMI);
            return new Assignment(new Variable(idName), expr);
        } else if (currentToken.type == Token.TokenType.LBRACK) { // [
            eat(Token.TokenType.LBRACK);
            Expression indexExpr = parseExpression();
            eat(Token.TokenType.RBRACK);
            eat(Token.TokenType.ASSIGN); // :=
            Expression valueExpr = parseExpression();
            eat(Token.TokenType.SEMI);
            return new ArrayAssignment(new Variable(idName), indexExpr, valueExpr);
        } else if (currentToken.type == Token.TokenType.LPAREN) { // ( for procedure call
            eat(Token.TokenType.LPAREN);
            ArrayList<Expression> args = parseArgList();
            eat(Token.TokenType.RPAREN);
            eat(Token.TokenType.SEMI);
            // The original parser handles procedure calls as statements by assigning to __ignore
            // For now, we'll just create a ProcedureCall expression.
            // If the evaluator expects a statement, this needs wrapping.
            // Based on procedures/Parser.java: it makes an Assignment to "__ignore" variable.
            return new Assignment(new Variable("__ignore"), new ProcedureCall(idName, args));
        } else {
            throw new RuntimeException("Unexpected token after ID in assignment or procedure call: " +
                                       currentToken.type + " at line " + currentToken.line);
        }
    }

    // block -> BEGIN statement* END ;
    private Block parseBlock() {
        eat(Token.TokenType.BEGIN);
        ArrayList<Statement> statements = new ArrayList<>();
        while (currentToken.type != Token.TokenType.END) {
            statements.add(parseStatement());
        }
        eat(Token.TokenType.END);
        eat(Token.TokenType.SEMI);
        return new Block(statements);
    }

    // expression -> bool_expression
    private Expression parseExpression() {
        return parseBoolExpression();
    }

    // bool_expression -> bool_term ( OR bool_term )*
    private Expression parseBoolExpression() {
        Expression left = parseBoolTerm();
        while (currentToken.type == Token.TokenType.OR) {
            eat(Token.TokenType.OR);
            Expression right = parseBoolTerm();
            left = new BinOp("OR", left, right);
        }
        return left;
    }

    // bool_term -> bool_factor ( AND bool_factor )*
    private Expression parseBoolTerm() {
        Expression left = parseBoolFactor();
        while (currentToken.type == Token.TokenType.AND) {
            eat(Token.TokenType.AND);
            Expression right = parseBoolFactor();
            left = new BinOp("AND", left, right);
        }
        return left;
    }
    
    private static final Set<Token.TokenType> RELATIONAL_OPS = new HashSet<>(Arrays.asList(
            Token.TokenType.EQ, Token.TokenType.NE, Token.TokenType.LT, Token.TokenType.GT, Token.TokenType.LE, Token.TokenType.GE
    ));

    // bool_factor -> NOT bool_factor
    //              | ( bool_expression )
    //              | TRUE | FALSE
    //              | primary_expression ( rel_op primary_expression )?
    //              (The `primary_expression` must resolve to boolean if not followed by rel_op)
    private Expression parseBoolFactor() {
        if (currentToken.type == Token.TokenType.NOT) {
            eat(Token.TokenType.NOT);
            Expression factor = parseBoolFactor();
            return new BinOp("NOT", new Boolean(false), factor); // "NOT" as a binary op placeholder
        } else if (currentToken.type == Token.TokenType.LPAREN) {
            eat(Token.TokenType.LPAREN);
            Expression expr = parseBoolExpression();
            eat(Token.TokenType.RPAREN);
            return expr;
        } else if (currentToken.type == Token.TokenType.TRUE) {
            eat(Token.TokenType.TRUE);
            return new Boolean(true);
        } else if (currentToken.type == Token.TokenType.FALSE) {
            eat(Token.TokenType.FALSE);
            return new Boolean(false);
        } else {
            // This could be the start of a numeric, string, or boolean primary expression.
            // We parse a primary expression and then check if a relational operator follows.
            Expression left = parsePrimaryExpression();

            if (RELATIONAL_OPS.contains(currentToken.type)) {
                String op = currentToken.lexeme;
                eat(currentToken.type);
                Expression right = parsePrimaryExpression();
                return new BinOp(op, left, right);
            }
            return left; // It's a boolean variable, array element or procedure call that returns boolean
        }
    }

    // primary_expression -> NUMBER
    //                     | STRING
    //                     | ID ( [ expression ] | ( arg_list ) | ε )
    //                     | ( expression )
    //                     | MINUS primary_expression (for unary minus on numbers)
    private Expression parsePrimaryExpression() {
        Expression expr;
        if (currentToken.type == Token.TokenType.NUMBER) {
            expr = new Number((Integer) currentToken.literal);
            eat(Token.TokenType.NUMBER);
        } else if (currentToken.type == Token.TokenType.STRING) {
            expr = new SString((String) currentToken.literal);
            eat(Token.TokenType.STRING);
        } else if (currentToken.type == Token.TokenType.ID) {
            String name = currentToken.lexeme;
            eat(Token.TokenType.ID);
            if (currentToken.type == Token.TokenType.LBRACK) { // Array element
                eat(Token.TokenType.LBRACK);
                Expression index = parseExpression();
                eat(Token.TokenType.RBRACK);
                expr = new ArrayElement(name, index);
            } else if (currentToken.type == Token.TokenType.LPAREN) { // Procedure call
                eat(Token.TokenType.LPAREN);
                ArrayList<Expression> args = parseArgList();
                eat(Token.TokenType.RPAREN);
                expr = new ProcedureCall(name, args);
            } else { // Variable
                expr = new Variable(name);
            }
        } else if (currentToken.type == Token.TokenType.LPAREN) {
            eat(Token.TokenType.LPAREN);
            expr = parseExpression();
            eat(Token.TokenType.RPAREN);
        } else if (currentToken.type == Token.TokenType.MINUS) {
            eat(Token.TokenType.MINUS);
            Expression subExpr = parsePrimaryExpression();
            expr = new BinOp("-", new Number(0), subExpr); // Represent unary minus as 0 - expr
        } else {
            throw new RuntimeException("Unexpected token at primary expression: " + currentToken.type +
                                       " at line " + currentToken.line);
        }
        return expr;
    }


    // numeric_expression -> term ( ( PLUS | MINUS ) term )*
    private Expression parseNumericExpression() {
        Expression left = parseTerm();
        while (currentToken.type == Token.TokenType.PLUS || currentToken.type == Token.TokenType.MINUS) {
            String op = currentToken.lexeme;
            eat(currentToken.type);
            Expression right = parseTerm();
            left = new BinOp(op, left, right);
        }
        return left;
    }

    // term -> factor ( ( STAR | SLASH | MOD ) factor )*
    private Expression parseTerm() {
        Expression left = parseFactor();
        while (currentToken.type == Token.TokenType.STAR ||
               currentToken.type == Token.TokenType.SLASH ||
               currentToken.type == Token.TokenType.MOD) {
            String op = currentToken.lexeme;
            eat(currentToken.type);
            Expression right = parseFactor();
            left = new BinOp(op, left, right);
        }
        return left;
    }

    // factor -> NUMBER
    //         | ID
    //         | ID [ expression ] (array element)
    //         | ID ( arg_list ) (procedure call returning a value)
    //         | ( expression )
    //         | MINUS factor (unary minus)
    //         | STRING (String literal)
    private Expression parseFactor() {
        Expression expr;
        if (currentToken.type == Token.TokenType.NUMBER) {
            expr = new Number((Integer) currentToken.literal);
            eat(Token.TokenType.NUMBER);
        } else if (currentToken.type == Token.TokenType.ID) {
            String name = currentToken.lexeme;
            eat(Token.TokenType.ID);
            if (currentToken.type == Token.TokenType.LBRACK) { // Array element
                eat(Token.TokenType.LBRACK);
                Expression index = parseExpression();
                eat(Token.TokenType.RBRACK);
                expr = new ArrayElement(name, index);
            } else if (currentToken.type == Token.TokenType.LPAREN) { // Procedure call
                eat(Token.TokenType.LPAREN);
                ArrayList<Expression> args = parseArgList();
                eat(Token.TokenType.RPAREN);
                expr = new ProcedureCall(name, args);
            } else { // Variable
                expr = new Variable(name);
            }
        } else if (currentToken.type == Token.TokenType.LPAREN) {
            eat(Token.TokenType.LPAREN);
            expr = parseExpression();
            eat(Token.TokenType.RPAREN);
        } else if (currentToken.type == Token.TokenType.MINUS) {
            eat(Token.TokenType.MINUS);
            Expression subExpr = parseFactor();
            expr = new BinOp("-", new Number(0), subExpr); // Represent unary minus as 0 - expr
        } else if (currentToken.type == Token.TokenType.STRING) {
            expr = new SString((String) currentToken.literal);
            eat(Token.TokenType.STRING);
        }
        else {
            throw new RuntimeException("Unexpected token at factor: " + currentToken.type +
                                       " at line " + currentToken.line);
        }
        return expr;
    }

    // str_expression -> str_term ( PLUS str_term )*
    private Expression parseStrExpression() {
        Expression left = parseStrTerm();
        while (currentToken.type == Token.TokenType.PLUS) {
            String op = currentToken.lexeme;
            eat(Token.TokenType.PLUS);
            Expression right = parseStrTerm();
            left = new BinOp(op, left, right);
        }
        return left;
    }

    // str_term -> str_factor
    private Expression parseStrTerm() {
        return parseStrFactor();
    }

    // str_factor -> STRING
    //             | ID
    //             | ID [ expression ] (string array element)
    //             | ( str_expression )
    private Expression parseStrFactor() {
        Expression expr;
        if (currentToken.type == Token.TokenType.STRING) {
            expr = new SString((String) currentToken.literal);
            eat(Token.TokenType.STRING);
        } else if (currentToken.type == Token.TokenType.ID) {
            String name = currentToken.lexeme;
            eat(Token.TokenType.ID);
            if (currentToken.type == Token.TokenType.LBRACK) { // Array element
                eat(Token.TokenType.LBRACK);
                Expression index = parseExpression();
                eat(Token.TokenType.RBRACK);
                expr = new ArrayElement(name, index);
            } else if (currentToken.type == Token.TokenType.LPAREN) { // Procedure call returning string
                eat(Token.TokenType.LPAREN);
                ArrayList<Expression> args = parseArgList();
                eat(Token.TokenType.RPAREN);
                expr = new ProcedureCall(name, args);
            } else { // String variable
                expr = new Variable(name);
            }
        } else if (currentToken.type == Token.TokenType.LPAREN) {
            eat(Token.TokenType.LPAREN);
            expr = parseStrExpression();
            eat(Token.TokenType.RPAREN);
        } else {
            throw new RuntimeException("Unexpected token at string factor: " + currentToken.type +
                                       " at line " + currentToken.line);
        }
        return expr;
    }

    // arg_list -> expression ( , expression )* | ε
    private ArrayList<Expression> parseArgList() {
        ArrayList<Expression> args = new ArrayList<>();
        if (currentToken.type != Token.TokenType.RPAREN) { // Check if not empty list
            args.add(parseExpression());
            while (currentToken.type == Token.TokenType.COMMA) {
                eat(Token.TokenType.COMMA);
                args.add(parseExpression());
            }
        }
        return args;
    }
}