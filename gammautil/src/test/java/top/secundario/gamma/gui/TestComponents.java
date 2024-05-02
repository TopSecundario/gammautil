package top.secundario.gamma.gui;

import org.junit.Test;
import top.secundario.gamma.gui.test.TestDialog;

import javax.swing.*;
import java.util.function.Supplier;

import static org.junit.Assert.assertTrue;

public class TestComponents {

    @Test
    public void test_moveToScreenCenter() {
        Supplier<JComponent> ftas = () -> {
            return new JLabel("Move this dialog to screen center.");
        };
        String[] checkList = new String[]{"Is this dialog in the screen center?"};

        TestDialog testDialog = TestDialog.display("Test: moveToScreenCenter", ftas, checkList, Components::moveToScreenCenter);
        assertTrue(testDialog.assertPass());
    }
}
