package top.secundario.gamma.common;

import java.util.Collection;

@FunctionalInterface
public interface CollectionAdd<C extends Collection<E>, E> {
    public void add(C c, E e);
}
