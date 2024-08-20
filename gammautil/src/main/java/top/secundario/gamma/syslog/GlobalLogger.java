package top.secundario.gamma.syslog;

import top.secundario.gamma.syslog.tl.AbstractLogTransporter;
import top.secundario.gamma.syslog.tl.FileLogTransporter;
import top.secundario.gamma.syslog.tl.PrintStreamLogTransporter;
import top.secundario.gamma.syslog.tl.SeverityLogFilter;

import java.io.IOException;

public class GlobalLogger {

    public static void LOG(Severity severity, String msgFmt, Object... msgArgs) {
        LOG(severity, AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void EMERGENCY(String msgFmt, Object... msgArgs) {
        EMERGENCY(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void ALERT(String msgFmt, Object... msgArgs) {
        ALERT(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void CRITICAL(String msgFmt, Object... msgArgs) {
        CRITICAL(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void ERROR(String msgFmt, Object... msgArgs) {
        ERROR(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void WARN(String msgFmt, Object... msgArgs) {
        WARN(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void NOTICE(String msgFmt, Object... msgArgs) {
        NOTICE(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void INFO(String msgFmt, Object... msgArgs) {
        INFO(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }

    public static void DEBUG(String msgFmt, Object... msgArgs) {
        DEBUG(AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
    }


    public static void LOG(Severity severity, String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(severity, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void EMERGENCY(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Emergency, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void ALERT(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Alert, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void CRITICAL(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Critical, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void ERROR(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Error, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void WARN(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Warning, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void NOTICE(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Notice, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void INFO(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Informational, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }

    public static void DEBUG(String msgid, String msgFmt, Object... msgArgs) {
        globalLogger.log(Severity.Debug, Thread.currentThread().getName(), msgid, msgFmt, msgArgs);
    }


    private static Syslog globalLogger;

    public static void initForFileOut(String appName) throws IOException {
        initForFileOut(appName, false);
    }

    public static void initForFileOut(String appName, boolean enableStatistics) throws IOException {
        initForFileOut(1000, appName, enableStatistics);
    }

    public static void initForFileOut(int logBufferCapacity, String appName, boolean enableStatistics) throws IOException {
        String pathToLogFile = appName + ".log";
        initForFileOut(logBufferCapacity, appName, pathToLogFile, enableStatistics);
    }

    public static void initForFileOut(int logBufferCapacity, String appName, String pathToLogFile, boolean enableStatistics) throws IOException {
        FileLogTransporter fileOut = new FileLogTransporter(AbstractLogTransporter.NILVALUE, appName, pathToLogFile);
        globalLogger = new Syslog(logBufferCapacity, enableStatistics, fileOut);
    }


    public static void initForFileAndStdOut(String appName, Severity stdOutLowestSeverity) throws IOException {
        String pathToLogFile = appName + ".log";
        initForFileAndStdOut(1000, appName, pathToLogFile, stdOutLowestSeverity, false);
    }

    public static void initForFileAndStdOut(int logBufferCapacity, String appName, String pathToLogFile, Severity stdOutLowestSeverity, boolean enableStatistics) throws IOException {
        FileLogTransporter fileOut = new FileLogTransporter(AbstractLogTransporter.NILVALUE, appName, pathToLogFile);
        PrintStreamLogTransporter stdOut = new PrintStreamLogTransporter(AbstractLogTransporter.NILVALUE, appName, new SeverityLogFilter(stdOutLowestSeverity));
        globalLogger = new Syslog(logBufferCapacity, enableStatistics, fileOut, stdOut);
    }

    protected GlobalLogger() {}
}
