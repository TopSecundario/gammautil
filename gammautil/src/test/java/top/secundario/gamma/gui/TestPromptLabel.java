package top.secundario.gamma.gui;

import org.junit.Test;
import top.secundario.gamma.gui.test.TestDialog;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertTrue;

public class TestPromptLabel {

    @Test
    public void test_PromptLabel() {
        String[] checkList = new String[]{
                "Input valid prompt",
                "Clean prompt",
                "Prompt check/uncheck null",
                "Input valid content",
                "Clean content",
                "Content check/uncheck null",
                "Select prompt font",
                "Select prompt font style",
                "Input prompt font size",
                "Select content font",
                "Select content font style",
                "Input content font size",
                "Choice prompt fg color",
                "Choice prompt bg color",
                "Cancel prompt bg color",
                "Choice content fg color",
                "Choice content bg color",
                "Cancel content bg color",
        };

        Consumer<TestDialog> dbv = (_testDialog) -> {
            _testDialog.setSize(670, 850);
            Components.moveToScreenCenter(_testDialog);
        };

        TestDialog testDialog = TestDialog.display("Test: PromptLabel", this::createTestArea, checkList, dbv);
        assertTrue(testDialog.assertPass());
    }


    private JPanel createTestArea() {
        JPanel testArea = new JPanel(new BorderLayout());

        promptLabel = new PromptLabel("简体中文", "English");
        promptLabel.setBorder(new LineBorder(Color.YELLOW, 2));
        promptLabel.setPromptFgColor(Color.PINK);
        promptLabel.setContentFgColor(Color.BLUE);
        promptLabel.setPromptBgColor(Color.BLUE);
        promptLabel.setContentBgColor(Color.PINK);
        testArea.add(promptLabel, BorderLayout.CENTER);

        JPanel setPane = createSetPane();
        testArea.add(setPane, BorderLayout.SOUTH);

        return testArea;
    }

