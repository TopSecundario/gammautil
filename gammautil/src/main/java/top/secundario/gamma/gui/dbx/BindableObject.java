package top.secundario.gamma.gui.dbx;

public abstract class BindableObject<V> implements Providable<V>, Receivable<V>, Changeable<V> {
    public final Class<V> clazzV;

    protected BindableObject(Class<V> clzV) {
        clazzV = clzV;
    }

    @Override
    public V provide(Class<V> clzV) throws DBXException {
        return null;
    }

    @Override
    public void receive(V v, Object src) { }

    public Object getSource() {
        return this;
    }
}
