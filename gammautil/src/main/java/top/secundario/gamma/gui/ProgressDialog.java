package top.secundario.gamma.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ProgressDialog extends JDialog implements WindowListener {

    private JLabel labelPrompt;
    private JLabel labelStatus;
    private JProgressBar progressBar;
    private JButton btnCancel;
    private JButton btnDone;
    private WindowActivatedHandler wah;
    private ProgressDialogCloseHandler ch;

    public ProgressDialog(Window owner, String title, String prompt, boolean indeterminate, String status) {
        super(owner, title);
        setSize(400, 240);
        Components.moveToScreenCenter(this);
        wah = null;
        ch = null;
        addWindowListener(this);

        setContentPane(createContentPane(prompt, indeterminate, status));

        setCancelButtonEnable(false);
        setDoneButtonEnable(false);
    }

    public void setPrompt(String prompt) {
        labelPrompt.setText(prompt);
    }

    public void setStatus(String status) {
        labelStatus.setText(status);
    }

    public void setIndeterminateProgressBar(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
    }

    public void setProgressBarLimitValues(int minValue, int maxValue) {
        progressBar.setMinimum(minValue);
        progressBar.setMaximum(maxValue);
    }

    public void setProgressBarValue(int value) {
        progressBar.setValue(value);
    }

    public void setCancelButtonEnable(boolean enable) {
        btnCancel.setEnabled(enable);
    }

    public void setDoneButtonEnable(boolean enable) {
        btnDone.setEnabled(enable);
    }

    public void disableWindowClose() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void setWindowActivatedHandler(WindowActivatedHandler wah) {
        this.wah = wah;
    }

    public void setCloseHandler(ProgressDialogCloseHandler ch) {
        this.ch = ch;
    }

    protected Container createContentPane(String prompt, boolean indeterminate, String status) {
        JPanel panel = new JPanel();

        MigLayout layout = new MigLayout("wrap 2",
                "[grow]",
                "[][][grow][][]");
        panel.setLayout(layout);

        labelPrompt = new JLabel(prompt);
        panel.add(labelPrompt, "span");

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(indeterminate);
        progressBar.setStringPainted(true);
        panel.add(progressBar, "span, growx");

        labelStatus = new JLabel(status);
        labelStatus.setHorizontalAlignment(JLabel.LEFT);
        labelStatus.setVerticalAlignment(JLabel.TOP);
        labelStatus.setOpaque(true);
        labelStatus.setBackground(Color.WHITE);
        panel.add(labelStatus, "span, grow");

        JSeparator hLine = new JSeparator(JSeparator.HORIZONTAL);
        hLine.setMinimumSize(new Dimension(1, 1));
        panel.add(hLine, "span, w 1:65535:65535, h 1!");

        btnCancel = new JButton("取消");
        btnCancel.addActionListener(_evt -> cancel());
        panel.add(btnCancel, "center");

        btnDone = new JButton("确定");
        panel.add(btnDone, "center");

        return panel;
    }

    protected void cancel() {
        if (null != ch) {
            ch.handle();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        cancel();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {
        if (null != wah) {
            wah.handle(e);
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
