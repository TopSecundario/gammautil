package top.secundario.gamma.common;

@FunctionalInterface
public interface TreePostVisitor<T, R> {
    public TreeVisitIndicator postVisit(T data, TreePostVisitContext<T, R> tpvCtx);
}
