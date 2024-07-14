package top.secundario.gamma.gui.dbx;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TextFieldWrapper<V> extends SimpleBindableWrapper<JTextField, V> implements ActionListener {
    TextFieldWrapper(JTextField tf, Class<V> clzV) {
        super(tf, clzV);
    }

    @Override
    public V provide(Class<V> clzV) throws DBXException {
        if (clzV == String.class) {
            return (V) wrappedObject.getText();
        } else {
            throw  new DBXException("JTextField can't provide none-string value!");
        }
    }

    @Override
    public void receive(V v, Object src) {
        String _v = (null != v) ? v.toString() : null;
        wrappedObject.setText(_v);
    }

    @Override
    public void addChangeListener(ChangeListener<V> chgl) {
        super.addChangeListener(chgl);
        wrappedObject.addActionListener(this);
    }

    @Override
    public void removeChangeListener(ChangeListener<V> chgl) {
        super.removeChangeListener(chgl);
        wrappedObject.removeActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        notifyAllChangeListeners();
    }
}
