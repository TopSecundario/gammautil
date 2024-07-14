package top.secundario.gamma.gui.dbx;

public abstract class BindableWrapper<T, V> extends SimpleBindableWrapper<T, V> {
    private final Providable<V> providable;
    private final Receivable<V> receivable;


    protected BindableWrapper(T obj, Providable<V> p, Class<V> clzV) {
        this(obj, p, (_v, _src) -> {}, clzV);
    }

    protected BindableWrapper(T obj, Receivable<V> r, Class<V> clzV) {
        this(obj, new NullProvider<>(), r, clzV);
    }

    protected BindableWrapper(T obj, Providable<V> p, Receivable<V> r, Class<V> clzV) {
        super(obj, clzV);
        providable = p;
        receivable = r;
    }

    @Override
    public V provide(Class<V> clzV) throws DBXException {
        return providable.provide(clzV);
    }

    @Override
    public void receive(V v, Object src) {
        receivable.receive(v, src);
    }
}
