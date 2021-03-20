

public class AstPrinter implements IVisitor<String> {

    int currentTabs;

    AstPrinter() {
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
        return parenthesize(expr.operator.getLexeme(), expr.right);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.getLexeme(), expr.left, expr.right);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return expr.name.getLexeme() + " := " + expr.value.accept(this);
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesize(expr.operator.getLexeme(), expr.left, expr.right);
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        return expr.value.toString();
    }

    @Override
    public String visitVariableExpr(Expr.Variable var) {
        return var.name.getLexeme();
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping group) {
        return null;
    }

    @Override
    public String visitGetExpr(Expr.Get get) {
        return null;
    }

    @Override
    public String visitSetExpr(Expr.Set set) {
        return null;
    }

    @Override
    public String visitCallExpr(Expr.Call call) {
        return null;
    }

    //Declaration visit functions

    @Override
    public String visitModuleDecl(Decl.Module decl) {
        StringBuilder sb = new StringBuilder();
        sb.append("MODULE ");
        String modName = decl.modHeader.ident.getLexeme();
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
            sb.append(assignment.ident.getLexeme()).append(" = ").append(assignment.value.accept(this));
        }
        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public String visitVarDecl(Decl.Var decl) {
        StringBuilder sb = new StringBuilder();
        sb.append(decl.type.getLexeme()).append(" ");
        int numOfVar = decl.variables.size();
        for (IToken var : decl.variables) {
            numOfVar--;
            sb.append(var.getLexeme());
            if (numOfVar >= 1) {
                sb.append(", ");
            }
        }
        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public String visitProcDecl(Decl.Procedure decl) {
        return null;
    }

    //Statements visit functions

    @Override
    public String visitSeqStmt(Stmt.Seq stmt) {
        StringBuilder sb = new StringBuilder();
        for (Stmt stmt1 : stmt.stmtList) {
            sb.append(insertTabs());
            sb.append(stmt1.accept(this));
            sb.append(" ;\n");
        }
        return sb.toString();
    }

    @Override
    public String visitAssignStmt(Stmt.Assign stmt) {
        return stmt.ident.getLexeme() + " := " + stmt.value.accept(this);
    }

    @Override
    public String visitExprStmt(Stmt.ExprStmt expr) {
        return expr.expr.accept(this);
    }

    @Override
    public String visitVarStmt(Stmt.Var decl) {
        StringBuilder sb = new StringBuilder();
        sb.append(decl.type.getLexeme()).append(" ");
        int numOfAssignments = decl.varNames.size();
        for (IToken var : decl.varNames) {
            numOfAssignments--;
            sb.append(var.getLexeme());
            if (numOfAssignments >= 1) {
                sb.append(", ");
            }
        }
        sb.append("; ");
        return sb.toString();
    }

    @Override
    public String visitHeaderProc(Proc.Header header) {
        return null;
    }

    @Override
    public String visitBodyProc(Proc.Body param) {
        return null;
    }

    @Override
    public String visitParamProc(Proc.Param param) {
        return null;
    }

    @Override
    public String visitCallStmt(Stmt.Call call) {
        return null;
    }

    @Override
    public String visitRepeatStmt(Stmt.Repeat repeat) {
        return null;
    }

    @Override
    public String visitWhileStmt(Stmt.While aWhile) {
        return null;
    }

    @Override
    public String visitQueryStmt(Stmt.Query query) {
        return null;
    }

    @Override
    public String visitCommandStmt(Stmt.Command command) {
        return null;
    }

    @Override
    public String visitElseIfStmt(Stmt.ElseIf elseIf) {
        StringBuilder sb = new StringBuilder();
        sb.append("ELSIF ");
        sb.append(elseIf.condition.accept(this));
        sb.append(" THEN ");
        sb.append(elseIf.thenBranch.accept(this));
        return sb.toString();
    }

    @Override
    public String visitInlineStmt(Stmt.Inline inline) {
        StringBuilder sb = new StringBuilder();
        for (Stmt stmt : inline.stmtList) {
            sb.append(stmt.accept(this)).append(" ");
        }
        return sb.toString();
    }

    @Override
    public String visitIfStmt(Stmt.If anIf) {
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");
        sb.append(anIf.condition.accept(this));
        sb.append(" THEN ");
        sb.append(anIf.thenBranch.accept(this));
        if (anIf.elseIfBranches != null) {
            for (Stmt elseIf : anIf.elseIfBranches) {
                sb.append(elseIf.accept(this));
            }
        }
        if (anIf.elseBranch != null) {
            sb.append("ELSE ");
            sb.append(anIf.elseBranch.accept(this));
        }
        sb.append("END");
        return sb.toString();
    }

    //Module visit functions

    @Override
    public String visitHeaderMod(Mod.Header mod) {
        return mod.ident.getLexeme() + ";\n";
    }

    @Override
    public String visitBodyMod(Mod.Body mod) {
        currentTabs++;
        StringBuilder sb = new StringBuilder();
        if (mod.constants != null) {
            sb.append(insertTabs()).append(mod.constants.accept(this));
        }
        if (mod.variable_declarations.size() >= 1) {
            for (Decl.Var variable_declaration : mod.variable_declarations) {
                sb.append(insertTabs()).append(variable_declaration.accept(this));
            }
        }
        if (mod.procedure_declarations.size() >= 1) {
            for (Decl.Procedure procedure_declaration : mod.procedure_declarations) {
                sb.append(insertTabs()).append(procedure_declaration.accept(this));
            }
        }
        if (mod.seqStmt != null) {
            sb.append("BEGIN\n");
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

//    public static void main(String[] args) {
////        Expr expr = new Expr.Binary(
////                new Token(Token.TokenType.AST, "*", null, null),
////                new Expr.Unary(
////                        new Token(Token.TokenType.MINUS, "-", null, null),
////                        new Expr.Literal(123)),
////                new Expr.Literal(45.67));
////
////        System.out.println(new AstPrinter().print(expr));
//
//        Decl decl = new Decl.Module(
//                new Mod.Header(new Token(Token.TokenType.MODULE, "Assignments", null, null)),
//                new Mod.Body(
//                        new Decl.Const(
//                                  Arrays.asList(new Stmt.Assign(
//                                            new Token(Token.TokenType.IDENT, "N", null, null),
//                                            new Expr.Literal(10))
//                        )
//                        ),
//                        new Decl.Var(
//                                new Token(Token.TokenType.INT, "INT", null, null),
//                                Arrays.asList(
//                                        new Token(Token.TokenType.IDENT, "x", null, null)
//                                )
//                        ),
//                        null,
//                        null
//                )
//        );
//
//        Stmt stmts = new Stmt.Seq(
//                                    new Stmt.ExprStmt(
//                                            new Expr.Literal(10)
//                                    ),
//                                    new Stmt.ExprStmt(
//                                            new Expr.Literal(20)
//                                    )
//        );
//
////        Decl decl = new Decl.Const(
////                                    Arrays.asList(new Stmt.Assign(
////                                            new Token(Token.TokenType.IDENT, "N", null, null),
////                                            new Expr.Literal(10))
////                                    ));
//        System.out.println(new AstPrinter().printDecl(decl));
//
//    }

}