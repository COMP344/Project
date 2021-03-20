import java.util.List;

abstract class Proc implements INode {

    static class Header extends Proc {
        final IToken ident;
        final Param param;
        final IToken return_type;

        public Header(IToken ident, Param param, IToken return_type) {
            this.ident = ident;
            this.param = param;
            this.return_type = return_type;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitHeaderProc(this);
        }
    }

    static class Param extends Proc {
        final IToken name;
        final IToken type;

        public Param(IToken name, IToken type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitParamProc(this);
        }
    }

    static class Body extends Proc {
        final List<Decl.Var> variable_declarations;
        final Stmt.Seq stmts;
        final Expr return_expr;

        public Body(List<Decl.Var> variable_declarations, Stmt.Seq stmts, Expr return_expr) {
            this.variable_declarations = variable_declarations;
            this.stmts = stmts;
            this.return_expr = return_expr;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitBodyProc(this);
        }
    }

}
