package top.secundario.gamma.gui;

public class ProgressManager {
    private static final ProgressIndicator DefaultProgressIndicator = (_arg1, _arg2, _arg3, _arg4) -> {};


    private final ProgressIndicator indicator;
    private long totalAmount;
    /** millisecond */
    private Long lastReportTime;
    /** millisecond */
    private long reportInterval;
    /** millisecond */
    private long startTime;
    private volatile boolean cancel;

    public ProgressManager(ProgressIndicator pi, long total) {
        this.indicator = (null != pi) ? pi : DefaultProgressIndicator;
        this.totalAmount = total;
        this.lastReportTime = 0L;
        this.reportInterval = 500;
        this.startTime = System.currentTimeMillis();
        this.cancel = false;
    }

    public void reportStart() {
        lastReportTime = System.currentTimeMillis();
        startTime = lastReportTime;
        indicator.indicate(0, totalAmount, ProgressIndicator.Progress.START, 0);
    }

    public boolean report(long processed) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastReportTime >= reportInterval) {
            lastReportTime = currentTime;
            indicator.indicate(processed, totalAmount, ProgressIndicator.Progress.PROCESSING, currentTime - startTime);
            return true;
        } else {
            return false;
        }
    }

    public void reportTerminate(long processed) {
        indicator.indicate(processed, totalAmount, ProgressIndicator.Progress.TERMINATED, System.currentTimeMillis() - startTime);
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void requestCancel() {
        cancel = true;
    }

    public boolean shouldCancel() {
        return cancel;
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
