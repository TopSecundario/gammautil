package top.secundario.gamma.syslog;

import java.io.PrintStream;

abstract class AbstractSyslogStatistics {
    abstract int avgWriteSpinCount();
    abstract int avgWriteBufferSize();
    abstract int readSpinCount();
    abstract void setReadSpinCount(int spinCnt);
    abstract int buffersSwapCount();
    abstract int buffersSwapRate();
    abstract void printStatistics(PrintStream out);

    abstract void onWriteLog(int spinCnt);
    abstract void onRead();
    abstract void onSwapBuffers();
}
