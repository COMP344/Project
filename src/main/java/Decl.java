import java.util.List;

abstract class Decl {

    abstract <R> R accept(Decl.Visitor<R> visitor);

    interface Visitor<R> {
        R visitModuleDecl(Module decl);

        R visitConstDecl(Const decl);

        R visitVarDecl(Var decl);

        R visitProcDecl(Proc decl);

    }

    static class Module extends Decl {
        Mod.Header modHeader;
        Mod.Body modBody;

        public Module(Mod.Header modHeader, Mod.Body modBody) {
            this.modHeader = modHeader;
            this.modBody = modBody;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitModuleDecl(this);
        }
    }

    static class Const extends Decl {
        List<Stmt.Assign> assignList;

        public Const(List<Stmt.Assign> assignList) {
            this.assignList = assignList;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitConstDecl(this);
        }
    }

    static class Var extends Decl {
        Token type;
        List<Token> varNames;

        public Var(Token type, List<Token> varNames) {
            this.type = type;
            this.varNames = varNames;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarDecl(this);
        }
    }

    static class Proc extends Decl {


        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProcDecl(this);
        }
    }


}
