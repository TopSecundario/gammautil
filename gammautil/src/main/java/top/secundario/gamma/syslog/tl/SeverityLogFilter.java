package top.secundario.gamma.syslog.tl;

import top.secundario.gamma.syslog.RawLog;
import top.secundario.gamma.syslog.Severity;

import java.util.function.Predicate;

public class SeverityLogFilter implements Predicate<RawLog> {
    private Severity lowestSeverity;

    public SeverityLogFilter() {
        this(Severity.Debug);
    }

    public SeverityLogFilter(Severity lowestSeverity) {
        this.lowestSeverity = lowestSeverity;
    }

    public Severity getLowestSeverity() {
        return lowestSeverity;
    }

    public void setLowestSeverity(Severity lowestSeverity) {
        this.lowestSeverity = lowestSeverity;
    }

    @Override
    public boolean test(RawLog rawLog) {
        return  rawLog.severity.code() <= lowestSeverity.code();
    }
}
