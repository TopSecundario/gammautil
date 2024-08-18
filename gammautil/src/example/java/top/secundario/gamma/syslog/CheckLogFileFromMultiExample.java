package top.secundario.gamma.syslog;

import top.secundario.gamma.syslog.tl.AbstractLogTransporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CheckLogFileFromMultiExample {
    private static final int MAX_LINE_NUM = 1000_0000;
    private static final int[] integrityBitMap = new int[(MAX_LINE_NUM + 31) / 32];

    private static void setIntegrityBitMap(int index) {
        int loc = index / 32;
        int offset = index % 32;
        int mask = (1 << offset);
        integrityBitMap[loc] |= mask;
    }

    private static boolean checkIntegrityBitMap(int index) {
        int loc = index / 32;
        int offset = index % 32;
        int mask = (1 << offset);
        return  0 != (integrityBitMap[loc] & mask);
    }

    /*
    <DEBUG>2024-08-16T09:47:01.318456+08:00[LogGen2] test msg: i=0
     */
    private static LocalDateTime parseLogLine(String logLine) {
        String strTimestamp = logLine.substring(logLine.indexOf('>') + 1 , logLine.indexOf('['));
        LocalDateTime timestamp = LocalDateTime.parse(strTimestamp, AbstractLogTransporter.SYSLOG_DATE_TIME_FMT);

        try {
            String strIndex = logLine.substring(logLine.indexOf('=') + 1);
            int index = Integer.parseInt(strIndex);
            setIntegrityBitMap(index);
        } catch (NumberFormatException nfe) {
            System.err.println("May loss log!");
        }

        return timestamp;
    }

    private static void checkLogFile(String pathToLogFile) throws IOException {
        LocalDateTime ldtLast = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());

        /* parse log file and check time continuous */
        int lineNo = 0;
        try (var fr = new FileReader(pathToLogFile, StandardCharsets.UTF_8); var br = new BufferedReader(fr))
        {
            String logLine = br.readLine();
            while (null != logLine) {
                ++lineNo;
                if (lineNo > MAX_LINE_NUM) {
                    System.err.println("line no exceed " + MAX_LINE_NUM);
                    break;
                }

                LocalDateTime ldtCurr = parseLogLine(logLine);
                if (ldtCurr.isBefore(ldtLast)) {
                    System.err.printf("%s isBefore %s at line%d%n", ldtCurr, ldtLast, lineNo);
                }
                ldtLast = ldtCurr;

                logLine = br.readLine();
            }
        }

        /* check integrity */
        System.out.println("Checked max line no: " + lineNo);
        for (int index = 0 ; index < lineNo ; ++index) {
            if (! checkIntegrityBitMap(index)) {
                System.err.println("Loss index " + index);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        checkLogFile("D:\\gamma\\gammautil\\gammautil\\src\\example/log.txt");
    }
}
