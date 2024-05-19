package top.secundario.gamma.common;

@FunctionalInterface
public interface TreeControllableVisitor<T> {
    public TreeVisitIndicator visit(T data);
}
