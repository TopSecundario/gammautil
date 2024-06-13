package top.secundario.gamma.gui;

@FunctionalInterface
public interface ProgressIndicator {
    public static enum Progress {
        START,
        PROCESSING,
        TERMINATED;
    }


    /**
     * Indicate progress.
     *
     * @param processed   Processed amount
     * @param total       Total amount
     * @param progress    Progress state
     * @param elapsedTime Elapsed time in millisecond
     */
    public void indicate(long processed, long total, Progress progress, long elapsedTime);
}
