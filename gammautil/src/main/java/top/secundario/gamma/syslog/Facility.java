package top.secundario.gamma.syslog;

/**
 * The Syslog Protocol: the Facility.
 */
public enum Facility {
    KERNEL_MSG(0, "kernel messages"),
    USER_MSG(1, "user-level messages"),
    MAIL_SYS(2, "mail system"),
    SYS_DAEMONS(3, "system daemons"),
    SECURITY_AUTH_MSG_4(4, "security/authorization messages"),
    SYSLOG_DAEMON(5, "messages generated internally by syslogd"),
    LINE_PRINTER_SUB_SYS(6, "line printer subsystem"),
    NWK_NEWS_SUB_SYS(7, "network news subsystem"),
    UUCP(8, "UUCP subsystem"),
    CLK_DAEMON_9(9, "clock daemon"),
    SECURITY_AUTH_MSG_10(10, "security/authorization messages"),
    FTP(11, "FTP daemon"),
    NTP(12, "NTP subsystem"),
    LOG_AUDIT(13, "log audit"),
    LOG_ALERT(14, "log alert"),
    CLK_DAEMON_15(15, "clock daemon"),
    LOCAL0(16, "local0"),
    LOCAL1(17, "local1"),
    LOCAL2(18, "local2"),
    LOCAL3(19, "local3"),
    LOCAL4(20, "local4"),
    LOCAL5(21, "local5"),
    LOCAL6(22, "local6"),
    LOCAL7(23, "local7")
    ;

    private final int _code;
    private final String _description;

    private Facility(int code, String description) {
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
        return  _description + "(" + _code + ")";
    }
}
