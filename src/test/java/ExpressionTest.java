//public class ExpressionTest{

//    public static void main(String[] args) {
//        var expr = new Addition(new Literal(4), new Addition(new Literal(3), new Literal(3)));
//        var sb = new StringBuilder();
//        var expressionPrinter = new ExpressionPrinter(sb);
//        expr.accept(expressionPrinter);
//        System.out.println(sb);
//    }
//}

//class ExpressionPrinter implements IExpressionVisitor {
//    StringBuilder sb;
//
//    public ExpressionPrinter(StringBuilder sb) {
//        this.sb = sb;
//    }
//
//    @Override
//    public void visitLiteralExpr(Literal literal) {
//        sb.append(literal.value);
//    }
//
//    @Override
//    public void visitAdditionExpr(Addition addition) {
//        sb.append("(");
//        addition.left.accept(this);
//        sb.append("+");
//        addition.right.accept(this);
//        sb.append(")");
//    }
//}
