package top.secundario.gamma.ctypes;

public class Transformers {

    /** uint8 -> Short */
    static class CUInt8ToJShort extends DefaultTransformer {
        @Override
        public Object from_byte(byte b) throws CTypesException {
            return (short) (b & 0xFF);
        }
    }

    /** uint8 -> Integer */
    static class CUInt8ToJInteger extends DefaultTransformer {
        @Override
        public Object from_byte(byte b) throws CTypesException {
            return b & 0xFF;
        }
    }

    /** uint8 -> Long */
    static class CUInt8ToJLong extends DefaultTransformer {
        @Override
        public Object from_byte(byte b) throws CTypesException {
            return b & 0xFFL;
        }
    }

    /** int8 -> Short */
    static class CInt8ToJShort extends DefaultTransformer {
        @Override
        public Object from_byte(byte b) throws CTypesException {
            return (short) b;
        }
    }

    /** int8 -> Integer */
    static class CInt8ToJInteger extends DefaultTransformer {
        @Override
        public Object from_byte(byte b) throws CTypesException {
            return (int) b;
        }
    }

    /** int8 -> Long */
    static class CInt8ToJLong extends DefaultTransformer {
        @Override
        public Object from_byte(byte b) throws CTypesException {
            return (long) b;
        }
    }

    /** uint16 -> Integer */
    static class CUInt16ToJInteger extends DefaultTransformer {
        @Override
        public Object from_short(short h) throws CTypesException {
            return h & 0xFFFF;
        }
    }

    /** uint16 -> Long */
    static class CUInt16ToJLong extends DefaultTransformer {
        @Override
        public Object from_short(short h) throws CTypesException {
            return h & 0xFFFFL;
        }
    }

    /** int16 -> Integer */
    static class CInt16ToJInteger extends DefaultTransformer {
        @Override
        public Object from_short(short h) throws CTypesException {
            return (int) h;
        }
    }

    /** int16 -> Long */
    static class CInt16ToJLong extends DefaultTransformer {
        @Override
        public Object from_short(short h) throws CTypesException {
            return (long) h;
        }
    }

    /** uint32 -> Long */
    static class CUInt32ToJLong extends DefaultTransformer {
        @Override
        public Object from_int(int i) throws CTypesException {
            return (long) i & 0xFFFF_FFFFL;
        }
    }

    /** int32_t -> Long */
    static class CInt32ToJLong extends DefaultTransformer {
        @Override
        public Object from_int(int i) throws CTypesException {
            return (long) i;
        }
    }

    protected Transformers() {}
}
