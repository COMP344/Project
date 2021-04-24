import java.util.Map;

public class AstPrinter implements IVisitor<String>, IExpressionVisitor<String> {

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
        return get.object.accept(this) + "." + get.index.getLexeme();
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
        String modName = decl.modHeader.ident.getLexeme();
        sb.append("MODULE ");
        sb.append(decl.modHeader.accept(this));
        sb.append(decl.modBody.accept(this));
        sb.append("END ").append(modName).append(".");
        return sb.toString();
    }

    @Override
    public String visitConstDecl(Decl.Const decl) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONST ");
        for (Map.Entry<IToken, Expr> entry : decl.assignList.entrySet()) {
            sb.append(entry.getKey().getLexeme()).append(" = ").append(entry.getValue().accept(this));
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
        StringBuilder sb = new StringBuilder();
        String procName = decl.procHeader.ident.getLexeme();
        sb.append("PROCEDURE ");
        sb.append(decl.procHeader.accept(this));
        sb.append(decl.procBody.accept(this));
        sb.append(insertTabs()).append("END ").append(procName).append("\n");
        return sb.toString();
    }

    //Statements visit functions

    @Override
    public String visitSeqStmt(Stmt.Seq stmt) {
        StringBuilder sb = new StringBuilder();
        int numOfStmts = stmt.stmtList.size();
        for (Stmt stmt1 : stmt.stmtList) {
            numOfStmts--;
            sb.append(insertTabs());
            sb.append(stmt1.accept(this));
            if (numOfStmts >= 1) {
                sb.append(";");
            }
            sb.append("\n");
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
        StringBuilder sb = new StringBuilder();
        sb.append(header.ident.getLexeme());
        if (header.param != null) {
            sb.append(header.param.accept(this));
        }
        if (header.return_type != null) {
            sb.append(": ");
            sb.append(header.return_type.getLexeme());
        }
        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public String visitBodyProc(Proc.Body body) {
        StringBuilder sb = new StringBuilder();
        if (body.variable_declarations.size() >= 1) {
            currentTabs++;
            for (Decl.Var variable_declaration : body.variable_declarations) {
                sb.append(insertTabs()).append(variable_declaration.accept(this));
            }
            currentTabs--;
        }
        if (body.stmts != null) {
            sb.append(insertTabs()).append("BEGIN\n");
            currentTabs++;
            sb.append(body.stmts.accept(this));
            currentTabs--;
        }
        if (body.return_expr != null) {
            currentTabs++;
            sb.append(insertTabs()).append("RETURN ");
            sb.append(body.return_expr.accept(this)).append("\n");
            currentTabs--;
        }
        return sb.toString();
    }

    @Override
    public String visitParamProc(Proc.Param param) {
        return "(" + param.type.getLexeme() + " " + param.name.getLexeme() + ")";
    }

    @Override
    public String visitCallStmt(Stmt.Call call) {
        return null;
    }

    @Override
    public String visitRepeatStmt(Stmt.Repeat repeat) {
        StringBuilder sb = new StringBuilder();
        sb.append("REPEAT\n");
        currentTabs++;
        sb.append(repeat.statement_sequence.accept(this));
        currentTabs--;
        sb.append(insertTabs()).append("UNTIL");
        sb.append(repeat.condition.accept(this));
        return sb.toString();
    }

    @Override
    public String visitWhileStmt(Stmt.While aWhile) {
        StringBuilder sb = new StringBuilder();
        sb.append("WHILE ");
        sb.append(aWhile.condition.accept(this));
        sb.append(" DO ");
        sb.append(aWhile.statement_sequence.accept(this));
        sb.append(" END");
        return sb.toString();
    }

    @Override
    public String visitQueryStmt(Stmt.Query query) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        if (!query.bool) sb.append("~");
        sb.append(query.ident.getLexeme());
        if (query.index != -1) {
            sb.append(".").append(query.index);
        }
        return sb.toString();
    }

    @Override
    public String visitCommandStmt(Stmt.Command command) {
        StringBuilder sb = new StringBuilder();
        if (command.operation.getTokenType().equals(Token.TokenType.OP)) {
            sb.append("!");
            if (!command.bool) sb.append("~");
            sb.append(command.ident.getLexeme());
            if (command.index != -1) {
                sb.append(".").append(command.index);
            }
            return sb.toString();
        } else {
            sb.append(command.operation.getLexeme());
            sb.append(" ");
            sb.append(command.ident.getLexeme());
        }
        return sb.toString();
    }

    @Override
    public String visitElseIfStmt(Stmt.ElseIf elseIf) {
        return "ELSIF " +
                elseIf.condition.accept(this) +
                " THEN " +
                elseIf.thenBranch.accept(this);
    }

    @Override
    public String visitInlineStmt(Stmt.Inline inline) {
        StringBuilder sb = new StringBuilder();
        int numOfStms = inline.stmtList.size();
        for (Stmt stmt : inline.stmtList) {
            numOfStms--;
            sb.append(stmt.accept(this));
            if (numOfStms >= 1) {
                sb.append("; ");
            }
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

    private String insertTabs() {
        return "\t".repeat(Math.max(0, currentTabs));
    }

}