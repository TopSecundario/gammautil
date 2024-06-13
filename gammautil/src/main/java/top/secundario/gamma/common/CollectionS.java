package top.secundario.gamma.common;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class CollectionS {

    public static <T> T search(Collection<T> c, Predicate<? super T> p) {
        Objects.requireNonNull(c);
        Objects.requireNonNull(p);

        for (T e : c) {
            if (p.test(e)) {
                return e;
            }
        }

        return null;
    }

    protected CollectionS() {}
}
