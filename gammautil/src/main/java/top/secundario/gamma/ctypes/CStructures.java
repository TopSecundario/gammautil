package top.secundario.gamma.ctypes;

import top.secundario.gamma.common.CollectionS;
import top.secundario.gamma.common.Ref;

import java.io.PrintStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.secundario.gamma.ctypes.ByteOrder.BIG_ENDIAN;
import static top.secundario.gamma.ctypes.CType.*;
import static top.secundario.gamma.ctypes.OutputFormat.DefaultOutputFormat;

public class CStructures {

    static class ByteExtractor implements Extractor {
        private static final int nBytes = 1;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            byte byt = cs.bytes[cs.cursor];
            cs.advance(nBytes);
            return xfrmr.from_byte(byt);
        }
    }

    static class BEShortExtractor implements Extractor {
        private static final int nBytes = 2;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            int h = (cs.bytes[cs.cursor] << 8) | (cs.bytes[cs.cursor + 1] & 0x00FF) ;
            cs.advance(nBytes);
            return xfrmr.from_short((short) h);
        }
    }

    static class LEShortExtractor implements Extractor {
        private static final int nBytes = 2;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            int h = (cs.bytes[cs.cursor + 1] << 8) | (cs.bytes[cs.cursor] & 0x00FF) ;
            cs.advance(nBytes);
            return xfrmr.from_short((short) h);
        }
    }

    static class BEIntExtractor implements Extractor {
        private static final int nBytes = 4;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            int i =  (cs.bytes[cs.cursor    ] << 24)                |
                    ((cs.bytes[cs.cursor + 1] << 16) & 0x00FF_0000) |
                    ((cs.bytes[cs.cursor + 2] << 8)  & 0x0000_FF00) |
                     (cs.bytes[cs.cursor + 3]        & 0x0000_00FF) ;
            cs.advance(nBytes);
            return xfrmr.from_int(i);
        }
    }

    static class LEIntExtractor implements Extractor {
        private static final int nBytes = 4;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            int i =  (cs.bytes[cs.cursor + 3] << 24)                |
                    ((cs.bytes[cs.cursor + 2] << 16) & 0x00FF_0000) |
                    ((cs.bytes[cs.cursor + 1] << 8)  & 0x0000_FF00) |
                     (cs.bytes[cs.cursor    ]        & 0x0000_00FF) ;
            cs.advance(nBytes);
            return xfrmr.from_int(i);
        }
    }

    static class BELongExtractor implements Extractor {
        private static final int nBytes = 8;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            long l = ((long) cs.bytes[cs.cursor    ] << 56)                           |
                    (((long) cs.bytes[cs.cursor + 1] << 48) & 0x00FF_0000_0000_0000L) |
                    (((long) cs.bytes[cs.cursor + 2] << 40) & 0x0000_FF00_0000_0000L) |
                    (((long) cs.bytes[cs.cursor + 3] << 32) & 0x0000_00FF_0000_0000L) |
                    (((long) cs.bytes[cs.cursor + 4] << 24) & 0x0000_0000_FF00_0000L) |
                    (((long) cs.bytes[cs.cursor + 5] << 16) & 0x0000_0000_00FF_0000L) |
                    (((long) cs.bytes[cs.cursor + 6] << 8)  & 0x0000_0000_0000_FF00L) |
                    ( (long) cs.bytes[cs.cursor + 7]        & 0x0000_0000_0000_00FFL) ;
            cs.advance(nBytes);
            return xfrmr.from_long(l);
        }
    }

    static class LELongExtractor implements Extractor {
        private static final int nBytes = 8;
        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            reqNBytes(cs, nBytes);
            long l = ((long) cs.bytes[cs.cursor + 7] << 56)                           |
                    (((long) cs.bytes[cs.cursor + 6] << 48) & 0x00FF_0000_0000_0000L) |
                    (((long) cs.bytes[cs.cursor + 5] << 40) & 0x0000_FF00_0000_0000L) |
                    (((long) cs.bytes[cs.cursor + 4] << 32) & 0x0000_00FF_0000_0000L) |
                    (((long) cs.bytes[cs.cursor + 3] << 24) & 0x0000_0000_FF00_0000L) |
                    (((long) cs.bytes[cs.cursor + 2] << 16) & 0x0000_0000_00FF_0000L) |
                    (((long) cs.bytes[cs.cursor + 1] << 8)  & 0x0000_0000_0000_FF00L) |
                    ( (long) cs.bytes[cs.cursor    ]        & 0x0000_0000_0000_00FFL) ;
            cs.advance(nBytes);
            return xfrmr.from_long(l);
        }
    }

    static class StructExtractor implements Extractor {
        CStructureInfo cStructInfo;

        public StructExtractor(CStructureInfo cStructInfo) {
            this.cStructInfo = cStructInfo;
        }

        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            return  unpackStruct(cStructInfo, cs);
        }
    }

    static class FixedLengthArrayExtractor implements Extractor {
        private final int fixedArrayLength;
        private final Class<?> clzArrayItemType;
        private final Extractor itemExtractor;

        public FixedLengthArrayExtractor(Class<?> clzArrItem, int fixedArrLen, Extractor itemExtractor) {
            clzArrayItemType = clzArrItem;
            fixedArrayLength = fixedArrLen;
            this.itemExtractor = itemExtractor;
        }

        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            try {
                Object arrObj = Array.newInstance(clzArrayItemType, fixedArrayLength);
                for (int i = 0 ; i < fixedArrayLength ; ++i) {
                    Object itemObj = itemExtractor.extract(cs, xfrmr, cStructureObj);
                    Array.set(arrObj, i, itemObj);
                }

                return arrObj;
            } catch (Exception e) {
                throw new CTypesException(e, "Exception when extract array");
            }
        }
    }

    static class VariableLengthArrayExtractor implements Extractor {
        private final Field jFieldForArrayLength;
        private final Class<?> clzArrayItemType;
        private final Extractor itemExtractor;

        public VariableLengthArrayExtractor(Class<?> clzArrItem, Field jFieldForArrLen, Extractor itemExtractor) {
            this.clzArrayItemType = clzArrItem;
            this.jFieldForArrayLength = jFieldForArrLen;
            this.itemExtractor = itemExtractor;
        }

        @Override
        public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException {
            try {
                int arrayLength = jFieldForArrayLength.getInt(cStructureObj);

                if (arrayLength >= 0) {
                    FixedLengthArrayExtractor flae = new FixedLengthArrayExtractor(clzArrayItemType, arrayLength, itemExtractor);
                    return  flae.extract(cs, xfrmr, cStructureObj);
                } else {
                    throw  new CTypesException("Field '%s' for array length: %d", jFieldForArrayLength, arrayLength);
                }

            } catch (Exception e) {
                throw  new CTypesException(e, "Exception when extract array");
            }
        }
    }

    /* pre-defined extractors */
    private static final ByteExtractor byteExtractor = new ByteExtractor();
    private static final BEShortExtractor beShortExtractor = new BEShortExtractor();
    private static final LEShortExtractor leShortExtractor = new LEShortExtractor();
    private static final BEIntExtractor beIntExtractor = new BEIntExtractor();
    private static final LEIntExtractor leIntExtractor = new LEIntExtractor();
    private static final BELongExtractor beLongExtractor = new BELongExtractor();
    private static final LELongExtractor leLongExtractor = new LELongExtractor();

    /* pre-defined formatters */
    private static final Formatter javaStyledFormatter = new Formatters.JavaStyledFormatter();


    public record ArrayLengthInfo(int fixedLength, CFieldInfo varLength) {}

    public static class CFieldInfo {
        public final int ordinal;
        public final String name;
        /** for array, it is item's type */
        public final CType ctype;
        ByteOrder byteOrder;
        public final Field jField;
        Extractor extractor;
        /** if ctype is struct */
        public final CStructureInfo structureInfo;
        /** fixed-length array and its length indicator */
        public final int fixedArrLen;
        /** variable-length array and its length indicator */
        public final CFieldInfo varArrLen;
        /** Java-style format string */
        public final String format;
        /** applied formatter, for primitive/basic type */
        public final Formatter appliedPrimFmtr;
        /** applied transformer, for primitive/basic type */
        public final Transformer appliedPrimXfrmr;

        public CFieldInfo(int ordinal,
                          String name,
                          CType ctype,
                          ByteOrder byteOrder,
                          Field jField,
                          CStructureInfo cStructureInfo,
                          ArrayLengthInfo arrLen,
                          String fmt,
                          Formatter primFmtr,
                          Transformer primXfrmr)
                throws CTypesException
        {
            this.ordinal = ordinal;
            this.name = name;
            this.ctype = ctype;
            this.byteOrder = byteOrder;
            this.jField = jField;
            this.structureInfo = cStructureInfo;
            if (null != arrLen) {
                this.fixedArrLen = arrLen.fixedLength;
                this.varArrLen = arrLen.varLength;
            } else {
                this.fixedArrLen = 0;
                this.varArrLen = null;
            }
            this.format = fmt;
            this.appliedPrimFmtr = primFmtr;
            this.appliedPrimXfrmr = primXfrmr;

            associateExtractor();
        }

        private Extractor getBasicExtractor() throws CTypesException {
            if (ctype == uint8_t || ctype == int8_t) {
                return byteExtractor;
            } else if (ctype == uint16_t || ctype == int16_t) {
                return  (BIG_ENDIAN == byteOrder) ? beShortExtractor : leShortExtractor;
            } else if (ctype == uint32_t || ctype == int32_t) {
                return  (BIG_ENDIAN == byteOrder) ? beIntExtractor : leIntExtractor;
            } else if (ctype == uint64_t || ctype == int64_t) {
                return  (BIG_ENDIAN == byteOrder) ? beLongExtractor : leLongExtractor;
            } else if (ctype == struct) {
                return new StructExtractor(structureInfo);
            } else {
                throw  new CTypesException("No extractor for c-field %s(%s)", name, ctype);
            }
        }

        private void associateExtractor() throws CTypesException {
            Extractor basicExtractor = getBasicExtractor();
            if (! isArray()) {
                this.extractor = basicExtractor;
            } else {
                /* Array */
                Class<?> clzArrItem = jField.getType().getComponentType();
                if (fixedArrLen > 0) {
                    this.extractor = new FixedLengthArrayExtractor(clzArrItem, fixedArrLen, basicExtractor);
                } else if (null != varArrLen) {
                    this.extractor = new VariableLengthArrayExtractor(clzArrItem, varArrLen.jField, basicExtractor);
                } else {
                    throw  new CTypesException("Doesn't understand: fixedArrLen %d, varArrLen %s !", fixedArrLen, varArrLen);
                }
            }
        }

        public boolean isArray() {
            return  (fixedArrLen > 0) || (null != varArrLen);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CFieldInfo{");
            sb.append("ordinal=").append(ordinal);
            sb.append(", name='").append(name).append('\'');
            sb.append(", ctype=").append(ctype);
            sb.append(", byteOrder=").append(byteOrder);
            sb.append(", fixedArrLen=").append(fixedArrLen);
            sb.append(", varArrLen=").append((null != varArrLen) ? varArrLen.name : null);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class CStructureInfo {
        public final String name;
        ByteOrder byteOrder;
        int pack;
        public final List<CFieldInfo> fieldList;
        public final Class<?> jType;

        public CStructureInfo(String name, ByteOrder byteOrder, int pack, List<CFieldInfo> fieldList, Class<?> jType) {
            this.name = name;
            this.byteOrder = byteOrder;
            this.pack = pack;
            this.fieldList = fieldList;
            this.jType = jType;
        }
    }


    public static CStructureInfo parse(Class<?> clzCStructure) throws CTypesException {
        return parse(clzCStructure, null);
    }

    public static CStructureInfo parse(Class<?> clzCStructure, ByteOrder overridedByteOrder) throws CTypesException {
        CStructureContext ctx = new CStructureContext();

        return parseCStructure(clzCStructure, overridedByteOrder, ctx);
    }

    public static <T> T unpack(Class<T> clzT, byte[] bytes, int offset, int length) throws CTypesException {
        CStructureInfo cStructInfo = parse(clzT, null);
        CodeStream cs = new CodeStream(bytes, offset, length);

        return (T) unpackStruct(cStructInfo, cs);
    }

    public static Object unpack(CStructureInfo cStructInfo, byte[] bytes, int offset, int length) throws CTypesException {
        return unpackStruct(cStructInfo, new CodeStream(bytes, offset, length));
    }

    private static Object unpackStruct(CStructureInfo cStructInfo, CodeStream cs) throws CTypesException {
        try {
            Constructor<?> constructor = cStructInfo.jType.getDeclaredConstructor();
            Object cStructObj = constructor.newInstance();

            for (CFieldInfo cFieldInfo : cStructInfo.fieldList) {
                Object val = cFieldInfo.extractor.extract(cs, cFieldInfo.appliedPrimXfrmr, cStructObj);
                cFieldInfo.jField.set(cStructObj, val);
            }

            return cStructObj;
        } catch (NoSuchMethodException nsme) {
            throw  new CTypesException(nsme, "Miss constructor %s() !", cStructInfo.jType.getSimpleName());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException  ex) {
            throw  new CTypesException(ex, "Failed to construct %s !", cStructInfo.jType.getName());
        }
    }


    public static void dump(Object cStructureObj, CStructureInfo cStructureInfo) throws CTypesException {
        dump(cStructureObj, cStructureInfo, System.out);
    }

    public static void dump(Object cStructureObj, CStructureInfo cStructureInfo, PrintStream output) throws CTypesException {
        dump(cStructureObj, cStructureInfo, DefaultOutputFormat, output);
    }

    public static void dump(Object cStructureObj, CStructureInfo cStructureInfo, OutputFormat of, PrintStream output)
            throws CTypesException
    {
        dump(cStructureObj, cStructureInfo, of, output, 0);
    }

    public static void dump(Object cStructureObj, CStructureInfo cStructureInfo, OutputFormat of, PrintStream output, int initHierarchy)
            throws CTypesException
    {
        reqCStructureObject(cStructureObj, cStructureInfo);

        dumpStruct("", cStructureObj, cStructureInfo, output, initHierarchy, of);
    }

    private static void dumpStruct(String cFieldName,
                                   Object cStructObj,
                                   CStructureInfo cStructInfo,
                                   PrintStream output,
                                   int hierarchy,
                                   OutputFormat of) throws CTypesException
    {
        try {
            /* struct head */
            OutputFormat.CStructHeadGenerator shg = of.getCStructHeadGenerator();
            String sh = shg.generateHead(cFieldName, cStructInfo, cStructObj, hierarchy, of);
            output.print(sh);

            /* fields */
            final int nFields = cStructInfo.fieldList.size();
            for (int iField = 0 ; iField < nFields ; ++iField) {
                CFieldInfo cFieldInfo = cStructInfo.fieldList.get(iField);

                if (! cFieldInfo.isArray()) {   // NOT array
                    if (cFieldInfo.ctype.primitive()) {           // primitive
                        Object v = cFieldInfo.jField.get(cStructObj);
                        String fv = cFieldInfo.appliedPrimFmtr.format(v, cFieldInfo.format, cFieldInfo.ctype);
                        OutputFormat.CFieldGenerator fg = of.getCFieldGenerator();
                        String f = fg.generate(cFieldInfo.name, fv, cFieldInfo, hierarchy + 1, of);
                        output.print(f);
                    } else if (struct == cFieldInfo.ctype) {      // struct
                        Object v = cFieldInfo.jField.get(cStructObj);
                        dumpStruct(cFieldInfo.name, v, cFieldInfo.structureInfo, output, hierarchy + 1, of);
                    } else {
                        throw  new CTypesException("%s generation will be supported in future", cFieldInfo.ctype);
                    }

                } else {                        // Array
                    Object arrObj = cFieldInfo.jField.get(cStructObj);
                    final int actualArrayLength = Array.getLength(arrObj);

                    /* array head */
                    OutputFormat.CArrayHeadGenerator ahg = of.getCArrayHeadGenerator();
                    String ah = ahg.generateHead(cFieldInfo, actualArrayLength, hierarchy + 1, of);
                    output.print(ah);

                    if (cFieldInfo.ctype.primitive()) {         // primitive array
                        if (null != of.getCArrayItemGenerator()) {    // use Array Item generator
                            OutputFormat.CArrayItemGenerator aig = of.getCArrayItemGenerator();
                            for (int iItem = 0; iItem < actualArrayLength; ++iItem) {
                                Object v = Array.get(arrObj, iItem);
                                String fv = cFieldInfo.appliedPrimFmtr.format(v, cFieldInfo.format, cFieldInfo.ctype);
                                String ai = aig.generateItem(iItem, fv, cFieldInfo.name, cFieldInfo, hierarchy + 2, of);
                                output.print(ai);

                                /* array item separator */
                                if (iItem != actualArrayLength - 1) {
                                    output.print(of.getCArrayItemSeparator());
                                }
                            }
                        } else {                                      // use Array Item Prompt and Field generator
                            OutputFormat.CArrayItemPromptGenerator aipg = of.getCArrayItemPromptGenerator();
                            OutputFormat.CFieldGenerator fg = of.getCFieldGenerator();
                            for (int iItem = 0; iItem < actualArrayLength; ++iItem) {
                                Object v = Array.get(arrObj, iItem);
                                String fv = cFieldInfo.appliedPrimFmtr.format(v, cFieldInfo.format, cFieldInfo.ctype);

                                String aip = aipg.generateItemPrompt(cFieldInfo, iItem, hierarchy + 2, of);
                                String aif = fg.generate(aip, fv, cFieldInfo, hierarchy + 2, of);
                                output.print(aif);

                                /* array item separator */
                                if (iItem != actualArrayLength - 1) {
                                    output.print(of.getCArrayItemSeparator());
                                }
                            }
                        }
                    } else if (struct == cFieldInfo.ctype) {    // struct array
                        OutputFormat.CArrayItemPromptGenerator aipg = of.getCArrayItemPromptGenerator();
                        for (int iItem = 0; iItem < actualArrayLength; ++iItem) {
                            Object v = Array.get(arrObj, iItem);

                            String aip = aipg.generateItemPrompt(cFieldInfo, iItem, hierarchy + 2, of);
                            dumpStruct(aip, v, cFieldInfo.structureInfo, output, hierarchy + 2, of);

                            /* array item separator */
                            if (iItem != actualArrayLength - 1) {
                                output.print(of.getCArrayItemSeparator());
                            }
                        }
                    } else {
                        throw  new CTypesException("%s generation will be supported in future", cFieldInfo.ctype);
                    }

                    /* array tail */
                    OutputFormat.CArrayTailGenerator atg = of.getCArrayTailGenerator();
                    String at = atg.generateTail(cFieldInfo, hierarchy + 1, of);
                    output.print(at);
                }

                /* field separator */
                if (iField != nFields - 1) {
                    output.print(of.getCFieldSeparator());
                }
            }

            /* struct tail */
            OutputFormat.CStructTailGenerator stg = of.getCStructTailGenerator();
            String st = stg.generateTail(cStructInfo, cStructObj, hierarchy, of);
            output.print(st);

        } catch (Exception e) {
            throw  new CTypesException(e, "dump struct exception");
        }
    }


    public static int length(CStructureInfo cStructInfo) throws CTypesException {
        return structLength(cStructInfo);
    }

    public static int length(Class<?> clzCStructure) throws CTypesException {
        return length(parse(clzCStructure));
    }

    private static int structLength(CStructureInfo cStructInfo) throws CTypesException {
        int len = 0;     // struct total length

        for (CFieldInfo cFieldInfo : cStructInfo.fieldList) {
            if (! cFieldInfo.isArray()) {                  // NOT array
                if (cFieldInfo.ctype.primitive()) {            // primitive
                    len = len + cFieldInfo.ctype.byteLength();
                } else if (struct == cFieldInfo.ctype) {
                    len = len + structLength(cFieldInfo.structureInfo);
                } else {
                    throw  new CTypesException("Unexpected ctype: " + cFieldInfo.ctype);
                }
            } else {                                       // Array
                if (cFieldInfo.fixedArrLen > 0) {             // fixed-length array
                    /* item length */
                    int n;
                    if (cFieldInfo.ctype.primitive()) {            // primitive array
                        n = cFieldInfo.ctype.byteLength();
                    } else if (struct == cFieldInfo.ctype) {       // struct array
                        n = structLength(cFieldInfo.structureInfo);
                    } else {
                        throw  new CTypesException("Unexpected ctype: " + cFieldInfo.ctype);
                    }

                    /* array length */
                    len = len + (n * cFieldInfo.fixedArrLen);
                } else {                                      // variable-length array
                    throw  new CTypesException("Can't calc struct length, because %s is var-len array!", cFieldInfo.name);
                }
            }
        }

        return len;
    }


    private static CStructureInfo parseCStructure(Class<?> clzT, ByteOrder overridedByteOrder, CStructureContext ctx) throws CTypesException {
        CStructureInfo cStructInfo = null;
        CStruct anno_CStruct = clzT.getAnnotation(CStruct.class);
        if (null != anno_CStruct) {
            //System.out.println(anno_CStruct);
            String cStructName = (anno_CStruct.name().equals("")) ? clzT.getSimpleName() : anno_CStruct.name();

            cStructInfo = ctx.getCStructureInfoByName(cStructName);
            if (null == cStructInfo) {
                ByteOrder cStructByteOrder = (null != overridedByteOrder) ? overridedByteOrder : anno_CStruct.byteOrder();

                List<CFieldInfo> cFieldInfoList = parseCFields(clzT, cStructName, cStructByteOrder, ctx, overridedByteOrder);

                cStructInfo = new CStructureInfo(cStructName, cStructByteOrder, anno_CStruct.pack(), cFieldInfoList, clzT);
                ctx.putCStructureInfo(cStructInfo);
            }
        } else {
            throw  new CTypesException("Type '%s' doesn't represent c-struct! need CStruct annotation.", clzT.getName());
        }

        return cStructInfo;
    }

    private static List<CFieldInfo> parseCFields(Class<?> clzT,
                                                 String cStructureName,
                                                 ByteOrder cStructureByteOrder,
                                                 CStructureContext ctx,
                                                 ByteOrder overridedByteOrder) throws CTypesException
    {
        List<CFieldInfo> cFieldInfoList = new ArrayList<>();

        /* parse c-fields */
        Field[] fields = clzT.getFields();
        for (Field jField : fields) {
            int modifiers = jField.getModifiers();
            if ((! Modifier.isStatic(modifiers)) && (! Modifier.isFinal(modifiers))) {    // !static && !final
                CField anno_CField = jField.getAnnotation(CField.class);
                if (null != anno_CField) {
                    //System.out.println(anno_CField);
                    String cFieldName = (anno_CField.name().equals("")) ? jField.getName() : anno_CField.name();

                    System.out.println(jField.getType());
                    CType cRawFieldType = anno_CField.ctype();
                    Ref<CStructureInfo> refCStructureInfo = new Ref<>();
                    CType cMappedFieldType = parseType(cRawFieldType, jField, ctx, overridedByteOrder, refCStructureInfo);

                    ArrayLengthInfo arrLenInfo = null;
                    if (jField.getType().isArray()) {
                        arrLenInfo = parseArrayLength(anno_CField, cFieldInfoList);
                    }

                    Formatter appliedFormatter = parseFormatter(cMappedFieldType, anno_CField.format(), anno_CField.enumFmt(), anno_CField.formatter());
                    Transformer appliedTransformer = parseTransformer(cMappedFieldType, jField.getType(), null);

                    CFieldInfo cFieldInfo = new CFieldInfo(anno_CField.ordinal(),
                            cFieldName,
                            cMappedFieldType,
                            cStructureByteOrder,
                            jField,
                            refCStructureInfo.get(),
                            arrLenInfo,
                            anno_CField.format(),
                            appliedFormatter,
                            appliedTransformer);

                    cFieldInfoList.add(cFieldInfo);
                }
            }
        }

        /* fields post check */
        if (! cFieldInfoList.isEmpty()) {         // fields number
            /* fields ordinal */
            cFieldInfoList.sort((_cf1, _cf2) -> {return Integer.compare(_cf1.ordinal, _cf2.ordinal);});
            for (int i = 0 ; i < cFieldInfoList.size() ; ++i) {
                if (cFieldInfoList.get(i).ordinal != i) {
                    throw new CTypesException("CField %s.%s ordinal error! expected %d, actual %d", cStructureName,
                            cFieldInfoList.get(i).name, i, cFieldInfoList.get(i).ordinal);
                }
                System.out.println(cFieldInfoList.get(i));
            }
        } else {
            System.err.printf("WARN: Number of valid c-field in c-struct %s is 0!%n", cStructureName);
        }

        return cFieldInfoList;
    }


    private static void reqNBytes(CodeStream cs, int nBytes) throws CTypesException {
        if (! cs.isSatisfy(nBytes)) {
            throw  new CTypesException("require %d bytes failed from code stream!", nBytes);
        }
    }

    private static void reqArrayLength(int arrLen) throws CTypesException {
        if (arrLen <= 0) {
            throw  new CTypesException("Meaningless array length: " + arrLen);
        }
    }

    private static void reqCStructureObject(Object cStructureObj, CStructureInfo cStructureInfo) throws CTypesException {
        Objects.requireNonNull(cStructureObj);
        Objects.requireNonNull(cStructureInfo);

        if (cStructureObj.getClass() != cStructureInfo.jType) {
            throw  new CTypesException("Unexpected type '%s', need: %s", cStructureObj.getClass(), cStructureInfo.jType);
        }
    }

    private static CType parseType(CType rawCType,
                                   Field jField,
                                   CStructureContext ctx,
                                   ByteOrder overridedByteOrder,
                                   Ref<CStructureInfo> refCStructureInfo) throws CTypesException
    {
        CType mappedCType;
        Class<?> _jType = jField.getType();
        Class<?> jType = _jType.isArray() ? _jType.getComponentType() : _jType;

        if (rawCType.primitive()) {
            if (rawCType.supportJavaType(jType)) {
                mappedCType = rawCType;
            } else {
                throw  new CTypesException("Java field '%s'(%s) incompatible with c-type %s", jField.getName(), jType, rawCType);
            }

        } else if (AS_JAVA_TYPE == rawCType) {
            try {
                mappedCType = CType.fromJavaBasicType(jType);
            } catch (IllegalArgumentException iae) {
                /* parse field type as structure */
                CStructureInfo cStructureInfo = parseCStructure(jType, overridedByteOrder, ctx);
                mappedCType = struct;
                refCStructureInfo.set(cStructureInfo);
            }

        } else if (struct == rawCType) {
            /* parse field type as structure */
            CStructureInfo cStructInfo = parseCStructure(jType, overridedByteOrder, ctx);
            mappedCType = struct;
            refCStructureInfo.set(cStructInfo);
        } else {
            throw  new CTypesException("CType %s will be supported in future", rawCType);
        }

        return mappedCType;
    }

    private static ArrayLengthInfo parseArrayLength(CField anno_CField, List<CFieldInfo> cFieldInfoList) throws CTypesException {
        final String rawLenInd = anno_CField.arrLenInd();
        int fixedLen = 0;
        CFieldInfo varLen_CField = null;

        try {
            fixedLen = Integer.parseUnsignedInt(rawLenInd);
            reqArrayLength(fixedLen);
        } catch (NumberFormatException nfe0) {
            if (rawLenInd.startsWith("0x") || rawLenInd.startsWith("0X")) {
                try {
                    fixedLen = Integer.parseUnsignedInt(rawLenInd.substring(2), 16);
                    reqArrayLength(fixedLen);
                } catch (NumberFormatException nfe1) {
                    throw  new CTypesException("Invalid hex int for fixed-len array in annotated filed '%s' : %s", anno_CField, rawLenInd);
                }
            } else {
                varLen_CField = CollectionS.search(cFieldInfoList, (_f) -> {return rawLenInd.equals(_f.name);});
                if (null != varLen_CField) {
                    Class<?> jType = varLen_CField.jField.getType();
                    if ((jType != byte.class) && (jType != short.class) && (jType != int.class)) {
                        throw new CTypesException("'%s' incompatible with array len indicator '%s' in annotation %s",
                                varLen_CField.jField, rawLenInd, anno_CField);
                    }
                } else {
                    throw  new CTypesException("'%s' is invalid var-len array indicator in annotation %s, must predefine.", rawLenInd, anno_CField);
                }
            }
        }

        return  new ArrayLengthInfo(fixedLen, varLen_CField);
    }

    private static Formatter parseFormatter(CType ctype, String format, String enumFmt, Class<? extends Formatter> clzFormatter)
            throws CTypesException
    {
        if (ctype.primitive()) {
            /* C primitives */
            if (clzFormatter == Formatter.class || clzFormatter == Formatters.JavaStyledFormatter.class) {
                /* apply builtin formatters */
                if ("".equals(enumFmt)) {
                    /* not enum */
                    if ("".equals(format)) {
                        return ctype.basicFormatter();
                    } else {
                        return javaStyledFormatter;
                    }
                } else {
                    /* enum */
                    return new Formatters.CEnumFormatter(enumFmt);
                }
            } else {
                /* apply specified formatter */
                try {
                    Constructor<? extends Formatter> ctor = clzFormatter.getDeclaredConstructor();
                    return ctor.newInstance();
                } catch (Exception e) {
                    throw  new CTypesException(e, "Create formatter fail: " + clzFormatter);
                }
            }
        } else if (struct == ctype) {
            System.out.println("struct doesn't support formatter!");
            return null;
        } else {
            throw  new CTypesException("Unexpected CType: " + ctype);
        }
    }

    private static Transformer parseTransformer(CType ctype, Class<?> jtype, Class<? extends Transformer> clzTransformer)
        throws CTypesException
    {
        if (ctype.primitive()) {
            Class<?> jBasicType = jtype.isArray() ? jtype.getComponentType() : jtype;
            try {
                return ctype.basicTransformer(jBasicType);
            } catch (IllegalArgumentException iae) {
                throw  new CTypesException(iae, "When parse transformer");
            }
        } else if (struct == ctype) {
            System.out.println("struct doesn't support transformer!");
            return null;
        } else {
            throw  new CTypesException("Unexpected CType: " + ctype);
        }
    }


    protected CStructures() {}
}
