package top.secundario.gamma.gui.dbx;

@FunctionalInterface
public interface Providable<V> {
    public V provide(Class<V> clzV) throws DBXException;
}
