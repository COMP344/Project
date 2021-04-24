public interface IExpressionVisitor<R> {
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
}
