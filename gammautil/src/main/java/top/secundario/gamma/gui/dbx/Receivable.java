package top.secundario.gamma.gui.dbx;

@FunctionalInterface
public interface Receivable<V> {
    public void receive(V v, Object src);
}
