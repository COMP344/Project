import java.util.Arrays;

public class AstPrinter implements Expr.Visitor<String>, Decl.Visitor<String>, Stmt.Visitor<String>, Mod.Visitor<String> {

    StringBuilder sb;

    int currentTabs;

    AstPrinter() {
        this.sb = new StringBuilder();
        this.currentTabs = 0;
    }

    String printExpr(Expr expr) {
        return expr.accept(this);
    }

    String printDecl(Decl decl) {
        return decl.accept(this);
    }

    String printMod(Mod mod) {
        return mod.accept(this);
    }

    String printStmt(Stmt stmt) {
        return stmt.accept(this);
    }

    //Expression visit functions

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
        return parenthesizeAssignExpr(expr.name.lexeme, expr.value);
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        return expr.value.toString();
    }

    //Declaration visit functions

    @Override
    public String visitModuleDecl(Decl.Module decl) {
        StringBuilder sb = new StringBuilder();
        sb.append("MODULE ");
        String modName = decl.modHeader.ident.lexeme;
        sb.append(decl.modHeader.accept(this));
        sb.append(decl.modBody.accept(this));
        sb.append("END ").append(modName).append(".");
        return sb.toString();
    }

    @Override
    public String visitConstDecl(Decl.Const decl) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONST ");
        for (Stmt.Assign assignment : decl.assignList) {
            sb.append(assignment.ident.lexeme).append(" = ").append(assignment.value.accept(this));
        }
        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public String visitVarDecl(Decl.Var decl) {
        StringBuilder sb = new StringBuilder();
        sb.append(decl.type.lexeme).append(" ");
        int numOfAssignments = decl.varNames.size();
        for (Token var : decl.varNames) {
            numOfAssignments--;
            sb.append(var.lexeme);
            if (numOfAssignments >= 1) {
                sb.append(", ");
            }
        }
        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public String visitProcDecl(Decl.Proc decl) {
        return null;
    }

    //Statements visit functions

    @Override
    public String visitSeqStmt(Stmt.Seq stmt) {
        StringBuilder sb = new StringBuilder();
        for (Stmt stmt1 : stmt.stmtList) {
            sb.append(insertTabs()).append(stmt1.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visitAssignStmt(Stmt.Assign stmt) {
        return stmt.ident.lexeme + " := " + stmt.value.accept(this);
    }

    @Override
    public String visitExprStmt(Stmt.ExprStmt expr) {
        return expr.expr.accept(this);
    }

    //Module visit functions

    @Override
    public String visitHeaderMod(Mod.Header mod) {
        return mod.ident.lexeme + ";\n";
    }

    @Override
    public String visitBodyMod(Mod.Body mod) {
        currentTabs++;
        StringBuilder sb = new StringBuilder();
        if (mod.consts != null) {
            sb.append(insertTabs()).append(mod.consts.accept(this));
        }
        if (mod.vars != null) {
            sb.append(insertTabs()).append(mod.vars.accept(this));
        }
        if (mod.proc != null) {
            sb.append(insertTabs()).append(mod.proc.accept(this));
        }
        if (mod.seqStmt != null) {
            sb.append(mod.seqStmt.accept(this));
        }
        currentTabs--;
        return sb.toString();
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        sb.append(name);

        for (Expr expr : exprs) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }

        sb.append(")");

        return sb.toString();
    }

    private String parenthesizeAssignExpr(String name, Expr expr) {
        StringBuilder sb = new StringBuilder();

        sb.append("(= ");
        sb.append(name);
        sb.append(" ");
        sb.append(expr.accept(this));
        sb.append(")");

        return sb.toString();
    }

    private String insertTabs() {
        return "\t".repeat(Math.max(0, currentTabs));
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
                new Mod.Header(new Token(Token.TokenType.MODULE, "Assignments", null, null)),
                new Mod.Body(
                        new Decl.Const(
                                  Arrays.asList(new Stmt.Assign(
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

        Stmt stmts = new Stmt.Seq(
                                    new Stmt.ExprStmt(
                                            new Expr.Literal(10)
                                    ),
                                    new Stmt.ExprStmt(
                                            new Expr.Literal(20)
                                    )
        );

//        Decl decl = new Decl.Const(
//                                    Arrays.asList(new Stmt.Assign(
//                                            new Token(Token.TokenType.IDENT, "N", null, null),
//                                            new Expr.Literal(10))
//                                    ));
        System.out.println(new AstPrinter().printDecl(decl));

    }

}