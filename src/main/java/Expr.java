import java.util.List;

abstract class Expr implements INode {

    static class Unary extends Expr {

        final IToken operator;
        final Expr right;
        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    static class Binary extends Expr {

        final IToken operator;
        final Expr right;
        final Expr left;
        public Binary(Token operator, Expr left, Expr right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    static class Assign extends Expr {

        final IToken name;
        final Expr value;
        public Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    static class Logical extends Expr {

        final Expr left;
        final IToken operator;
        final Expr right;
        public Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    static class Literal extends Expr {

        final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    static class Variable extends Expr {

        final Token name;

        Variable(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }

    static class Grouping extends Expr {

        final Expr expression;

        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    static class Get extends Expr {

        final Expr object;
        final Token index;

        public Get(Expr object, Token index) {
            this.object = object;
            this.index = index;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }
    }

    static class Set extends Expr {

        final Expr object;
        final Token index;
        final Expr value;

        public Set(Expr object, Token index, Expr value) {
            this.object = object;
            this.index = index;
            this.value = value;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }
    }

    static class Call extends Expr {
        final Expr callee;
        final List<Expr> arguments;

        public Call(Expr callee, List<Expr> arguments) {
            this.callee = callee;
            this.arguments = arguments;
        }

        @Override
        public <R> R accept(IVisitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

}
