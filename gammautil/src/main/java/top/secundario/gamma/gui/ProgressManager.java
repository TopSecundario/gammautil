package top.secundario.gamma.gui;

import static top.secundario.gamma.gui.ProgressIndicator.Progress.*;

public class ProgressManager {
    private static final ProgressIndicator DefaultProgressIndicator = (_arg1, _arg2, _arg3, _arg4) -> {};


    private final ProgressIndicator indicator;
    private final long totalAmount;
    /** millisecond */
    private long lastReportTime;
    /** millisecond */
    private long firstReportTime;
    /** millisecond */
    private long reportInterval;

    public ProgressManager(ProgressIndicator pi, long total) {
        this.indicator = (null != pi) ? pi : DefaultProgressIndicator;
        this.totalAmount = total;
        this.lastReportTime = 0;
        this.firstReportTime = System.currentTimeMillis();
        this.reportInterval = 500;
    }

    public void reportStart() {
        lastReportTime = System.currentTimeMillis();
        indicator.indicate(0, totalAmount, START, 0);
        firstReportTime = lastReportTime;
    }

    public boolean report(long processed) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastReportTime >= reportInterval) {
            indicator.indicate(processed, totalAmount, PROCESSING, currentTime - firstReportTime);
            lastReportTime = currentTime;
            return true;
        } else {
            return false;
        }
    }

    public void reportTerminate(long processed) {
        indicator.indicate(processed, totalAmount, TERMINATED, System.currentTimeMillis() - firstReportTime);
    }

    public ProgressIndicator getIndicator() {
        return indicator;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public long getReportInterval() {
        return reportInterval;
    }

    public void setReportInterval(long reportInterval) {
        this.reportInterval = reportInterval;
    }
}
