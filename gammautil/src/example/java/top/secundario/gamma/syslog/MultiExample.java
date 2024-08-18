package top.secundario.gamma.syslog;

import top.secundario.gamma.syslog.tl.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiExample {
    private final AtomicInteger globalCounter = new AtomicInteger(0);
    private final Syslog syslog;

    public MultiExample() throws IOException {
        syslog = new Syslog(1000, false,
                new FileLogTransporter("MyComputer", "testApp", "D:\\gamma\\gammautil\\gammautil\\src\\example/log.txt"),
                new PrintStreamLogTransporter("MyComputer", "testApp", new SeverityLogFilter(Severity.Informational))
                //new TcpSyslogTransporter("MyComputer", "testApp", InetAddress.getByName("192.168.31.184"), 514)
                );

        for (int i = 0; i < 10; ++i) {
            Thread thread = new Thread(this::runLogGen, "LogGen" + i);
            thread.start();
        }
    }

    public static void main(String[] args) throws IOException {
        MultiExample testApp = new MultiExample();
    }

    private void runLogGen() {
        Random rand = new Random(System.currentTimeMillis());
        String procid = Thread.currentThread().getName();

        for (; ; ) {
            int nLog = rand.nextInt(1, 10);
            for (int l = 0 ; l < nLog ; ++l) {
                int seqNo = globalCounter.getAndIncrement();
                String msgFmt = "test 消息: i=%d";
                Object[] msgArgs = new Object[]{seqNo};

                if (0 != l) {
                    syslog.log(Severity.Debug, procid, AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
                } else {
                    Severity severity = Severity.fromCode(rand.nextInt(0, Severity.MAX_CODE+1));
                    syslog.log(severity, procid, AbstractLogTransporter.NILVALUE, msgFmt, msgArgs);
                }
            }

            try {
                Thread.sleep(rand.nextLong(500, 5000));
            } catch (InterruptedException ignore) {
            }

            if (Thread.currentThread().getName().equals("LogGen0")) {
                syslog.printStatistics();
            }
        }
    }
}
