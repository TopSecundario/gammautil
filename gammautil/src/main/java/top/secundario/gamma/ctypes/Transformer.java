package top.secundario.gamma.ctypes;

public interface Transformer {
    public Object from_byte(byte b) throws CTypesException;
    public Object from_short(short h) throws CTypesException;
    public Object from_int(int i) throws CTypesException;
    public Object from_long(long l) throws CTypesException;

    public Object from(Object obj) throws CTypesException;
}
