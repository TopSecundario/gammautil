package top.secundario.gamma.gui.test;

import top.secundario.gamma.gui.Components;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestDialogExample2 {
    public static void main(String[] args) {
        Supplier<JComponent> functionTestAreaSupplier = () -> {
            return new JLabel("move to center");
        };
        String[] checkList = new String[]{"Check item 1", "Check item 2"};
        Consumer<TestDialog> doBeforeVisible = Components::moveToScreenCenter;
        TestDialog testDialog = TestDialog.display("Test dialog example 2", functionTestAreaSupplier, checkList, doBeforeVisible);
        System.out.println(testDialog.assertPass());
    }
}
