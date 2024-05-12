package top.secundario.gamma.common;

@FunctionalInterface
public interface Func3R<X, Y, Z, R> {
    public R f(X x, Y y, Z z);
}
