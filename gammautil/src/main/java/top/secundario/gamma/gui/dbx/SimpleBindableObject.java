package top.secundario.gamma.gui.dbx;

import java.util.ArrayList;
import java.util.List;

public class SimpleBindableObject<V> extends BindableObject<V> {

    private final List<ChangeListener<V>> changeListenerList;

    public SimpleBindableObject(Class<V> clzV) {
        super(clzV);
        changeListenerList = new ArrayList<>();
    }

    @Override
    public void addChangeListener(ChangeListener<V> listener) {
        changeListenerList.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener<V> listener) {
        changeListenerList.remove(listener);
    }

    protected void notifyAllChangeListeners() {
        try {
            V v = provide(clazzV);
            changeListenerList.forEach((_listener) -> {_listener.onChanged(v, getSource());});
        } catch (DBXException dbxe) {
            System.err.println(dbxe);
        }
    }
}
