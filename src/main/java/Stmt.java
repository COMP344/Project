import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class Stmt {

    abstract <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitSeqStmt(Seq stmt);

        R visitAssignStmt(Assign stmt);

        R visitExprStmt(ExprStmt expr);

    }

    static class Seq extends Stmt {
        List<Stmt> stmtList;

        public Seq(Stmt... stmts) {
            this.stmtList = new ArrayList<>();
            this.stmtList.addAll(Arrays.asList(stmts));
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSeqStmt(this);
        }
    }

    static class Assign extends Stmt {
        Token ident;
        Expr value;

        public Assign(Token ident, Expr value) {
            this.ident = ident;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignStmt(this);
        }
    }

    static class ExprStmt extends Stmt {
        Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExprStmt(this);
        }
    }
}
