package top.secundario.gamma.common;

public class ArrayStorageExtendException extends RuntimeException {
    private final int originCapacity;
    private final int requestedNewCapacity;
    private final int increment;

    public ArrayStorageExtendException(int oc, int rnc, int inc) {
        super(String.format("Can't extend array storage: origin capacity %d, requested new capacity %d, increment %d", oc, rnc, inc));
        originCapacity = oc;
        requestedNewCapacity = rnc;
        increment = inc;
    }
}
