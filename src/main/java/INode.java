interface INode {
    <R> R accept(IVisitor<R> visitor);
}
