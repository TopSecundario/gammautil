package top.secundario.gamma.ctypes;

public class CTypesException extends Exception {
    public CTypesException(String msgFmt, Object...msgArgs) {
        this(null, msgFmt, msgArgs);
    }

    public CTypesException(Throwable cause, String msgFmt, Object...msgArgs) {
        super(String.format(msgFmt, msgArgs), cause);
    }
}
