package top.secundario.gamma.gui.test;

import top.secundario.gamma.gui.Components;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class TestDialogExample {
    private static TestDialog tdMainWin;

    private static void createMainWindow() {
        tdMainWin = new TestDialog("Test Dialog example 1",
                new JLabel("Can you see this?"),
                new String[]{"I can see"});
        tdMainWin.pack();
        //tdMainWin.setSize(300, 200);
        Components.moveToScreenCenter(tdMainWin);
        tdMainWin.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(TestDialogExample::createMainWindow);
        System.out.println(tdMainWin.assertPass());
    }
}
