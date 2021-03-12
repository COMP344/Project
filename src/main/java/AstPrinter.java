import java.util.Arrays;

public class AstPrinter implements Expr.Visitor<String>, Decl.Visitor<String>, Stmt.Visitor<String>, Mod.Visitor<String> {

    StringBuilder sb;

    AstPrinter() {
        this.sb = new StringBuilder();
    }

    String print(Expr expr) {
        return expr.accept(this);
    }

    String print(Decl decl) { return decl.accept(this); }

    String print(Mod mod) {
        return mod.accept(this);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesizeOther("=", expr.name.lexeme, expr.value);
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return null;
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        return expr.value.toString();
    }



    private String parenthesize(String name, Expr ... exprs) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        sb.append(name);

        for(Expr expr : exprs) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }

        sb.append(")");

        return sb.toString();
    }

    private String parenthesizeOther(String name, Object ... objs) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        sb.append(name);

        for(Object obj : objs) {
            sb.append(" ");
            if (obj instanceof Expr) {
                sb.append(((Expr) obj).accept(this));
            } else if (obj instanceof Token) {
                sb.append(((Token) obj).lexeme);
            } else {
                sb.append(obj);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
//        Expr expr = new Expr.Binary(
//                new Token(Token.TokenType.AST, "*", null, null),
//                new Expr.Unary(
//                        new Token(Token.TokenType.MINUS, "-", null, null),
//                        new Expr.Literal(123)),
//                new Expr.Literal(45.67));
//
//        System.out.println(new AstPrinter().print(expr));

        Decl decl = new Decl.Module(
                        new Mod.Header(new Token(Token.TokenType.MODULE, "MODULE", null, null)),
                        new Mod.Body(
                                new Decl.Const(Arrays.asList(
                                                        new Stmt.Assign(
                                                            new Token(Token.TokenType.IDENT, "N", null, null),
                                                            new Expr.Literal(10))
                                                            )
                                ),
                                new Decl.Var(
                                        new Token(Token.TokenType.INT, "INT", null, null),
                                        Arrays.asList(
                                                new Token(Token.TokenType.IDENT, "x", null, null)
                                                )
                                ),
                                null,
                                null
                        )
                    );
        System.out.println(new AstPrinter().print(decl));
    }

    @Override
    public String visitModuleDecl(Decl.Module decl) {
        return "MODULE " + decl.modBody.accept(this);
    }

    @Override
    public String visitConstDecl(Decl.Const decl) {
        sb = new StringBuilder();
        sb.append("CONST");
        for (Stmt.Assign assignment : decl.assignList) {
            sb.append(assignment.ident.lexeme).append(" = ").append(assignment.value.toString()).append(";");
        }
        return sb.toString();
    }

    @Override
    public String visitVarDecl(Decl.Var decl) {
        sb = new StringBuilder();
        sb.append(decl.type.lexeme);
        for (Token var : decl.varNames) {
            sb.append(var.lexeme).append(", ");
        }
        return sb.toString();
    }

    @Override
    public String visitProcDecl(Decl.Proc decl) {
        return null;
    }

    @Override
    public String visitSeqStmt(Stmt.Seq stmt) {
        return null;
    }

    @Override
    public String visitAssignStmt(Stmt.Assign stmt) {
        return null;
    }

    @Override
    public String visitHeaderMod(Mod.Header mod) {
        return mod.ident.lexeme;
    }

    @Override
    public String visitBodyMod(Mod.Body mod) {
        sb = new StringBuilder();
        sb.append(mod.consts.accept(this)).append('\n');
        sb.append(mod.vars.accept(this)).append('\n');

        sb.append("END ");
        return sb.toString();
    }
}