package top.secundario.gamma.ctypes;

/*
Mode1: none/basic;
Mode2: Java-style format string;
Mode3: Class<? extends Formatter>
 */

public interface Formatter {
    public String format(Object v, String jStyleFmtStr, CType cType)
            throws CTypesException;
}
