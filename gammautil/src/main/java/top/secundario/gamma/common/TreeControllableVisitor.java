package top.secundario.gamma.common;

public interface TreeControllableVisitor<T> {
    public TreeVisitIndicator visit(T data);
}
