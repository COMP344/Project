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

    static class If extends Stmt {
        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;
        final List<Stmt.ElseIf> elseIfBranches;

        public If(Expr condition, Stmt thenBranch, Stmt elseBranch, List<Stmt.ElseIf> elseIfBranches) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
            this.elseIfBranches = elseIfBranches;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }
    }

    static class ElseIf extends Stmt {
        final Expr condition;
        final Stmt thenBranch;

        public ElseIf(Expr condition, Stmt thenBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitElseIfStmt(this);
        }
    }

    static class Call extends Stmt {
        final IToken ident;
        final Expr expression;

        public Call(IToken ident, Expr expression) {
            this.ident = ident;
            this.expression = expression;
        }


        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitCallStmt(this);
        }
    }

    static class Repeat extends Stmt {
        final Stmt.Seq statement_sequence;
        final Expr condition;

        public Repeat(Stmt.Seq statement_sequence, Expr condition) {
            this.statement_sequence = statement_sequence;
            this.condition = condition;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitRepeatStmt(this);
        }
    }

    static class While extends Stmt {
        final Expr condition;
        final Stmt.Inline statement_sequence;
        final List<Stmt.ElseIf> elseIfBranches;

        public While(Expr condition, Inline statement_sequence, List<Stmt.ElseIf> elseIfBranches) {
            this.condition = condition;
            this.statement_sequence = statement_sequence;
            this.elseIfBranches = elseIfBranches;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }
    }

    static class Query extends Stmt {
        final boolean bool;
        final IToken ident;
        final int index;

        public Query(boolean bool, IToken ident, int index) {
            this.bool = bool;
            this.ident = ident;
            this.index = index;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitQueryStmt(this);
        }
    }

    static class Command extends Stmt {
        final boolean bool;
        final IToken ident;
        final int index;
        final IToken operation;

        public Command(boolean bool, IToken ident, int index, IToken operation) {
            this.bool = bool;
            this.ident = ident;
            this.index = index;
            this.operation = operation;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitCommandStmt(this);
        }
    }

    static class Inline extends Stmt {
        final List<Stmt> stmtList;

        public Inline(List<Stmt> statements) {
            this.stmtList = statements;
        }


        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitInlineStmt(this);
        }
    }
}
