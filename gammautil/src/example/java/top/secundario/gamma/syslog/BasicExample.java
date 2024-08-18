package top.secundario.gamma.syslog;

import top.secundario.gamma.syslog.tl.AbstractLogTransporter;
import top.secundario.gamma.syslog.tl.PrintStreamLogTransporter;

import java.time.Instant;
import java.util.Random;

public class BasicExample {
    public static void main(String[] args) {
        Syslog syslog = new Syslog(1000, false,
                new PrintStreamLogTransporter("MyComputer", "testApp"));

        Random rand = new Random(System.currentTimeMillis());
        int c = 10;
        String procid = Thread.currentThread().getName();
        for (int i = 0 ; i < c ; ++i) {
            String msgFmt = "test msg: i=%d";
            Object[] msgArgs = new Object[]{i};

            syslog.log(Severity.Debug, procid, AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);

            try {
                Thread.sleep(rand.nextLong(50, 10000));
            } catch (InterruptedException ignore) {}
        }

        System.exit(0);
    }
}
