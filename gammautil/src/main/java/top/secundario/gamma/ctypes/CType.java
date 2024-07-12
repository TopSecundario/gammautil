package top.secundario.gamma.ctypes;

import static top.secundario.gamma.ctypes.TriState.*;

public enum CType {
    /* C primitives */
    uint8_t(true, TRUE, 1,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(byte.class, new DefaultTransformer()),
                    new CompatibleJavaTypeInfo(short.class, new Transformers.CUInt8ToJShort()),
                    new CompatibleJavaTypeInfo(int.class, new Transformers.CUInt8ToJInteger()),
                    new CompatibleJavaTypeInfo(long.class, new Transformers.CUInt8ToJLong())},
            new Class[]{},
            new Formatters.CUInt8Formatter()),
    int8_t(true, FALSE, 1,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(byte.class, new DefaultTransformer()),
                    new CompatibleJavaTypeInfo(short.class, new Transformers.CInt8ToJShort()),
                    new CompatibleJavaTypeInfo(int.class, new Transformers.CInt8ToJInteger()),
                    new CompatibleJavaTypeInfo(long.class, new Transformers.CInt8ToJLong())},
            new Class[]{byte.class},
            new Formatters.CInt8Formatter()),
    uint16_t(true, TRUE, 2,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(short.class, new DefaultTransformer()),
                    new CompatibleJavaTypeInfo(int.class, new Transformers.CUInt16ToJInteger()),
                    new CompatibleJavaTypeInfo(long.class, new Transformers.CUInt16ToJLong())},
            new Class[]{},
            new Formatters.CUInt16Formatter()),
    int16_t(true, FALSE, 2,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(short.class, new DefaultTransformer()),
                    new CompatibleJavaTypeInfo(int.class, new Transformers.CInt16ToJInteger()),
                    new CompatibleJavaTypeInfo(long.class, new Transformers.CInt16ToJLong())},
            new Class[]{short.class},
            new Formatters.CInt16Formatter()),
    uint32_t(true, TRUE, 4,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(int.class, new DefaultTransformer()),
                    new CompatibleJavaTypeInfo(long.class, new Transformers.CUInt32ToJLong())},
            new Class[]{},
            new Formatters.CUInt32Formatter()),
    int32_t(true, FALSE, 4,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(int.class, new DefaultTransformer()),
                    new CompatibleJavaTypeInfo(long.class, new Transformers.CInt32ToJLong())},
            new Class[]{int.class},
            new Formatters.CInt32Formatter()),
    uint64_t(true, TRUE, 8,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(long.class, new DefaultTransformer())},
            new Class[]{},
            new Formatters.CUInt64Formatter()),
    int64_t(true, FALSE, 8,
            new CompatibleJavaTypeInfo[]{
                    new CompatibleJavaTypeInfo(long.class, new DefaultTransformer())},
            new Class[]{long.class},
            new Formatters.CInt64Formatter()),

    /* C compound types */
    struct(false, UNDEF, -1, new CompatibleJavaTypeInfo[]{}, new Class[]{}, null),     // NOTICE struct

    /** SPECIAL: As Java type, NOTICE the mapping rule */
    AS_JAVA_TYPE(false, UNDEF, -1, new CompatibleJavaTypeInfo[]{}, new Class[]{}, null);       // TODO: to check


    private static record CompatibleJavaTypeInfo(Class compatibleJavaType, Transformer basicTransformer) {}

    private final boolean _primitive;
    private final TriState _unsigned;
    private final int _byteLength;
    private final CompatibleJavaTypeInfo[] _compatibleJavaTypesInfo;
    private final Class[] _fromJavaTypes;
    private final Formatter _basicFormatter;

    private CType(boolean prim, TriState u, int bytLen, CompatibleJavaTypeInfo[] javaTypesInfo, Class[] fromJTypes, Formatter basicFmtr) {
        _primitive = prim;
        _unsigned = u;
        _byteLength = bytLen;
        _compatibleJavaTypesInfo = javaTypesInfo;
        _fromJavaTypes = fromJTypes;
        _basicFormatter = basicFmtr;
    }

    public boolean primitive() {
        return _primitive;
    }

    public TriState unsigned() {
        return _unsigned;
    }

    public int byteLength() {
        return _byteLength;
    }

    public boolean supportJavaType(Class clz) {
        for (CompatibleJavaTypeInfo cjti : _compatibleJavaTypesInfo) {
            if (cjti.compatibleJavaType == clz) {
                return true;
            }
        }
        return false;
    }

    public Transformer basicTransformer(Class clz) throws IllegalArgumentException {
        for (CompatibleJavaTypeInfo cjti : _compatibleJavaTypesInfo) {
            if (cjti.compatibleJavaType == clz) {
                return cjti.basicTransformer;
            }
        }

        throw new IllegalArgumentException(String.format("%s -> %s : no basic transformer!", this, clz.getSimpleName()));
    }

    public Formatter basicFormatter() {
        return _basicFormatter;
    }

    public static CType fromJavaBasicType(Class clz) throws IllegalArgumentException {
        for (CType e : values()) {
            for (Class c : e._fromJavaTypes) {
                if (c == clz) {
                    return e;
                }
            }
        }
        throw  new IllegalArgumentException("Can't map Java basic type to C primitive type: " + clz);
    }
}
