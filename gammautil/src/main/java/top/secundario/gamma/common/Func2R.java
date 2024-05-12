package top.secundario.gamma.common;

@FunctionalInterface
public interface Func2R<X, Y, R> {
    public R f(X x, Y y);
}
