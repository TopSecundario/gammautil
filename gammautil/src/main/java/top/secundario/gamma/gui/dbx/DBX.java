package top.secundario.gamma.gui.dbx;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Bind and eXchange.
 */
public class DBX<V> implements ChangeListener<V> {
    private V value;
    private final List<BindableObject<V>> bindableObjectList;

    public DBX() {
        value = null;
        bindableObjectList = new ArrayList<>();
    }

    public void set(V v, Object src) {
        if (! Objects.equals(this.value, v)) {
            this.value = v;
            for (BindableObject<V> bo : bindableObjectList) {
                if (bo.getSource() != src) {
                    bo.receive(this.value, src);
                }
            }
        }
    }

    public V get() {
        return value;
    }

    public String toString() {
        return  (Objects.nonNull(value)) ? value.toString() : null;
    }

    @Override
    public void onChanged(V v, Object src) {
        set(v, src);
    }

    public boolean bind(BindableObject<V> bo) {
        for (BindableObject<V> _bo : bindableObjectList) {
            if (_bo.getSource() == bo.getSource()) {
                return false;
            }
        }
        boolean addFlag = bindableObjectList.add(bo);
        if (addFlag) {
            bo.addChangeListener(this);
        }
        return addFlag;
    }

    public void unbind(Object src) {
        for (BindableObject<V> bo : bindableObjectList) {
            if (bo.getSource() == src) {
                bo.removeChangeListener(this);
                bindableObjectList.remove(bo);
                return;
            }
        }
    }

    public boolean bind(JLabel label, Class<V> clzV) {
        return bind(new LabelWrapper<>(label, clzV));
    }

    public boolean bind(JTextField tf, Class<V> clzV) {
        return bind(new TextFieldWrapper<>(tf, clzV));
    }
}
