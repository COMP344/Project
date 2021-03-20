import java.util.List;

abstract class Mod implements INode {

    static class Header extends Mod {
        IToken ident;

        public Header(IToken ident) {
            this.ident = ident;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitHeaderMod(this);
        }
    }

    static class Body extends Mod {
        Decl.Const constants;
        List<Decl.Var> variable_declarations;
        List<Decl.Procedure> procedure_declarations;
        Stmt.Seq seqStmt;

        public Body(Decl.Const constants, List<Decl.Var> variable_declarations, List<Decl.Procedure> procedure_declarations, Stmt.Seq seqStmt) {
            this.constants = constants;
            this.variable_declarations = variable_declarations;
            this.procedure_declarations = procedure_declarations;
            this.seqStmt = seqStmt;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitBodyMod(this);
        }
    }


}
