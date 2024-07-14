package top.secundario.gamma.gui.dbx;

@FunctionalInterface
public interface ChangeListener<V> {
    public void onChanged(V v, Object src);
}
