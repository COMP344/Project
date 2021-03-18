interface IVisitor<R> {

    R visitModuleDecl(Decl.Module decl);

    R visitConstDecl(Decl.Const decl);

    R visitVarDecl(Decl.Var decl);

    R visitProcDecl(Decl.Proc decl);

    R visitHeaderMod(Mod.Header mod);

    R visitBodyMod(Mod.Body mod);

    R visitSeqStmt(Stmt.Seq stmt);

    R visitAssignStmt(Stmt.Assign stmt);

    R visitExprStmt(Stmt.ExprStmt expr);

    R visitUnaryExpr(Expr.Unary expr);

    R visitBinaryExpr(Expr.Binary expr);

    R visitAssignExpr(Expr.Assign expr);

    R visitLogicalExpr(Expr.Logical expr);

    R visitLiteral(Expr.Literal expr);

    R visitVariableExpr(Expr.Variable var);

    R visitGroupingExpr(Expr.Grouping group);

    R visitGetExpr(Expr.Get get);

    R visitSetExpr(Expr.Set set);

    R visitCallExpr(Expr.Call call);

    R visitIfStmt(Stmt.IF anIf);

    R visitVarStmt(Stmt.Var var);
}
