package top.secundario.gamma.syslog.tl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Predicate;

import top.secundario.gamma.common.Strings;
import top.secundario.gamma.syslog.RawLog;

public abstract class AbstractLogTransporter {
    /** The Syslog Protocol VERSION  */
    public static final String VERSION = "1";
    /** The Syslog Protocol NILVALUE */
    public static final String NILVALUE = "-";
    /** The Syslog Protocol date-time format */
    public static final DateTimeFormatter SYSLOG_DATE_TIME_FMT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    public static byte[] strToPrintUsAscii(String str, int maxAsciiLen) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(str)) {
            return  new byte[0];
        }
        if (maxAsciiLen <= 0) {
            throw  new IllegalArgumentException("Arg 'maxAsciiLen' less than or equal 0: " + maxAsciiLen);
        }

        if (! str.codePoints().allMatch(i -> {return i >= 33 && i <= 126;})) {
            throw  new IllegalArgumentException("'" + str + "' is not all print US ASCII!");
        }

        byte[] fullAsciiBytes = str.getBytes(StandardCharsets.UTF_8);
        if (fullAsciiBytes.length <= maxAsciiLen) {
            return fullAsciiBytes;
        } else {
            return Arrays.copyOf(fullAsciiBytes, maxAsciiLen);
        }
    }

    public static String timestampToStr(Instant timestamp) {
        return timestampToStr(timestamp, ZoneId.systemDefault());
    }

    public static String timestampToStr(Instant timestamp, ZoneId zoneId) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(timestamp, zoneId);
        return zdt.format(SYSLOG_DATE_TIME_FMT);
    }


    protected String hostname;
    protected String appName;
    protected ZoneId timeZone;
    protected volatile Predicate<RawLog> logFilter;

    protected AbstractLogTransporter(String hostname, String appName, ZoneId timeZone) {
        this(hostname, appName, timeZone, (_rawLog) -> true);
    }

    protected AbstractLogTransporter(String hostname, String appName, ZoneId timeZone, Predicate<RawLog> logFilter) {
        this.hostname = !Strings.isNullOrEmpty(hostname) ? hostname : NILVALUE;
        this.appName = !Strings.isNullOrEmpty(appName) ? appName : NILVALUE;
        this.timeZone = timeZone;
        this.logFilter = logFilter;
    }

    public abstract void openTransport();
    public abstract void transportLog(RawLog rawLog);
    public abstract void closeTransport();

    public Predicate<RawLog> getLogFilter() {
        return logFilter;
    }

    public void setLogFilter(Predicate<RawLog> logFilter) {
        this.logFilter = logFilter;
    }
}
