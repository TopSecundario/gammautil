package top.secundario.gamma.gui;

import net.miginfocom.swing.MigLayout;
import org.junit.Test;
import top.secundario.gamma.gui.dbx.DBX;
import top.secundario.gamma.gui.test.TestDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class TestDBX_BiSlider {
    private DBX<String> dbx1;
    private JLabel label;
    private JLabel valueChangedLabel;

    @Test
    public void test_dbx_and_biSlider() {
        String[] checkList = new String[]{
                "input sth into text field, then press enter",
                "click button",
                "drag left tick of the biSlider",
                "drag right tick of the biSlider",
        };

        Consumer<TestDialog> dbv = (_testDialog) -> {
            _testDialog.setSize(400, 400);
            Components.moveToScreenCenter(_testDialog);
        };

        TestDialog testDialog = TestDialog.display("Test: DBX and BiSlider", this::createTestArea, checkList, dbv);
        assertTrue(testDialog.assertPass());
    }

    private JComponent createTestArea() {
        JPanel contentPane = new JPanel();

        MigLayout layout = new MigLayout("",
                "[grow, fill][]",
                "");
        contentPane.setLayout(layout);

        JTextField tf = new JTextField();
        contentPane.add(tf);

        JButton btn = new JButton("Button");
        btn.addActionListener(this::on_btn_action);
        contentPane.add(btn, "wrap");

        label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.YELLOW);
        contentPane.add(label, "span");

        BiSlider biSlider = new BiSlider();
        biSlider.setLowSlidedValue(25);
        biSlider.setHighSlidedValue(75);
        //biSlider.setBorder(new LineBorder(Color.RED, 2));
        biSlider.addValueChangingHandler(this::on_biSlider_valueChanging);
        biSlider.addValueChangedHandler(this::on_biSlider_valueChanged);
        contentPane.add(biSlider, "span");

        valueChangedLabel = new JLabel();
        valueChangedLabel.setOpaque(true);
        valueChangedLabel.setBackground(Color.YELLOW);
        contentPane.add(valueChangedLabel, "span");

        dbx1 = new DBX<>();
        dbx1.bind(label, String.class);
        dbx1.bind(tf, String.class);
        dbx1.set("hello", null);

        return contentPane;
    }

    private void on_btn_action(ActionEvent event) {
        dbx1.set("hello", null);
    }

    private void on_biSlider_valueChanging(int newLowValue, int newHighValue) {
        label.setText(String.format("BiSlider value changing: lv %d, hv %d", newLowValue, newHighValue));
    }

    private void on_biSlider_valueChanged(int newLowValue, int newHighValue) {
        valueChangedLabel.setText(String.format("BiSlider value changed: lv %d, hv %d", newLowValue, newHighValue));
    }
}
