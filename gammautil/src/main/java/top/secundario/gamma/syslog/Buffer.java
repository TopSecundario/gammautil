package top.secundario.gamma.syslog;

import java.time.Instant;
import java.util.function.Consumer;

/**
 * Buffer for Raw logs.
 */
class Buffer {
    static final int WILL_FULL_RESERVED = 2;

    private final RawLog[] logs;
    final int WILL_FULL_LEVEL;
    private volatile int logCount;

    Buffer(int capacity) throws IllegalArgumentException {
        if (capacity <= WILL_FULL_RESERVED) {
            throw  new IllegalArgumentException("Buffer's capacity less than or equal " + WILL_FULL_RESERVED);
        }

        logs = new RawLog[capacity];
        for (int i = 0 ; i < capacity ; ++i) {
            logs[i] = new RawLog();
        }

        WILL_FULL_LEVEL = capacity - WILL_FULL_RESERVED;
        logCount = 0;
    }

    int getLogCount() {
        return logCount;
    }

    void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    void writeLog(Instant timestamp, Severity severity, String procid, String msgid, String msgFmt, Object[] msgArgs) {
        RawLog log = logs[logCount];
        log.timestamp = timestamp;
        log.severity = severity;
        log.procid = procid;
        log.msgid = msgid;
        log.msgFmt = msgFmt;
        log.msgArgs = msgArgs;
        ++logCount;
    }

    void readLogs(Consumer<RawLog> consumer) {
        for (int i = 0 ; i < logCount ; ++i) {
            consumer.accept(logs[i]);
        }
    }
}
