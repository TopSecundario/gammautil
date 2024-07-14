package top.secundario.gamma.gui;

public record ProgressReport(long processed, long total, ProgressIndicator.Progress progress, long elapsedMilli) {
}
