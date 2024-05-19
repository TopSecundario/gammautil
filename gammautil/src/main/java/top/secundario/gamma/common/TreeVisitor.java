package top.secundario.gamma.common;

@FunctionalInterface
public interface TreeVisitor<T, R> {
    public TreeVisitIndicator visit(T data, TreeVisitContext<T, R> tvCtx);
}
