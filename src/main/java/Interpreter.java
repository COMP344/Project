import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements IVisitor<Void>, IExpressionVisitor<Object> {

    SymbolTable globals = new SymbolTable(null);
    SymbolTable environment = globals;
    StringBuilder result = null;
    Map<String, Object> constants;
    int registerIndex = 12;
    int currentLine = 0;

    Interpreter() {
        result = new StringBuilder();
        constants = new HashMap<>();
        environment.defineVariable("A", "SET", 5, null);
        environment.defineVariable("B", "SET", 6, null);
        environment.defineVariable("S", "SET", 3, null);
    }


    void interpret(List<Decl> declarations) {
        for (Decl declaration : declarations) {
            execute(declaration);
        }
    }

    private void execute(Decl declaration) {
        declaration.accept(this);
    }

    private void execute(Mod module) {
        module.accept(this);
    }

    private void execute(Proc procedure) {
        procedure.accept(this);
    }

    private void execute(Stmt statement) {
        statement.accept(this);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Void visitModuleDecl(Decl.Module decl) {
        execute(decl.modHeader);
        execute(decl.modBody);
        return null;
    }

    @Override
    public Void visitConstDecl(Decl.Const decl) {
        for (Map.Entry<IToken, Expr> entry : decl.assignList.entrySet()) {
            constants.put(entry.getKey().getLexeme(), evaluate(entry.getValue()));
        }
        return null;
    }

    @Override
    public Void visitVarDecl(Decl.Var decl) {
        for (IToken variable : decl.variables) {
            environment.defineVariable(variable.getLexeme(), decl.type.getLexeme(), registerIndex++, null);
        }
        return null;
    }

    @Override
    public Void visitProcDecl(Decl.Procedure decl) {
        return null;
    }

    @Override
    public Void visitHeaderMod(Mod.Header mod) {
        return null;
    }

    @Override
    public Void visitBodyMod(Mod.Body mod) {
        if (mod.constants != null) {
            execute(mod.constants);
        }
        for (Decl var_decl : mod.variable_declarations) {
            execute(var_decl);
        }
        for (Decl proc_decl : mod.procedure_declarations) {
            execute(proc_decl);
        }
        execute(mod.seqStmt);
        return null;
    }

    @Override
    public Void visitSeqStmt(Stmt.Seq stmt) {
        for (Stmt statement : stmt.stmtList) {
            execute(statement);
        }
        return null;
    }

    @Override
    public Void visitAssignStmt(Stmt.Assign stmt) {
        String variable = stmt.ident.getLexeme();
        if (stmt.value instanceof Expr.Variable) {
            String value = (String) evaluate(stmt.value);
            if (constants.containsKey(value)) {
                result.append("\t\tMOVLW ").append(constants.get(value)).append("\n");
            } else {
                result.append("\t\tMOVWF ").append(environment.getVariableRegisterIndex((value))).append(", 0\n");
            }
        } else {
            result.append("\t\tMOVLW ").append(evaluate(stmt.value)).append("\n");
        }
        currentLine++;
        result.append("\t\tMOVWF ").append(environment.getVariableRegisterIndex(variable)).append(", 1\n");
        currentLine++;
        return null;
    }

    @Override
    public Void visitExprStmt(Stmt.ExprStmt expr) {
        return null;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        if (expr.operator.getLexeme().equals("+")) {
            return evaluate(expr.right);
        } else {
            return -((int) evaluate(expr.right));
        }
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        return null;
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        return null;
    }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable var) {
        return var.name.getLexeme();
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping group) {
        return null;
    }

    @Override
    public Object visitGetExpr(Expr.Get get) {
        return null;
    }

    @Override
    public Object visitSetExpr(Expr.Set set) {
        return null;
    }

    @Override
    public Object visitCallExpr(Expr.Call call) {
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If anIf) {
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var var) {
        return null;
    }

    @Override
    public Void visitHeaderProc(Proc.Header header) {
        return null;
    }

    @Override
    public Void visitBodyProc(Proc.Body param) {
        return null;
    }

    @Override
    public Void visitParamProc(Proc.Param param) {
        return null;
    }

    @Override
    public Void visitCallStmt(Stmt.Call call) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(Stmt.Repeat repeat) {
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While aWhile) {
        return null;
    }

    @Override
    public Void visitQueryStmt(Stmt.Query query) {
        int beginLine = currentLine;
        String name = query.ident.getLexeme();
        int registerIndex = environment.getVariableRegisterIndex(name);
        if (query.bool) {
            result.append("\t\tBTFSS ").append(registerIndex).append(", ");
        } else {
            result.append("\t\tBTFSC ").append(registerIndex).append(", ");
        }
        if (query.index < 0) {
            result.append("0").append("\n");
        } else {
            result.append(query.index).append("\n");
        }
        currentLine++;
        result.append("\t\tGOTO ").append(beginLine).append("\n");
        currentLine++;
        return null;
    }

    @Override
    public Void visitCommandStmt(Stmt.Command command) {
        String name = command.ident.getLexeme();
        int registerIndex = environment.getVariableRegisterIndex(name);
        switch (command.operation.getTokenType().name()) {
            case "OP":
                if (command.bool) {
                    result.append("\t\tBSF ").append(registerIndex).append(", ");
                } else {
                    result.append("\t\tBCF ").append(registerIndex).append(", ");
                }
                if (command.index < 0) {
                    result.append("0").append("\n");
                } else {
                    result.append(command.index).append("\n");
                }
                break;
            case "INC":
                result.append("\t\tINCF ").append(registerIndex).append(", 1\n");
                break;
            case "DEC":
                result.append("\t\tDECF ").append(registerIndex).append(", 1\n");
                break;
            case "ROL":
                result.append("\t\tRLF ").append(registerIndex).append(", 1\n");
                break;
            case "ROR":
                result.append("\t\tRRF ").append(registerIndex).append(", 1\n");
                break;
        }

        return null;
    }

    @Override
    public Void visitElseIfStmt(Stmt.ElseIf elseIf) {
        return null;
    }

    @Override
    public Void visitInlineStmt(Stmt.Inline inline) {
        return null;
    }

    public void print() {
        System.out.println(result.toString());
    }
}
