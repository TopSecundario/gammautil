package top.secundario.gamma.gui.dbx;

public class DBXException extends Exception {
    public DBXException(String msgFmt, Object...msgArgs) {
        this(null, msgFmt, msgArgs);
    }

    public DBXException(Throwable cause, String msgFmt, Object...msgArgs) {
        super(String.format(msgFmt, msgArgs), cause);
    }
}
