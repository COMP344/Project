public interface IExpressionNode {
    <R> R accept(IExpressionVisitor<R> visitor);
}
