package top.secundario.gamma.syslog;

import top.secundario.gamma.syslog.tl.AbstractLogTransporter;

import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Syslog {
    private volatile Buffer writeBuffer;
    private volatile Buffer readBuffer;
    private final Semaphore logCountSem;
    private volatile boolean stopTransport;
    private final List<AbstractLogTransporter> logTransporterList;
    private final AbstractSyslogStatistics logStatistics;
    private final AtomicBoolean writeLock;
    private volatile boolean isBuffersSwapping;

    public Syslog(int logBufferCapacity, boolean enableStatistics, AbstractLogTransporter... logTransporters) throws IllegalArgumentException {
        writeBuffer = new Buffer(logBufferCapacity);
        readBuffer = new Buffer(logBufferCapacity);
        logCountSem = new Semaphore(0);
        stopTransport = false;

        logTransporterList = new ArrayList<>();
        Collections.addAll(logTransporterList, logTransporters);

        logStatistics = enableStatistics ? new RealStatistics() : new FakeStatistics();

        writeLock = new AtomicBoolean(false);
        isBuffersSwapping = false;

        Thread transportThread = new Thread(this::runTransport, "Syslog Transport");
        transportThread.setPriority(Thread.NORM_PRIORITY);
        transportThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopTransport = true;
                logCountSem.release();
            }
        });
    }

    public void log(Severity severity, String procid, String msgid, String msgFmt, Object[] msgArgs) {
        while (isBuffersSwapping);
        lockWriteBuffer();

        Instant timestamp = Instant.now();
        if (writeBuffer.getLogCount() < writeBuffer.WILL_FULL_LEVEL) {
            writeBuffer.writeLog(timestamp, severity, procid, msgid, msgFmt, msgArgs);
            unlockWriteBuffer();
            logCountSem.release();
        } else if (writeBuffer.getLogCount() == writeBuffer.WILL_FULL_LEVEL) {
            writeBuffer.writeLog(timestamp, severity, procid, msgid, msgFmt, msgArgs);
            writeBuffer.writeLog(timestamp, Severity.Warning, procid, AbstractLogTransporter.NILVALUE, "May loss log!", null);
            unlockWriteBuffer();
            logCountSem.release(2);
        } else {
            unlockWriteBuffer();
            System.err.println(AbstractLogTransporter.timestampToStr(timestamp) + " Loss log, read buf size " + readBuffer.getLogCount());
        }
    }

    private void runTransport() {
        openTransporters();

        do {
            try {
                logCountSem.acquire();
            } catch (InterruptedException ignore) { }

            isBuffersSwapping = true;
            lockWriteBuffer();
            simpleSwapBuffers();
            unlockWriteBuffer();
            isBuffersSwapping = false;

            transportLogsOnAll();
            readBuffer.setLogCount(0);

        } while (!stopTransport);

        closeTransporters();
    }

    private void lockWriteBuffer() {
        int spinCnt = 0;
        while (writeLock.compareAndExchange(false, true)) {
            ++spinCnt;
        }
        logStatistics.onWriteLog(spinCnt);
    }

    private void unlockWriteBuffer() {
        while (!writeLock.compareAndExchange(true, false));
    }

    private boolean simpleSwapBuffers() {
        boolean isSwapped;
        if ((writeBuffer.getLogCount() > 0) && (readBuffer.getLogCount() == 0)) {
            Buffer buf = writeBuffer;
            writeBuffer = readBuffer;
            readBuffer = buf;
            isSwapped = true;
            logStatistics.onSwapBuffers();
        } else {
            isSwapped = false;
        }

        return isSwapped;
    }

    private void openTransporters() {
        logTransporterList.forEach(AbstractLogTransporter::openTransport);
    }

    /**
     * Transport logs on all transporters.
     */
    private void transportLogsOnAll() {
        readBuffer.readLogs(_rawLog -> {
            _rawLog.message = String.format(_rawLog.msgFmt, _rawLog.msgArgs);
            for (var transporter : logTransporterList) {
                transporter.transportLog(_rawLog);
            }
        });
    }

    private void closeTransporters() {
        logTransporterList.forEach(AbstractLogTransporter::closeTransport);
    }


    public void printStatistics() {
        logStatistics.printStatistics(System.out);
    }

    public void printLocksState() {
        System.out.printf("WLck %s%n", writeLock);
    }

    public int avgWriteSpinCount() {
        return logStatistics.avgWriteSpinCount();
    }

    public int avgWriteBufferSize() {
        return logStatistics.avgWriteBufferSize();
    }

    public int buffersSwapRate() {
        return logStatistics.buffersSwapRate();
    }


    private class RealStatistics extends AbstractSyslogStatistics {
        /* performance statistics */
        private int writeSpinSum;
        private int writeCount;
        private int m_readSpinCount;
        private int m_buffersSwapCountCurr;
        private int buffersSwapCountLast;
        private int sumOfWriteBufferSize;
        private int m_buffersSwapRate;

        RealStatistics() {
            writeSpinSum = 0;
            writeCount = 0;
            m_readSpinCount = 0;
            m_buffersSwapCountCurr = 0;
            buffersSwapCountLast = 0;
            sumOfWriteBufferSize = 0;
            m_buffersSwapRate = 0;
        }

        @Override
        int avgWriteSpinCount() {
            return (0 != writeCount) ? (writeSpinSum / writeCount) : 0;
        }

        @Override
        int avgWriteBufferSize() {
            return (0 != m_buffersSwapCountCurr) ? (sumOfWriteBufferSize / m_buffersSwapCountCurr) : 0;
        }

        @Override
        int readSpinCount() {
            return m_readSpinCount;
        }

        @Override
        void setReadSpinCount(int spinCnt) {
            m_readSpinCount = spinCnt;
        }

        @Override
        int buffersSwapCount() {
            return m_buffersSwapCountCurr;
        }

        @Override
        int buffersSwapRate() {
            return m_buffersSwapRate;
        }

        @Override
        void printStatistics(PrintStream out) {
            m_buffersSwapRate = estimateBuffersSwapRate();
            out.printf("avgWriteSpinCount = %d , buffersSwapCount = %d , buffersSwapRate = %d , avgWriteBufferSize = %d%n",
                    avgWriteSpinCount(), buffersSwapCount(), buffersSwapRate(), avgWriteBufferSize());
        }

        @Override
        void onWriteLog(int spinCnt) {
            writeSpinSum += spinCnt;
            if (writeSpinSum >= 0) {
                ++writeCount;
            } else {
                writeSpinSum = spinCnt;
                writeCount = 1;
            }
        }

        @Override
        void onRead() {

        }

        @Override
        void onSwapBuffers() {
            sumOfWriteBufferSize += readBuffer.getLogCount();
            if (sumOfWriteBufferSize > 0) {
                ++m_buffersSwapCountCurr;
            } else {
                sumOfWriteBufferSize = readBuffer.getLogCount();
                m_buffersSwapCountCurr = 1;
            }
        }

        private int estimateBuffersSwapRate() {
            int buffersSwapRate;
            if (m_buffersSwapCountCurr >= buffersSwapCountLast) {
                buffersSwapRate = m_buffersSwapCountCurr - buffersSwapCountLast;
            } else {
                buffersSwapRate = Integer.MAX_VALUE - buffersSwapCountLast + m_buffersSwapCountCurr;
            }
            buffersSwapCountLast = m_buffersSwapCountCurr;

            return buffersSwapRate;
        }
    }

    private class FakeStatistics extends AbstractSyslogStatistics {
        @Override
        int avgWriteSpinCount() {
            return 0;
        }

        @Override
        int avgWriteBufferSize() {
            return 0;
        }

        @Override
        int readSpinCount() {
            return 0;
        }

        @Override
        void setReadSpinCount(int spinCnt) {

        }

        @Override
        int buffersSwapCount() {
            return 0;
        }

        @Override
        int buffersSwapRate() {
            return 0;
        }

        @Override
        void printStatistics(PrintStream out) {

        }

        @Override
        void onWriteLog(int spinCnt) {

        }

        @Override
        void onRead() {

        }

        @Override
        void onSwapBuffers() {

        }
    }
}
