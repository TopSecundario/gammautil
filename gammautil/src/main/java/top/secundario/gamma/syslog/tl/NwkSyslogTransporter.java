package top.secundario.gamma.syslog.tl;

import top.secundario.gamma.syslog.Facility;
import top.secundario.gamma.syslog.Severity;

import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;

public abstract class NwkSyslogTransporter extends AbstractLogTransporter {
    public static final int MAX_PRIVAL_LEN = 3;
    public static final int MAX_TIMESTAMP_LEN = 32;
    public static final int MAX_HOSTNAME_LEN = 255;
    public static final int MAX_APP_NAME_LEN = 48;
    public static final int MAX_PROCID_LEN = 128;
    public static final int MAX_MSGID_LEN = 32;

    protected static final byte[] SP = {32};
    protected static final byte[] ASCII_NILVALUE = {(byte) '-'};
    protected static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    protected static final byte[] ASCII_PRIVAL_START = {(byte) '<'};
    protected static final byte[] ASCII_PRIVAL_END = {(byte) '>'};
    protected static final byte[] ASCII_ZERO = {0};

    protected OutputStream out;

    protected Facility facility;
    protected byte[] ascii_version;
    protected byte[] ascii_hostname;
    protected byte[] ascii_appName;

    protected NwkSyslogTransporter(String hostname, String appName, ZoneId timeZone, Facility facility) throws IllegalArgumentException {
        super(hostname, appName, timeZone);
        this.facility = facility;

        ascii_version = strToPrintUsAscii(VERSION, VERSION.length());
        ascii_hostname = strToPrintUsAscii(this.hostname, MAX_HOSTNAME_LEN);
        ascii_appName = strToPrintUsAscii(this.appName, MAX_APP_NAME_LEN);
    }

    protected int prival(Severity severity) {
        return facility.code() * 8 + severity.code();
    }

    protected byte[] privalToAscii(int prival) {
        return strToPrintUsAscii(Integer.toString(prival), MAX_PRIVAL_LEN);
    }

    protected byte[] timestampToAscii(Instant timestamp) {
        String strTimestamp = timestampToStr(timestamp, timeZone);
        return strToPrintUsAscii(strTimestamp, MAX_TIMESTAMP_LEN);
    }
}
