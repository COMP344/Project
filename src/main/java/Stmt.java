import java.util.List;

abstract class Stmt implements INode {

    static class Var extends Stmt {
        final IToken type;
        final List<IToken> varNames;

        public Var(Token type, List<IToken> varNames) {
            this.type = type;
            this.varNames = varNames;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    static class Seq extends Stmt {
        List<Stmt> stmtList;

        public Seq(List<Stmt> statements) {
            this.stmtList = statements;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitSeqStmt(this);
        }
    }

    static class Assign extends Stmt {
        IToken ident;
        Expr value;

        public Assign(Token ident, Expr value) {
            this.ident = ident;
            this.value = value;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitAssignStmt(this);
        }
    }

    static class ExprStmt extends Stmt {
        Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitExprStmt(this);
        }
    }

    static class IF extends Stmt {
        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;

        public IF(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }
    }
}
