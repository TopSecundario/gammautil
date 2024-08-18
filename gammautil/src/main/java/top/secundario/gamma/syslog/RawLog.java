package top.secundario.gamma.syslog;

import java.time.Instant;

public class RawLog {
    public Instant timestamp;
    public Severity severity;
    public String msgFmt;
    public Object[] msgArgs;
    /** the Syslog MSGID */
    public String msgid;
    /** the Syslog PROCID */
    public String procid;
    /** message has formatted */
    public String message;
}
