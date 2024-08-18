package top.secundario.gamma.syslog.tl;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

import top.secundario.gamma.syslog.RawLog;
import top.secundario.gamma.syslog.Severity;

public class FileLogTransporter extends PrintStreamLogTransporter {
    public FileLogTransporter(String hostname, String appName, String pathToLogFile) throws IOException {
        super(hostname, appName, new PrintStream(pathToLogFile, StandardCharsets.UTF_8));
    }

    public FileLogTransporter(String hostname, String appName, String pathToLogFile, Predicate<RawLog> logFilter)
            throws IOException
    {
        super(hostname, appName, new PrintStream(pathToLogFile, StandardCharsets.UTF_8), logFilter);
    }

    @Override
    public void closeTransport() {
        out.close();
        super.closeTransport();
    }

    protected void printSeverity(Severity severity) {
        out.print(severity);
    }

    protected void printMessage(String message, Severity severity) {
        out.print(message);
    }
}
