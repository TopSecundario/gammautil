package top.secundario.gamma.common;

import java.util.function.Consumer;

public class ArrayS {

    public static <T> void forEach(T[] array, Consumer<? super T> action) {
        ObjectS.reqNotNull(array);
        ObjectS.reqNotNull(action);

        for (T item : array) {
            action.accept(item);
        }
    }

    protected ArrayS() {}
}
