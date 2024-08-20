package top.secundario.gamma.syslog;

import java.io.IOException;
import java.util.Random;

import static top.secundario.gamma.syslog.GlobalLogger.*;

public class GlobalLoggerExample {
    public static void main(String[] args) throws IOException {
        GlobalLogger.initForFileAndStdOut("testApp", Severity.Warning);

        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0 ; i < 100 ; ++i) {
            Severity severity = Severity.fromCode(rand.nextInt(0, Severity.MAX_CODE+1));

            switch (severity) {
                case Emergency:
                    EMERGENCY("消息i=%d", i);
                    break;
                case Alert:
                    ALERT("消息i=%d", i);
                    break;
                case Critical:
                    CRITICAL("消息i=%d", i);
                    break;
                case Error:
                    ERROR("消息i=%d", i);
                    break;
                case Warning:
                    WARN("消息i=%d", i);
                    break;
                case Notice:
                    NOTICE("消息i=%d", i);
                    break;
                case Informational:
                    INFO("消息i=%d", i);
                    break;
                default:
                    DEBUG("消息i=%d", i);
            }

            try {
                Thread.sleep(rand.nextLong(20, 2000));
            } catch (InterruptedException ignore) {}
        }

        System.exit(0);
    }
}
