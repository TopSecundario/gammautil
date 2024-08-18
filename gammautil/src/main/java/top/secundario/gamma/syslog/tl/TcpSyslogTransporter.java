package top.secundario.gamma.syslog.tl;

import top.secundario.gamma.common.Strings;
import top.secundario.gamma.syslog.Facility;
import top.secundario.gamma.syslog.RawLog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

public class TcpSyslogTransporter extends NwkSyslogTransporter {
    private Socket socket;

    public TcpSyslogTransporter(String hostname, String appName, InetAddress remoteAddr, int remotePort)
            throws IllegalArgumentException, IOException
    {
        this(hostname, appName, ZoneId.systemDefault(), Facility.LOCAL0, remoteAddr, remotePort);
    }

    public TcpSyslogTransporter(String hostname, String appName, ZoneId timeZone, Facility facility,
                                InetAddress remoteAddr, int remotePort) throws IllegalArgumentException, IOException
    {
        this(hostname, appName, timeZone, facility, remoteAddr, remotePort, null, 0);
    }

    public TcpSyslogTransporter(String hostname, String appName, ZoneId timeZone, Facility facility,
                                InetAddress remoteAddr, int remotePort, InetAddress localAddr, int localPort)
            throws IllegalArgumentException, IOException
    {
        super(hostname, appName, timeZone, facility);

        socket = new Socket(remoteAddr, remotePort, localAddr, localPort);
        out = socket.getOutputStream();
    }

    @Override
    public void openTransport() {

    }

    @Override
    public void transportLog(RawLog rawLog) {
        try {
            transportSyslogStream(rawLog);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void closeTransport() {
        try {
            socket.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        } finally {
            out = null;
            socket = null;
        }
    }

    private void transportSyslogStream(RawLog rawLog) throws IOException, IllegalArgumentException {
        /*======================HEADER=======================*/
        /* PRI */
        out.write(ASCII_PRIVAL_START);
        out.write(privalToAscii(prival(rawLog.severity)));
        out.write(ASCII_PRIVAL_END);
        /* VERSION SP */
        out.write(ascii_version);
        out.write(SP);
        /* TIMESTAMP SP */
        out.write(timestampToAscii(rawLog.timestamp));
        out.write(SP);
        /* HOSTNAME SP */
        out.write(ascii_hostname);
        out.write(SP);
        /* APP-NAME SP */
        out.write(ascii_appName);
        out.write(SP);
        /* PROCID SP */
        if (!Strings.isNullOrEmpty(rawLog.procid)) {
            out.write(strToPrintUsAscii(rawLog.procid, MAX_PROCID_LEN));
        } else {
            out.write(ASCII_NILVALUE);
        }
        out.write(SP);
        /* MSGID */
        if (!Strings.isNullOrEmpty(rawLog.msgid)) {
            out.write(strToPrintUsAscii(rawLog.msgid, MAX_MSGID_LEN));
        } else {
            out.write(ASCII_NILVALUE);
        }

        /*======================SP STRUCTURED-DATA=======================*/
        out.write(SP);
        out.write(ASCII_NILVALUE);

        /*======================SP MSG=======================*/
        out.write(SP);
        /* BOM UTF-8-STRING */
        out.write(UTF8_BOM);
        out.write(rawLog.message.getBytes(StandardCharsets.UTF_8));

        /* ! Extra ZERO */
        out.write(ASCII_ZERO);
    }
}
