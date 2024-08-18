package top.secundario.gamma.syslog;

/**
 * The Syslog Protocol: the Severity.
 */
public enum Severity {
    Emergency(0, "system is unusable"),
    Alert(1, "action must be taken immediately"),
    Critical(2, "critical conditions"),
    Error(3, "error conditions"),
    Warning(4, "warning conditions"),
    Notice(5, "normal but significant condition"),
    Informational(6, "informational messages"),
    Debug(7, "debug-level messages")
    ;

    public static final int MAX_CODE = 7;
    private final int _code;
    private final String _description;

    private Severity(int code, String description) {
        _code = code;
        _description = description;
    }

    public int code() {
        return _code;
    }

    public String description() {
        return _description;
    }

    public String toString() {
        return name().toUpperCase();
    }

    public static Severity fromCode(int code) throws IllegalArgumentException {
        for (Severity e : values()) {
            if (e._code == code) {
                return e;
            }
        }

        throw  new IllegalArgumentException("No such severity code: " + code);
    }
}
