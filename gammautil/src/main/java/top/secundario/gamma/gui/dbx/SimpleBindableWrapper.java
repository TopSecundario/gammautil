package top.secundario.gamma.gui.dbx;

public abstract class SimpleBindableWrapper<T, V> extends SimpleBindableObject<V> {
    protected final T wrappedObject;

    protected SimpleBindableWrapper(T obj, Class<V> clzV) {
        super(clzV);
        wrappedObject = obj;
    }

    @Override
    public Object getSource() {
        return wrappedObject;
    }

    @Override
    public String toString() {
        return wrappedObject.toString();
    }
}
