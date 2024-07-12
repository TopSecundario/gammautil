package top.secundario.gamma.ctypes;

import java.util.HashMap;
import java.util.Map;

public class CEnum {
    /** value -> id for C enum items */
    private final Map<Number, String> cEnum;

    /**
     *
     * @param cEnumFmt  String like: "v1:id1, v2: id2,v3 : id3 , v4: id4"
     * @throws CTypesException
     */
    public CEnum(String cEnumFmt) throws CTypesException {
        cEnum = new HashMap<>();
        parseCMenu(cEnumFmt);
    }

    public String findCEnumItem(Object v) {
        try {
            Number vNum = (Number) v;
            for (Map.Entry<Number, String> e : cEnum.entrySet()) {
                if (vNum.longValue() == e.getKey().longValue()) {
                    return e.getValue();
                }
            }
            return null;
        } catch (ClassCastException cce) {
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        final int n = cEnum.size();
        int i = 0;
        for (Map.Entry<Number, String> e : cEnum.entrySet()) {
            sb.append(e.getKey()).append(" : ").append(e.getValue());
            if (n - 1 != i) {
                sb.append(" , ");
            }
            ++i;
        }
        sb.append("}");
        return sb.toString();
    }

    private void parseCMenu(String cEnumFmt) throws CTypesException {
        String[] tokens = cEnumFmt.split("\\s*[,:]\\s*");
        for (int i = 0 ; i < tokens.length / 2; ++i) {
            String sv = tokens[2 * i];
            long v;
            try {
                v = Long.parseUnsignedLong(sv);
            } catch (NumberFormatException nfe1) {
                if (sv.startsWith("0x") || sv.startsWith("0X")) {
                    try {
                        v = Long.parseUnsignedLong(sv.substring(2), 16);
                    } catch (NumberFormatException nfe2) {
                        throw new CTypesException(nfe2, "Invalid hex unsigned int in C enum fmt '%s' : %s", cEnumFmt, sv);
                    }
                } else {
                    throw new CTypesException(nfe1, "Invalid unsigned int in C enum fmt '%s' : %s", cEnumFmt, sv);
                }
            }

            String eid = tokens[2 * i + 1];
            cEnum.put(v, eid);
        }
    }
}
