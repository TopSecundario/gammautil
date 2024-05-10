package top.secundario.gamma.common;


public class Ref<T> {
    private T v;

    public Ref() {
        v = null;
    }

    public Ref(T v) {
        this.v = v;
    }

    public T get() {
        return v;
    }

    public void set(T v) {
        this.v = v;
    }

    public String toString() {
        return  ObjectS.isNotNull(v) ? v.toString() : null;
    }

    // TODO: equals and hashCode
}
