package top.secundario.gamma.gui;

import net.miginfocom.swing.MigLayout;
import org.junit.Test;
import top.secundario.gamma.gui.test.TestDialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class TestRoundLabel2 {

    @Test
    public void test_RoundLabel_fill() {
        String[] checkList = new String[]{
                "this component fill the whole MigLayout cell?",
                "set each option",
                "resize the main window",
                "the round rectangle is ok?"
        };

        Consumer<TestDialog> dbv = (_testDialog) -> {
            _testDialog.setSize(500, 600);
            Components.moveToScreenCenter(_testDialog);
        };

        TestDialog testDialog = TestDialog.display("Test: RoundLabel (fill container)", this::createTestArea, checkList, dbv);
        assertTrue(testDialog.assertPass());
    }

    private JPanel createTestArea() {
        String[] logicFontNames = new String[]{"Dialog", "DialogInput", "Monospaced", "Serif", "SansSerif"};

        JPanel panel = new JPanel();

        MigLayout layout = new MigLayout("wrap 2",
                "[grow][]",
                "[grow][]");
        panel.setLayout(layout);

        rlExample = new RoundLabel("MAKE AMERICA GREAT AGAIN");
        rlExample.setOpaque(true);
        rlExample.setBackground(Color.BLUE);
        rlExample.setForeground(Color.WHITE);
        rlExample.setFont(new Font("Dialog", Font.PLAIN, 24));
        rlExample.setBorder(new MatteBorder(new Insets(10, 10, 5, 5), borderColor));
        //rlExample.setInsets();          NO setInsets()
        panel.add(rlExample, "span, center, grow");

        tfContent = new JTextField();
        panel.add(tfContent, "growx");

        JButton btnContent = new JButton("Set content");
        btnContent.addActionListener(this::on_btnContent_action);
        panel.add(btnContent);

        labelBgColor = new JLabel(Colors.toHTML(rlExample.getBackground()), JLabel.CENTER);
        labelBgColor.setOpaque(null != rlExample.getBackground());
        labelBgColor.setBackground(rlExample.getBackground());
        panel.add(labelBgColor, "growx");

        JButton btnBgColor = new JButton("Set bg color");
        btnBgColor.addActionListener(this::on_btnBgColor_action);
        panel.add(btnBgColor);

        labelFgColor = new JLabel(Colors.toHTML(rlExample.getForeground()), JLabel.CENTER);
        labelFgColor.setOpaque(null != rlExample.getForeground());
        labelFgColor.setBackground(rlExample.getForeground());
        panel.add(labelFgColor, "growx");

        JButton btnFgColor = new JButton("Set fg color");
        btnFgColor.addActionListener(this::on_btnFgColor_action);
        panel.add(btnFgColor);

        comboFontName = new JComboBox<>(logicFontNames);
        comboFontName.setSelectedItem(rlExample.getFont().getName());
        comboFontName.addActionListener(this::onFontSet);
        panel.add(comboFontName, "span, split 3");

        comboFontStyle = new JComboBox<>(FontStyle.values());
        comboFontStyle.setSelectedItem(FontStyle.from(rlExample.getFont().getStyle()));
        comboFontStyle.addActionListener(this::onFontSet);
        panel.add(comboFontStyle);

        tfFontSize = new JTextField(String.valueOf(rlExample.getFont().getSize()));
        tfFontSize.addActionListener(this::onFontSet);
        panel.add(tfFontSize, "growx");

        panel.add(new JLabel("t"), "span, split 10");

        tfTop = new JTextField("10");
        tfTop.addActionListener(this::onBorderSet);
        panel.add(tfTop, "growx");

        panel.add(new JLabel("l"));

        tfLeft = new JTextField("10");
        tfLeft.addActionListener(this::onBorderSet);
        panel.add(tfLeft, "growx");

        panel.add(new JLabel("b"));

        tfBottom = new JTextField("5");
        tfBottom.addActionListener(this::onBorderSet);
        panel.add(tfBottom, "growx");

        panel.add(new JLabel("r"));

        tfRight = new JTextField("5");
        tfRight.addActionListener(this::onBorderSet);
        panel.add(tfRight, "growx");

        btnBorderColor = new JButton("Border color");
        btnBorderColor.addActionListener(this::on_btnBorderColor_action);
        panel.add(btnBorderColor);

        cbBorder = new JCheckBox("Border", true);
        cbBorder.addActionListener(this::on_cbBorder_action);
        panel.add(cbBorder);

        return panel;
    }

    public void on_btnContent_action(ActionEvent event) {
        rlExample.setText(tfContent.getText());
    }

    public void on_btnBgColor_action(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice background color", rlExample.getBackground());

        rlExample.setBackground(newColor);

        labelBgColor.setText(Colors.toHTML(newColor));
        labelBgColor.setOpaque(null != newColor);
        labelBgColor.setBackground(newColor);
        Components.adaptForBgColor(labelBgColor);
    }

    public void on_btnFgColor_action(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice foreground color", rlExample.getForeground());
        if (null != newColor) {
            rlExample.setForeground(newColor);

            labelFgColor.setText(Colors.toHTML(newColor));
            labelFgColor.setOpaque(true);
            labelFgColor.setBackground(newColor);
            Components.adaptForBgColor(labelFgColor);
        }
    }

    public void on_btnBorderColor_action(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice border color", borderColor);
        if (null != newColor) {
            borderColor = newColor;
            onBorderSet(event);

            btnBorderColor.setBackground(newColor);
            Components.adaptForBgColor(btnBorderColor);
        }
    }

    public void on_cbBorder_action(ActionEvent event) {
        if (cbBorder.isSelected()) {
            onBorderSet(event);
        } else {
            rlExample.setBorder(null);
        }
    }

    private void onFontSet(ActionEvent event) {
        String fontName = (String) comboFontName.getSelectedItem();
        int fontStyle = ((FontStyle) comboFontStyle.getSelectedItem()).style();
        int fontSize = Integer.parseInt(tfFontSize.getText());
        rlExample.setFont(new Font(fontName, fontStyle, fontSize));
    }

    private void onBorderSet(ActionEvent event) {
        rlExample.setBorder(createBorder());
    }

    private Border createBorder() {
        int top = Integer.parseInt(tfTop.getText());
        int left = Integer.parseInt(tfLeft.getText());
        int bottom = Integer.parseInt(tfBottom.getText());
        int right = Integer.parseInt(tfRight.getText());
        return  new MatteBorder(new Insets(top, left, bottom, right), borderColor);
    }

    private JTextField tfContent;
    private RoundLabel rlExample;
    private JLabel labelBgColor;
    private JLabel labelFgColor;
    private JComboBox<String> comboFontName;
    private JComboBox<FontStyle> comboFontStyle;
    private JTextField tfFontSize;

    private Color borderColor = Color.YELLOW;
    private JTextField tfTop;
    private JTextField tfLeft;
    private JTextField tfBottom;
    private JTextField tfRight;
    private JButton btnBorderColor;
    private JCheckBox cbBorder;

}
