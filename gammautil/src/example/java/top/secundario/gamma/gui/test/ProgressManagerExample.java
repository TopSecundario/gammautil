package top.secundario.gamma.gui.test;

import top.secundario.gamma.gui.ProgressIndicator;
import top.secundario.gamma.gui.ProgressManager;

public class ProgressManagerExample {
    public static final long MILLI_PER_SEC = 1000;
    public static final long MILLI_PER_MIN = MILLI_PER_SEC * 60;
    public static final long MILLI_PER_HOUR = MILLI_PER_MIN * 60;

    private static String formatMillisecond(long milli) {
        long hour = milli / MILLI_PER_HOUR;
        long min = (milli - MILLI_PER_HOUR * hour) / MILLI_PER_MIN;
        long sec = (milli - MILLI_PER_HOUR * hour - MILLI_PER_MIN * min) / MILLI_PER_SEC;
        return String.format("%dh %d′%d″", hour, min, sec);
    }

    private static void doStuffLong() {
        int t = 2000;
        ProgressIndicator pi = (_p, _t, _s, _e) -> {
            System.out.printf("Processed %d, Total %d, State %s, %s elapsed%n", _p, _t, _s, formatMillisecond(_e));
        };
        ProgressManager pm = new ProgressManager(pi, t);

        int i;
        pm.reportStart();
        for (i = 0; i < t ; ++i) {
            try {
                Thread.sleep(40);    // simulate long-term task
            } catch (InterruptedException ignore) {}
            pm.report(i + 1);
        }
        pm.reportTerminate(i);
    }

    public static void main(String[] args) {
        doStuffLong();
    }
}
