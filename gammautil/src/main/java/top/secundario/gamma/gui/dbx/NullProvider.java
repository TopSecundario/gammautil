package top.secundario.gamma.gui.dbx;

public class NullProvider<V> implements Providable<V> {
    @Override
    public V provide(Class<V> clzV) throws DBXException {
        return null;
    }
}
