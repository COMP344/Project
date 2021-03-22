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

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitProcDecl(this);
        }
    }


}
