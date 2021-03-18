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
        Decl.Const consts;
        Decl.Var vars;
        Decl.Proc proc;
        Stmt.Seq seqStmt;

        public Body(Decl.Const consts, Decl.Var vars, Decl.Proc proc, Stmt.Seq seqStmt) {
            this.consts = consts;
            this.vars = vars;
            this.proc = proc;
            this.seqStmt = seqStmt;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitBodyMod(this);
        }
    }


}
