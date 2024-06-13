package top.secundario.gamma.gui.test;

import top.secundario.gamma.gui.ProgressIndicator;
import top.secundario.gamma.gui.ProgressManager;

public class ProgressManagerExample {
    private static void doStuffLong() {
        int t = 1000;
        ProgressIndicator pi = (_p, _t, _s, _e) -> {
            System.out.printf("Processed %d, Total %d, State %s, Elapsed %ds%n", _p, _t, _s, _e/1000);
        };
        ProgressManager pm = new ProgressManager(pi, t);

        int i;
        pm.reportStart();
        for (i = 0; i < t ; ++i) {
            try {
                Thread.sleep(100);    // simulate long-term task
            } catch (InterruptedException ignore) {}
            pm.report(i + 1);
        }
        pm.reportTerminate(i);
    }

    public static void main(String[] args) {
        doStuffLong();
    }
}
