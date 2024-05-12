package top.secundario.gamma.common;

public interface TreeVisitor<T, R> {
    public TreeVisitIndicator visit(T data, TreeVisitContext<T, R> tvCtx);
}
