abstract class Mod {

    abstract <R> R accept(Mod.Visitor<R> visitor);

    interface Visitor<R> {

        R visitHeaderMod(Header mod);

        R visitBodyMod(Body mod);

    }

    static class Header extends Mod {
        Token ident;

        public Header(Token ident) {
            this.ident = ident;
        }

        @Override
        <R> R accept(Mod.Visitor<R> visitor) {
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
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBodyMod(this);
        }
    }


}
