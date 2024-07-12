package top.secundario.gamma.ctypes;

public class DefaultTransformer implements Transformer {

    /**
     * byte -> Byte
     * @param b
     * @return
     * @throws CTypesException
     */
    @Override
    public Object from_byte(byte b) throws CTypesException {
        return b;
    }

    /**
     * short -> Short
     * @param h
     * @return
     * @throws CTypesException
     */
    @Override
    public Object from_short(short h) throws CTypesException {
        return h;
    }

    /**
     * int -> Integer
     * @param i
     * @return
     * @throws CTypesException
     */
    @Override
    public Object from_int(int i) throws CTypesException {
        return i;
    }

    /**
     * long -> Long
     * @param l
     * @return
     * @throws CTypesException
     */
    @Override
    public Object from_long(long l) throws CTypesException {
        return l;
    }

    @Override
    public Object from(Object obj) throws CTypesException {
        return obj;
    }
}
