import java.util.List;

abstract class Decl implements INode {

    static class Module extends Decl {
        Mod.Header modHeader;
        Mod.Body modBody;

        public Module(Mod.Header modHeader, Mod.Body modBody) {
            this.modHeader = modHeader;
            this.modBody = modBody;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitModuleDecl(this);
        }
    }

    static class Const extends Decl {
        List<Stmt.Assign> assignList;

        public Const(List<Stmt.Assign> assignList) {
            this.assignList = assignList;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitConstDecl(this);
        }
    }

    static class Var extends Decl {
        final IToken type;
        final List<IToken> variables;

        public Var(IToken type, List<IToken> variables) {
            this.type = type;
            this.variables = variables;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitVarDecl(this);
        }
    }

    static class Procedure extends Decl {
        Proc.Header procHeader;
        Proc.Body procBody;

        public Procedure(Proc.Header procHeader, Proc.Body procBody) {
            this.procHeader = procHeader;
            this.procBody = procBody;
        }

        //        final IToken name;
//        final IToken param;
//        final Decl.Var variables;
//        final Stmt.Seq body;
//        final IToken return_type;
//        final Expr return_expr;
//
//        public Proc(IToken name, IToken param, Decl.Var variables, Stmt.Seq body, IToken return_type, Expr return_expr) {
//            this.name = name;
//            this.param = param;
//            this.variables = variables;
//            this.body = body;
//            this.return_type = return_type;
//            this.return_expr = return_expr;
//        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitProcDecl(this);
        }
    }


}
