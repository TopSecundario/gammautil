package top.secundario.gamma.syslog.tl;

import top.secundario.gamma.syslog.RawLog;
import top.secundario.gamma.syslog.Severity;

import java.io.PrintStream;
import java.time.ZoneId;
import java.util.function.Predicate;

public class PrintStreamLogTransporter extends AbstractLogTransporter {
    protected PrintStream out;

    public PrintStreamLogTransporter(String hostname, String appName) {
        this(hostname, appName, System.out);
    }

    public PrintStreamLogTransporter(String hostname, String appName, Predicate<RawLog> logFilter) {
        this(hostname, appName, System.out, logFilter);
    }

    public PrintStreamLogTransporter(String hostname, String appName, PrintStream ps) {
        super(hostname, appName, ZoneId.systemDefault());
        out = (null != ps) ? ps : System.out;
    }

    public PrintStreamLogTransporter(String hostname, String appName, PrintStream ps, Predicate<RawLog> logFilter) {
        super(hostname, appName, ZoneId.systemDefault(), logFilter);
        out = (null != ps) ? ps : System.out;
    }


    @Override
    public void openTransport() {

    }

    @Override
    public void transportLog(RawLog rawLog) {
        if (logFilter.test(rawLog)) {
            out.print('<');
            printSeverity(rawLog.severity);
            out.print('>');
            out.print(timestampToStr(rawLog.timestamp, timeZone));
            out.print('[');
            out.print(rawLog.procid);
            out.print("] ");
            printMessage(rawLog.message, rawLog.severity);
            out.println();
        }
    }

    @Override
    public void closeTransport() {
        out = null;
    }

    protected void printSeverity(Severity severity) {
        switch (severity) {
            case Emergency:
            case Alert:
            case Critical:
            case Error:
                out.print("\033[37;41m");
                out.print(severity);
                out.print("\033[0m");
                return;

            case Warning:
            case Notice:
                out.print("\033[30;43m");
                out.print(severity);
                out.print("\033[0m");
                return;

            case Informational:
                out.print("\033[30;42m");
                out.print(severity);
                out.print("\033[0m");
                return;

            default:
                out.print(severity);
        }
    }

    protected void printMessage(String message, Severity severity) {
        switch (severity) {
            case Emergency:
            case Alert:
            case Critical:
            case Error:
                out.print("\033[31m");
                out.print(message);
                out.print("\033[0m");
                return;

            case Warning:
            case Notice:
                out.print("\033[33m");
                out.print(message);
                out.print("\033[0m");
                return;

            case Informational:
                out.print("\033[32m");
                out.print(message);
                out.print("\033[0m");
                return;

            default:
                out.print(message);
        }
    }
}
