package top.secundario.gamma.common;

@FunctionalInterface
public interface TreeSimpleVisitor<T> {
    public void visit(T data);
}
