package top.secundario.gamma.gui.dbx;

import javax.swing.*;

class LabelWrapper<V> extends SimpleBindableWrapper<JLabel, V> {
    LabelWrapper(JLabel label, Class<V> clzV) {
        super(label, clzV);
    }

    @Override
    public V provide(Class<V> clzV) throws DBXException {
        if (clzV == String.class) {
            return (V) wrappedObject.getText();
        } else {
            throw  new DBXException("JLabel can't provide none-string value!");
        }
    }

    @Override
    public void receive(V v, Object src) {
        String _v = (null != v) ? v.toString() : null;
        wrappedObject.setText(_v);
    }

    @Override
    public void addChangeListener(ChangeListener<V> listener) { }

    @Override
    public void removeChangeListener(ChangeListener<V> listener) { }
}
