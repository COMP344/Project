import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private final List<IToken> tokens;
    private int current;
    private static String current_module_name;
    private static String current_procedure_name;

    Parser(List<IToken> tokens) {
        this.tokens = tokens;
    }

    List<Decl> parse() {
        List<Decl> declarations = new ArrayList<>();

        while(!isAtEnd()) {
             declarations.add(moduleDeclaration());
        }

        return declarations;
    }

    private Decl moduleDeclaration() {
        consume(Token.TokenType.MODULE, "Expected 'MODULE' keyword");
        Mod.Header header = moduleHeader();
        Mod.Body body = moduleBody();
        return new Decl.Module(header, body);
    }

    private boolean endOfModule() {
        IToken end_name = consume(Token.TokenType.IDENT, "Expect '" + current_module_name +"'");
        assert end_name != null;
        return end_name.getLexeme().equals(current_module_name);
    }

    private boolean endOfProcedure() {
        IToken end_name = consume(Token.TokenType.IDENT, "Expect '" + current_procedure_name +"'");
        assert end_name != null;
        return end_name.getLexeme().equals(current_procedure_name);
    }

    private Mod.Header moduleHeader() {
        IToken name = consume(Token.TokenType.IDENT, "Expect Module name");
        assert name != null;
        current_module_name = name.getLexeme();
        consume(Token.TokenType.SEMICOLON, "Expected ';' after module header");
        return new Mod.Header(name);
    }

    private Mod.Body moduleBody() {
        Decl.Const const_declaration = null;
        List<Decl.Var> variable_declarations = new ArrayList<>();
        List<Decl.Procedure> procedure_declaration = new ArrayList<>();
        Stmt.Seq statement_sequence = null;

        if (match(Token.TokenType.CONST)) {
            const_declaration = constantDeclaration();
        }
        while (nextIsType()) {
            variable_declarations.add(variableDeclaration());
        }
        while (match(Token.TokenType.PROCED)) {
            procedure_declaration.add(procedureDeclaration());
        }
        if (match(Token.TokenType.BEGIN)) {
            statement_sequence = statementSequence();
        }
        consume(Token.TokenType.END, "Expect 'END'");
        if (endOfModule()) {
            consume(Token.TokenType.PERIOD, "Expect a '.'");
            return new Mod.Body(const_declaration,variable_declarations,procedure_declaration,statement_sequence);
        } else {
            //error
            return null;
        }
    }

    private boolean nextIsType() {
        return check(Token.TokenType.INT) || check(Token.TokenType.SET) || check(Token.TokenType.BOOL);
    }

    private Decl.Const constantDeclaration() {
        Map<IToken, Expr> assignList = new HashMap<>();
        IToken ident;
        Expr value;

        while (check(Token.TokenType.IDENT)) {
            ident = ident();
            consume(Token.TokenType.EQL, "Expect '='.");
            value = expression();
            assignList.put(ident, value);
            consume(Token.TokenType.SEMICOLON, "Expect ';' after a constant declared");
        }

        return new Decl.Const(assignList);
    }

    private Decl.Var variableDeclaration() {
        IToken type = type();
        List<IToken> variables = new ArrayList<>();

        if (check(Token.TokenType.IDENT)) {
            do {
                variables.add(ident());
            } while (match(Token.TokenType.COMMA));
        }

        consume(Token.TokenType.SEMICOLON, "Expected ';' after variable declaration");
        return new Decl.Var(type, variables);
    }

    private Decl.Procedure procedureDeclaration() {
        Proc.Header header = procedureHeader();
        consume(Token.TokenType.SEMICOLON, "Expected ';' between head and body");
        Proc.Body body = procedureBody();
        return new Decl.Procedure(header, body);
    }

    private Proc.Header procedureHeader() {
        IToken name = ident();
        assert name != null;
        current_procedure_name = name.getLexeme();
        Proc.Param param = null;
        IToken return_type = null;
        if (match(Token.TokenType.LPAREN)) {
            param = formalParameter();
            consume(Token.TokenType.RPAREN, "Expect ')' after parameter.");
        }
        if (match(Token.TokenType.COLON)) {
            return_type = type();
        }
        return new Proc.Header(name, param, return_type);
    }

    private Proc.Param formalParameter() {
        IToken type = type();
        IToken name = ident();
        return new Proc.Param(name, type);
    }

    private IToken ident() {
        return consume(Token.TokenType.IDENT, "Expected an identity.");
    }

    private IToken type() {
        if (match(Token.TokenType.INT, Token.TokenType.SET, Token.TokenType.BOOL)) {
            return previous();
        } else {
            return null;
        }
    }

    private Proc.Body procedureBody() {
        List<Decl.Var> variable_declarations = new ArrayList<>();
        Stmt.Seq stmts = null;
        Expr return_expr = null;

        while (nextIsType()) {
            variable_declarations.add(variableDeclaration());
        }
        if (match(Token.TokenType.BEGIN)) {
            stmts = statementSequence();
        }
        if (match(Token.TokenType.RETURN)) {
            return_expr = expression();
        }
        consume(Token.TokenType.END, "Expected 'END' keyword.");
        if (endOfProcedure()) {
            return new Proc.Body(variable_declarations, stmts, return_expr);
        } else {
            //error
            return null;
        }
    }

    private Stmt.Seq statementSequence() {
        List<Stmt> statements = new ArrayList<>();
        do {
            statements.add(statement());
        } while (match(Token.TokenType.SEMICOLON));
        return new Stmt.Seq(statements);
    }

    private Stmt.Inline inlineStatementSequence() {
        List<Stmt> statements = new ArrayList<>();
        do {
            statements.add(statement());
        } while (match(Token.TokenType.SEMICOLON));
        return new Stmt.Inline(statements);
    }

    private Stmt statement() {
        if (check(Token.TokenType.OP)) return commandStatement();
        if (check(Token.TokenType.INC)) return incStatement();
        if (check(Token.TokenType.DEC)) return decStatement();
        if (check(Token.TokenType.ROL)) return rolStatement();
        if (check(Token.TokenType.ROR)) return rorStatement();
        if (check(Token.TokenType.QUERY)) return queryStatement();
        if (check(Token.TokenType.IF)) return ifStatement();
        if (check(Token.TokenType.WHILE)) return whileStatement();
        if (check(Token.TokenType.REPEAT)) return repeatStatement();
        if (check(Token.TokenType.IDENT)) {
            if (next().getTokenType() == Token.TokenType.BECOMES)
                return assignStatement();
            else
                return callStatement();
        }

        //error
        return null;
    }

    private Stmt commandStatement() {
        boolean bool;
        IToken ident;
        IToken indexToken = null;
        int index;
        IToken operation;

        operation = consume(Token.TokenType.OP, "Expected '!' keyword.");
        bool = !match(Token.TokenType.NOT);
        ident = ident();
        if (match(Token.TokenType.PERIOD)) {
            indexToken = consume(Token.TokenType.NUMBER, "Expected a number.");
        }
        if (indexToken != null) {
            Double doubleIndex = (Double) indexToken.getLiteral();
            index = doubleIndex.intValue();
        } else {
            index = -1;
        }
        return new Stmt.Command(bool, ident, index, operation);
    }

    private Stmt incStatement() {
        IToken operation = consume(Token.TokenType.INC, "Expected 'INC'.");
        IToken ident = ident();
        return new Stmt.Command(true, ident, -1, operation);
    }

    private Stmt decStatement() {
        IToken operation = consume(Token.TokenType.DEC, "Expected 'DEC'.");
        IToken ident = ident();
        return new Stmt.Command(true, ident, -1, operation);
    }

    private Stmt rolStatement() {
        IToken operation = consume(Token.TokenType.ROL, "Expected 'ROL'.");
        IToken ident = ident();
        return new Stmt.Command(true, ident, -1, operation);
    }

    private Stmt rorStatement() {
        IToken operation = consume(Token.TokenType.ROR, "Expected 'ROR'.");
        IToken ident = ident();
        return new Stmt.Command(true, ident, -1, operation);
    }

    private Stmt queryStatement() {
        consume(Token.TokenType.QUERY, "Expected '?' keyword.");
        boolean bool = !match(Token.TokenType.NOT);
        IToken ident = ident();
        IToken indexToken = null;
        if (match(Token.TokenType.PERIOD)) {
            indexToken = consume(Token.TokenType.NUMBER, "Expected a number.");
        }
        int index;
        if (indexToken != null) {
            Double doubleIndex = (Double) indexToken.getLiteral();
            index = doubleIndex.intValue();
        } else {
            index = -1;
        }
        return new Stmt.Query(bool, ident, index);
    }

    private Stmt ifStatement() {
        consume(Token.TokenType.IF, "Expected 'IF' keyword.");
        Expr condition = expression();
        List<Stmt.ElseIf> elseIfBranches = new ArrayList<>();

        consume(Token.TokenType.THEN, "Expect 'THEN' after condition.");

        Stmt thenBranch = inlineStatementSequence();

        while (check(Token.TokenType.ELSIF)) {
            elseIfBranches.add(elseIfStatement());
        }

        Stmt elseBranch = null;
        if (match(Token.TokenType.ELSE)) {
            elseBranch = inlineStatementSequence();
        }
        consume(Token.TokenType.END, "Expect 'END' to finish if statement.");

        return new Stmt.If(condition, thenBranch, elseBranch, elseIfBranches);
    }

    private Stmt.ElseIf elseIfStatement() {
        consume(Token.TokenType.ELSIF, "Expected 'ELSIF' keyword.");
        Expr condition = expression();
        consume(Token.TokenType.THEN, "Expect 'THEN' after condition.");
        Stmt thenBranch = inlineStatementSequence();
        return new Stmt.ElseIf(condition,thenBranch);
    }

    private Stmt whileStatement() {
        consume(Token.TokenType.WHILE, "Expected 'WHILE' keyword.");
        Expr condition = expression();
        consume(Token.TokenType.DO, "Expected 'DO' keyword.");
        Stmt.Inline statement_sequence = inlineStatementSequence();
        List<Stmt.ElseIf> elseIfBranches = new ArrayList<>();
        while (match(Token.TokenType.ELSIF)) {
            elseIfBranches.add(elseIfStatement());
        }
        consume(Token.TokenType.END, "Expected 'END' keyword.");
        return new Stmt.While(condition, statement_sequence, elseIfBranches);
    }

    private Stmt repeatStatement() {
        consume(Token.TokenType.REPEAT, "Expected 'REPEAT' keyword.");
        Stmt.Seq statement_sequence = statementSequence();
        Expr condition = null;
        if (match(Token.TokenType.UNTIL)) {
            condition = expression();
        } else {
            consume(Token.TokenType.END, "Expected 'END' keyword.");
        }
        return new Stmt.Repeat(statement_sequence, condition);
    }

    private Stmt assignStatement() {
        IToken ident = consume(Token.TokenType.IDENT, "Expect an identifier.");
        Expr value = null;
        if (match(Token.TokenType.BECOMES)) {
            value = expression();
        }
        return new Stmt.Assign((Token) ident,value);
    }

    private Stmt callStatement() {
        IToken ident = ident();
        Expr expression = null;
        if (match(Token.TokenType.LPAREN)) {
            expression = expression();
            consume(Token.TokenType.RPAREN, "Expect ')' after parameter.");
        }
        return new Stmt.Call(ident, expression);
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = or();

        if (match(Token.TokenType.BECOMES)) {
            Expr value = assignment();

            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
                return new Expr.Assign(name, value);
            } else if (expr instanceof Expr.Get) {
                Expr.Get get = (Expr.Get)expr;
                return new Expr.Set(get.object, get.index, value);
            }
        }

        return expr;

    }

    private Expr or() {
        Expr expr = and();

        while (match(Token.TokenType.OR)) {
            IToken operator = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, (Token) operator, right);
        }

        return expr;
    }

    private Expr and() {
        Expr expr = equality();

        while (match(Token.TokenType.AND)) {
            IToken operator = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, (Token) operator, right);
        }

        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(Token.TokenType.EQL, Token.TokenType.NEQ)) {
            IToken operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary((Token) operator, expr , right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(Token.TokenType.GTR, Token.TokenType.GEQ, Token.TokenType.LSS, Token.TokenType.LEQ)) {
            IToken operator = previous();
            Expr right = term();
            expr = new Expr.Binary((Token) operator, expr , right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(Token.TokenType.PLUS, Token.TokenType.MINUS)) {
            IToken operator = previous();
            Expr right = factor();
            expr = new Expr.Binary((Token) operator, expr, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(Token.TokenType.SLASH, Token.TokenType.AST)) {
            IToken operator = previous();
            Expr right = unary();
            expr = new Expr.Binary((Token) operator, expr, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(Token.TokenType.NOT, Token.TokenType.MINUS)) {
            IToken operator = previous();
            Expr right = unary();
            return new Expr.Unary((Token) operator, right);
        }

        return call();
    }

    private Expr call() {
        Expr expr = primary();

        while (true) {
            if (match(Token.TokenType.LPAREN)) {
                expr = finishCall(expr);
            } else if (match(Token.TokenType.PERIOD)) {
                IToken index = consume(Token.TokenType.NUMBER, "Expected an index after '.'");
                expr = new Expr.Get(expr, (Token) index);
            } else {
                break;
            }
        }

        return expr;
    }

    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (match(Token.TokenType.LPAREN)) {
            do {
                if (arguments.size() >= 255) {
                    break;
                }
                arguments.add(expression());
            } while (match(Token.TokenType.COMMA));
            consume(Token.TokenType.RPAREN, "Expect ')' after arguments.");
        }

        return new Expr.Call(callee, arguments);
    }

    private Expr primary() {
        if (match(Token.TokenType.NUMBER)) {
            return new Expr.Literal(previous().getLiteral());
        }
        if (match(Token.TokenType.IDENT)) {
            return new Expr.Variable((Token) previous());
        }

        //error
        return null;
    }


    private boolean match(Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private IToken consume(Token.TokenType type, String message) {
        if (check(type)) {
            return advance();
        } else {
            //error
            return null;
        }
    }

    private boolean check(Token.TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().getTokenType() == type;
    }

    private IToken advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getTokenType() == Token.TokenType.EOF;
    }

    private IToken peek() {
        return tokens.get(current);
    }

    private IToken previous() {
        return tokens.get(current - 1);
    }

    private IToken next() {
        return tokens.get(current + 1);
    }


}