    private JPanel createSetPane() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(184,207,229), 1));

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        String[] logicFontNames = new String[]{"Dialog", "DialogInput", "Monospaced", "Serif", "SansSerif"};

        // row 0
        panel.add(new JLabel("Prompt"), new GBC("A1").eAnchor());

        String prompt = promptLabel.getPrompt();
        tfPrompt = new JTextField(prompt);
        if (null == prompt) {
            tfPrompt.setEnabled(false);
        }
        tfPrompt.addActionListener(this::onPromptSet);
        panel.add(tfPrompt, new GBC("B1").hFill().weightX(0.5));

        cbNullPrompt = new JCheckBox("null");
        cbNullPrompt.setSelected(null == prompt);
        cbNullPrompt.addActionListener(this::onPromptNullSelect);
        panel.add(cbNullPrompt, new GBC("C1").wAnchor());

        panel.add(new JLabel("Content"), new GBC("D1").eAnchor());

        String content = promptLabel.getContent();
        tfContent = new JTextField(content);
        if (null == content) {
            tfContent.setEnabled(false);
        }
        tfContent.addActionListener(this::onContentSet);
        panel.add(tfContent, new GBC("E1").hFill().weightX(0.5));

        cbNullContent = new JCheckBox("null");
        cbNullContent.setSelected(null == content);
        cbNullContent.addActionListener(this::onContentNullSelect);
        panel.add(cbNullContent, new GBC("F1").wAnchor());

        // row 1
        panel.add(new JLabel("Prompt Font"), new GBC("A2").eAnchor());

        comboPromptFontName = new JComboBox<>(logicFontNames);
        comboPromptFontName.setSelectedItem(promptLabel.getPromptFont().getName());
        comboPromptFontName.addActionListener(this::onPromptFontSet);
        panel.add(comboPromptFontName, new GBC("B2").hFill().weightX(0.34));

        panel.add(new JLabel("Style"), new GBC("C2").eAnchor());

        comboPromptFontStyle = new JComboBox<>(FontStyle.values());
        comboPromptFontStyle.setSelectedItem(FontStyle.from(promptLabel.getPromptFont().getStyle()));
        comboPromptFontStyle.addActionListener(this::onPromptFontSet);
        panel.add(comboPromptFontStyle, new GBC("D2").hFill().weightX(0.33));

        panel.add(new JLabel("Size"), new GBC("E2").eAnchor());

        tfPromptFontSize = new JTextField(String.valueOf(promptLabel.getPromptFont().getSize()));
        tfPromptFontSize.addActionListener(this::onPromptFontSet);
        panel.add(tfPromptFontSize, new GBC("F2").hFill().weightX(0.33));

        // row 2
        panel.add(new JLabel("Content Font"), new GBC("A3").eAnchor());

        comboContentFontName = new JComboBox<>(logicFontNames);
        comboContentFontName.setSelectedItem(promptLabel.getContentFont().getName());
        comboContentFontName.addActionListener(this::onContentFontSet);
        panel.add(comboContentFontName, new GBC("B3").hFill().weightX(0.34));

        panel.add(new JLabel("Style"), new GBC("C3").eAnchor());

        comboContentFontStyle = new JComboBox<>(FontStyle.values());
        comboContentFontStyle.setSelectedItem(FontStyle.from(promptLabel.getContentFont().getStyle()));
        comboContentFontStyle.addActionListener(this::onContentFontSet);
        panel.add(comboContentFontStyle, new GBC("D3").hFill().weightX(0.33));

        panel.add(new JLabel("Size"), new GBC("E3").eAnchor());

        tfContentFontSize = new JTextField(String.valueOf(promptLabel.getContentFont().getSize()));
        tfContentFontSize.addActionListener(this::onContentFontSet);
        panel.add(tfContentFontSize, new GBC("F3").hFill().weightX(0.33));

        // row 3
        panel.add(new JLabel("Prompt Fg Color"), new GBC("A4").eAnchor());

        labelPromptFgColor = new JLabel(Colors.toHTML(promptLabel.getPromptFgColor()), JLabel.CENTER);
        labelPromptFgColor.setOpaque(true);
        labelPromptFgColor.setBackground(promptLabel.getPromptFgColor());
        Components.adaptForBgColor(labelPromptFgColor);
        panel.add(labelPromptFgColor, new GBC("B4").hFill().weightX(0.5));

        JButton btnPromptFgColor = new JButton("Choice color");
        btnPromptFgColor.addActionListener(this::onPromptFgColorChoice);
        panel.add(btnPromptFgColor, new GBC("C4"));

        panel.add(new JLabel("Bg Color"), new GBC("D4").eAnchor());

        labelPromptBgColor = new JLabel(Colors.toHTML(promptLabel.getPromptBgColor()), JLabel.CENTER);
        labelPromptBgColor.setOpaque(true);
        labelPromptBgColor.setBackground(promptLabel.getPromptBgColor());
        Components.adaptForBgColor(labelPromptBgColor);
        panel.add(labelPromptBgColor, new GBC("E4").hFill().weightX(0.5));

        JButton btnPromptBgColor = new JButton("Choice color");
        btnPromptBgColor.addActionListener(this::onPromptBgColorChoice);
        panel.add(btnPromptBgColor, new GBC("F4"));

        // row 4
        panel.add(new JLabel("Content Fg Color"), new GBC("A5").eAnchor());

        labelContentFgColor = new JLabel(Colors.toHTML(promptLabel.getContentFgColor()), JLabel.CENTER);
        labelContentFgColor.setOpaque(true);
        labelContentFgColor.setBackground(promptLabel.getContentFgColor());
        Components.adaptForBgColor(labelContentFgColor);
        panel.add(labelContentFgColor, new GBC("B5").hFill().weightX(0.5));

        JButton btnContentFgColor = new JButton("Choice color");
        btnContentFgColor.addActionListener(this::onContentFgColorChoice);
        panel.add(btnContentFgColor, new GBC("C5"));

        panel.add(new JLabel("Bg Color"), new GBC("D5").eAnchor());

        labelContentBgColor = new JLabel(Colors.toHTML(promptLabel.getContentBgColor()), JLabel.CENTER);
        labelContentBgColor.setOpaque(true);
        labelContentBgColor.setBackground(promptLabel.getContentBgColor());
        Components.adaptForBgColor(labelContentBgColor);
        panel.add(labelContentBgColor, new GBC("E5").hFill().weightX(0.5));

        JButton btnContentBgColor = new JButton("Choice color");
        btnContentBgColor.addActionListener(this::onContentBgColorChoice);
        panel.add(btnContentBgColor, new GBC("F5"));


        return panel;
    }

    private Font getPromptFont() {
        String fontName = (String) comboPromptFontName.getSelectedItem();
        int fontStyle = ((FontStyle) comboPromptFontStyle.getSelectedItem()).style();
        int fontSize = Integer.parseInt(tfPromptFontSize.getText());
        return new Font(fontName, fontStyle, fontSize);
    }

    private Font getContentFont() {
        String fontName = (String) comboContentFontName.getSelectedItem();
        int fontStyle = ((FontStyle) comboContentFontStyle.getSelectedItem()).style();
        int fontSize = Integer.parseInt(tfContentFontSize.getText());
        return new Font(fontName, fontStyle, fontSize);
    }

    private void onPromptSet(ActionEvent event) {
        promptLabel.setPrompt(tfPrompt.getText());
    }

    private void onPromptNullSelect(ActionEvent event) {
        if (cbNullPrompt.isSelected()) {
            promptLabel.setPrompt(null);
            tfPrompt.setEnabled(false);
        } else {
            promptLabel.setPrompt(tfPrompt.getText());
            tfPrompt.setEnabled(true);
        }
    }

    private void onContentSet(ActionEvent event) {
        promptLabel.setContent(tfContent.getText());
    }

    private void onContentNullSelect(ActionEvent event) {
        if (cbNullContent.isSelected()) {
            promptLabel.setContent(null);
            tfContent.setEnabled(false);
        } else {
            promptLabel.setContent(tfContent.getText());
            tfContent.setEnabled(true);
        }
    }

    private void onPromptFontSet(ActionEvent event) {
        promptLabel.setPromptFont(getPromptFont());
    }

    private void onContentFontSet(ActionEvent event) {
        promptLabel.setContentFont(getContentFont());
    }

    private void onPromptFgColorChoice(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice prompt foreground color", promptLabel.getPromptFgColor());
        if (null != newColor) {
            promptLabel.setPromptFgColor(newColor);
            labelPromptFgColor.setText(Colors.toHTML(newColor));
            labelPromptFgColor.setBackground(newColor);
            Components.adaptForBgColor(labelPromptFgColor);
        }
    }

    private void onPromptBgColorChoice(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice prompt background color", promptLabel.getPromptBgColor());
        promptLabel.setPromptBgColor(newColor);

        if (null != newColor) {
            labelPromptBgColor.setText(Colors.toHTML(newColor));
            labelPromptBgColor.setOpaque(true);
        } else {
            labelPromptBgColor.setText("NONE");
            labelPromptBgColor.setOpaque(false);
        }
        labelPromptBgColor.setBackground(newColor);
        Components.adaptForBgColor(labelPromptBgColor);
    }

    private void onContentFgColorChoice(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice content foreground color", promptLabel.getContentFgColor());
        if (null != newColor) {
            promptLabel.setContentFgColor(newColor);
            labelContentFgColor.setText(Colors.toHTML(newColor));
            labelContentFgColor.setBackground(newColor);
            Components.adaptForBgColor(labelContentFgColor);
        }
    }

    private void onContentBgColorChoice(ActionEvent event) {
        Color newColor = JColorChooser.showDialog(null, "Choice content background color", promptLabel.getContentBgColor());
        promptLabel.setContentBgColor(newColor);

        if (null != newColor) {
            labelContentBgColor.setText(Colors.toHTML(newColor));
            labelContentBgColor.setOpaque(true);
        } else {
            labelContentBgColor.setText("NONE");
            labelContentBgColor.setOpaque(false);
        }
        labelContentBgColor.setBackground(newColor);
        Components.adaptForBgColor(labelContentBgColor);
    }


    private PromptLabel promptLabel;
    private JTextField tfPrompt;
    private JCheckBox cbNullPrompt;
    private JTextField tfContent;
    private JCheckBox cbNullContent;
    private JComboBox<String> comboPromptFontName;
    private JComboBox<FontStyle> comboPromptFontStyle;
    private JTextField tfPromptFontSize;
    private JComboBox<String> comboContentFontName;
    private JComboBox<FontStyle> comboContentFontStyle;
    private JTextField tfContentFontSize;
    private JLabel labelPromptFgColor;
    private JLabel labelPromptBgColor;
    private JLabel labelContentFgColor;
    private JLabel labelContentBgColor;
}
