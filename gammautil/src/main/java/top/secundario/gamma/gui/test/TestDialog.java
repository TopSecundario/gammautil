package top.secundario.gamma.gui.test;

import top.secundario.gamma.common.ObjectS;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestDialog extends JDialog implements WindowListener {

    public static TestDialog display(String title,
                                     Supplier<JComponent> functionTestAreaSupplier,
                                     String[] checkList,
                                     Consumer<TestDialog> doBeforeVisible)
    {
        AtomicReference<TestDialog> arTestDialog = new AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                TestDialog testDialog = null;
                if (null != functionTestAreaSupplier) {
                    testDialog = new TestDialog(title, functionTestAreaSupplier.get(), checkList);
                } else {
                    testDialog = new TestDialog(title, null, checkList);
                }
                testDialog.pack();
                if (null != doBeforeVisible) {
                    doBeforeVisible.accept(testDialog);
                }
                testDialog.setVisible(true);
                arTestDialog.set(testDialog);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        return arTestDialog.get();
    }

    private Map<String, JCheckBox> checkMap;
    private JButton verifyButton;
    private final Semaphore dialogClosedSem = new Semaphore(0);

    public TestDialog(String title, JComponent functionTestArea, String[] checkList) {
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel ftaWrap = new JPanel(new BorderLayout());
        if (ObjectS.isNotNull(functionTestArea)) {
            ftaWrap.add(functionTestArea, BorderLayout.CENTER);
        } else {
            ftaWrap.add(createDefaultFunctionTestArea(), BorderLayout.CENTER);
        }
        ftaWrap.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new TitledBorder("Function test area")));

        JPanel checkListArea = createCheckListArea(checkList);
        checkListArea.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 0, 5),
                new TitledBorder("Check list area")));

        JPanel btnWrap = new JPanel(new FlowLayout());
        verifyButton = new JButton("Verify");
        verifyButton.addActionListener(this::onVerifyButtonClicked);
        btnWrap.add(verifyButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(ftaWrap, BorderLayout.NORTH);
        contentPane.add(checkListArea, BorderLayout.CENTER);
        contentPane.add(btnWrap, BorderLayout.SOUTH);

        addWindowListener(this);
    }

    public boolean assertPass() {
        try {
            dialogClosedSem.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (JCheckBox cb : checkMap.values()) {
            if (! cb.isSelected()) {
                System.err.printf("[CheckItem] \"%s\" : False%n", cb.getText());
                return false;
            }
        }
        return true;
    }

    protected JComponent createDefaultFunctionTestArea() {
        JPanel panel = new JPanel();
        panel.setSize(200, 200);
        panel.setOpaque(true);
        panel.setBackground(new Color(184,207,229));
        return panel;
    }

    private JPanel createCheckListArea(String[] checkList) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.setMinimumSize(new Dimension(200, 100));

        checkMap = new HashMap<>();
        if (ObjectS.isNotNull(checkList)) {
            for (String checkItem : checkList) {
                JCheckBox checkBox = new JCheckBox(checkItem, false);
                panel.add(checkBox);
                checkMap.put(checkItem, checkBox);
            }
        }

        return panel;
    }

    private void onVerifyButtonClicked(ActionEvent event) {
        dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        checkMap.values().forEach((cb) -> cb.setSelected(true));
    }

    @Override
    public void windowClosed(WindowEvent e) {
        dialogClosedSem.release();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
