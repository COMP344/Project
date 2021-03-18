import java.util.ArrayList;
import java.util.List;

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
             declarations.add(declarations());
        }

        return declarations;
    }

    private Decl declarations() {
        if (match(Token.TokenType.MODULE)) {
            return module_declaration();
        } else {
            //error
            return null;
        }
    }

    private Decl module_declaration() {
        Mod.Header header = module_header();
        Mod.Body body = module_body();
        return new Decl.Module(header, body);
    }

    private Mod.Header module_header() {
        IToken name = consume(Token.TokenType.IDENT, "Expect Module name");
        assert name != null;
        current_module_name = name.getLexeme();
        if (match(Token.TokenType.SEMICOLON)) {
            return new Mod.Header(name);
        } else {
            //error
            return null;
        }
    }

    private Mod.Body module_body() {
        Decl.Const const_declaration = null;
        Decl.Var variable_declaration = null;
        Decl.Proc procedure_declaration = null;

        if (check(Token.TokenType.CONST)) {
            const_declaration = constant_declaration();
        }
        if (check(Token.TokenType.INT) || check(Token.TokenType.SET) || check(Token.TokenType.BOOL)) {
            variable_declaration = variable_declaration();
            consume(Token.TokenType.SEMICOLON, "Expect ';'");
        }
        while (match(Token.TokenType.PROCED)) {
            procedure_declaration = procedure_declaration();
        }
        consume(Token.TokenType.BEGIN, "Expect 'BEGIN'");
        Stmt.Seq statement_sequence = statement_sequence();
        consume(Token.TokenType.END, "Expect 'END'");
        if (endOfModule()) {
            consume(Token.TokenType.PERIOD, "Expect a '.'");
            return new Mod.Body(const_declaration,variable_declaration,procedure_declaration,statement_sequence);
        } else {
            //error
            return null;
        }
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

    private Stmt.Seq statement_sequence() {
        List<Stmt> statements = new ArrayList<>();
        do {
            statements.add(statement());
        } while (!check(Token.TokenType.END));
        return new Stmt.Seq(statements);
    }

    private Stmt statement() {
        if (match(Token.TokenType.IF)) return ifStatement();
        //if (check(Token.TokenType.IDENT)) return assignStatement();
        //error
        return expressionStatement();
    }

    private Stmt assignStatement() {
        IToken ident = consume(Token.TokenType.IDENT, "Expect an identifier.");
        Expr value = expression();
        return new Stmt.Assign((Token) ident, value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(Token.TokenType.SEMICOLON, "Expect ';' after expression");
        return new Stmt.ExprStmt(expr);
    }


    private Stmt ifStatement() {
        Expr condition = expression();
        consume(Token.TokenType.THEN, "Expect 'THEN' after condition.");

        Stmt thenBranch = statement();

        Stmt elseBranch = null;
        if (match(Token.TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new Stmt.IF(condition, thenBranch, elseBranch);
    }

    private Decl.Proc procedure_declaration() {
        IToken name = consume(Token.TokenType.IDENT, "Expect name of procedure.");
        IToken parameter = null;
        IToken return_type = null;
        Decl.Var variables = null;
        Stmt.Seq body = null;
        Expr return_expr = null;

        assert name != null;
        current_procedure_name = name.getLexeme();

        if (match(Token.TokenType.LPAREN)) {
            parameter = consume(Token.TokenType.IDENT, "Expect parameter identifier.");
            consume(Token.TokenType.RPAREN, "Expect ')' after parameter.");
        }
        if (match(Token.TokenType.COLON)) {
            if (match(Token.TokenType.INT)) return_type = previous();
            if (match(Token.TokenType.SET)) return_type = previous();
            if (match(Token.TokenType.BOOL)) return_type = previous();
        }
        variables = variable_declaration();
        if (match(Token.TokenType.BEGIN)) {
            body = statement_sequence();
        }
        if (match(Token.TokenType.RETURN)) {
            return_expr = expression();
        }
        consume(Token.TokenType.END, "Expect 'END'");
        if (endOfProcedure()) {
            return new Decl.Proc(name, parameter, variables, body, return_type, return_expr);
        } else {
            //error
            return null;
        }
    }

    private Decl.Var variable_declaration() {
        List<Stmt.Var> declVarList = new ArrayList<>();
        do {
            IToken type = null;
            if (match(Token.TokenType.INT)) type = previous();
            if (match(Token.TokenType.SET)) type = previous();
            if (match(Token.TokenType.BOOL)) type = previous();
            List<IToken> var_list = new ArrayList<>();
            do {
                var_list.add(consume(Token.TokenType.IDENT, "Expect identifier."));
            } while (match(Token.TokenType.COMMA));
            consume(Token.TokenType.SEMICOLON, "Expect ';'");
            declVarList.add(new Stmt.Var((Token) type, var_list));
        } while (check(Token.TokenType.INT) || check(Token.TokenType.SET) || check(Token.TokenType.BOOL));
        return new Decl.Var(declVarList);
    }

    private Decl.Const constant_declaration() {
        List<Stmt.Assign> assignList = new ArrayList<>();
        IToken ident;
        Expr value;
        while (match(Token.TokenType.CONST)) {
            ident = consume(Token.TokenType.IDENT, "Expect an identifier.");
            consume(Token.TokenType.EQL, "Expect '='.");
            value = expression();
            assignList.add(new Stmt.Assign((Token) ident, value));
            consume(Token.TokenType.SEMICOLON, "Expect ';' after a constant declared");
        }

        return new Decl.Const(assignList);
    }

    private Stmt.Assign assign_statement() {
        IToken ident = consume(Token.TokenType.IDENT, "Expect an identifier.");
        Expr value = null;
        if (match(Token.TokenType.BECOMES)) {
            value = expression();
        }
        return new Stmt.Assign((Token) ident,value);
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = or();

        if (match(Token.TokenType.BECOMES)) {
            IToken equals = previous();
            Expr value = assignment();

            if (expr instanceof Expr.Variable) {
                IToken name = ((Expr.Variable)expr).name;
                return new Expr.Assign((Token) name, value);
            } else if (expr instanceof Expr.Get) {
                Expr.Get get = (Expr.Get)expr;
                return new Expr.Set(get.object, get.name, value);
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
                IToken name = consume(Token.TokenType.NUMBER, "Expected an index after '.'");
                expr = new Expr.Get(expr, (Token) name);
            } else {
                break;
            }
        }

        return expr;
    }

    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(Token.TokenType.RPAREN)) {
            do {
                if (arguments.size() >= 255) {
                    break;
                }
                arguments.add(expression());
            } while (match(Token.TokenType.COMMA));
        }

        IToken paren = consume(Token.TokenType.RPAREN, "Expect ')' after arguments.");

        return new Expr.Call(callee, (Token) paren, arguments);
    }

    private Expr primary() {
        if (match(Token.TokenType.NUMBER)) {
            return new Expr.Literal(previous().getLiteral());
        }
        if (match(Token.TokenType.IDENT)) {
            return new Expr.Variable((Token) previous());
        }
        if (match(Token.TokenType.LPAREN)) {
            Expr expr = expression();
            consume(Token.TokenType.RPAREN, "Expected ')' after expression");
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


}
