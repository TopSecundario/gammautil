package top.secundario.gamma.ctypes;

public class Formatters {

    /**
     * For uint8_t
     */
    static class CUInt8Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                byte b = ((Number) v).byteValue();
                return  Integer.toString(Byte.toUnsignedInt(b));
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to u8", v);
            }
        }
    }

    /**
     * For int8_t
     */
    static class CInt8Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                byte b = ((Number) v).byteValue();
                return  Byte.toString(b);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to s8", v);
            }
        }
    }

    /**
     * For uint16_t
     */
    static class CUInt16Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                short h = ((Number) v).shortValue();
                return  Integer.toString(Short.toUnsignedInt(h));
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to u16", v);
            }
        }
    }

    /**
     * For int16_t
     */
    static class CInt16Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                short h = ((Number) v).shortValue();
                return  Short.toString(h);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to s16", v);
            }
        }
    }

    /**
     * For uint32_t
     */
    static class CUInt32Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                int i = ((Number) v).intValue();
                return  Integer.toUnsignedString(i);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to u32", v);
            }
        }
    }

    /**
     * For int32_t
     */
    static class CInt32Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                int i = ((Number) v).intValue();
                return  Integer.toString(i);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to s32", v);
            }
        }
    }

    /**
     * For uint64_t
     */
    static class CUInt64Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                long l = ((Number) v).longValue();
                return  Long.toUnsignedString(l);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to u64", v);
            }
        }
    }

    /**
     * For int64_t
     */
    static class CInt64Formatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                long l = ((Number) v).longValue();
                return  Long.toString(l);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format %s to s64", v);
            }
        }
    }


    static class JavaStyledFormatter implements Formatter {

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            try {
                return  String.format(jStyleFmtStr, v);
            } catch (Exception e) {
                throw  new CTypesException(e, "When format: %s , jStyleFmtStr '%s'", v, jStyleFmtStr);
            }
        }
    }


    static class CEnumFormatter implements Formatter {
        private final CEnum cEnum;

        public CEnumFormatter(String cEnumFmt) throws CTypesException {
            cEnum = new CEnum(cEnumFmt);
        }

        @Override
        public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
            if ("".equals(jStyleFmtStr)) {
                return  cType.basicFormatter().format(v, jStyleFmtStr, cType) + "(" + cEnum.findCEnumItem(v) + ")";
            } else {
                return String.format(jStyleFmtStr + "(%s)", v, cEnum.findCEnumItem(v));
            }
        }
    }

    protected Formatters() {}
}
