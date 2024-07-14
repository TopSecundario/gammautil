package top.secundario.gamma.gui.test;

import top.secundario.gamma.gui.ProgressDialog;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class ProgressDialogTest {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ProgressDialog pd = new ProgressDialog(null,
                        "this is title",
                        "this is prompt",
                        true,
                        "this is status");
                pd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                //pd.disableWindowClose();

                pd.setIndeterminateProgressBar(false);
                pd.setProgressBarLimitValues(0, 999);
                pd.setProgressBarValue(500);

                pd.setVisible(true);
            }
        });
    }
}
