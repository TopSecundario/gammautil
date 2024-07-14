package top.secundario.gamma.gui.dbx;

public interface Changeable<V> {
    public void addChangeListener(ChangeListener<V> listener);

    public void removeChangeListener(ChangeListener<V> listener);
}
