package top.secundario.gamma.types;

import top.secundario.gamma.common.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PrimitiveTransformations {

    private static final Pattern INTEGER_SEPARATOR_PATTERN = Pattern.compile("[_.,]");

    private static String removeIntegerSeparator(String intg) {
        Matcher m = INTEGER_SEPARATOR_PATTERN.matcher(intg);
        if (m.find()) {
            return m.replaceAll("");
        } else {
            return intg;
        }
    }

    private static String groupInteger(String intg, char sep) {
        int startIndex = ((intg.startsWith("-")) || (intg.startsWith("+"))) ? 2 : 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < intg.length() ; ++i) {
            int ri = intg.length() - 1 - i;                 // reversed index
            if ((i >= startIndex) && (ri % 4 == 3)) {
                sb.append(sep);
            }
            sb.append(intg.charAt(i));
        }
        return sb.toString();
    }


    public static int String_to_int(String any, String format) throws TransformationException {
        String intg = removeIntegerSeparator(any);
        integer_format int_fmt = integer_format.parse(format);

        if (! int_fmt.unsigned) {    // signed
            if ((intg.startsWith("0b")) || (intg.startsWith("0B"))) {     // binary
                try {
                    return Integer.parseInt(intg.substring(2), 2);
                } catch (NumberFormatException nfe) {
                    throw  new TransformationException(nfe, "Parse int failed: number '%s', format '%s'", any, format);
                }
            } else {                                                      // may not binary
                if (Radix.BINARY == int_fmt.radix) {                         // binary
                    try {
                        return Integer.parseInt(intg, 2);
                    } catch (NumberFormatException nfe) {
                        throw  new TransformationException(nfe, "Parse int failed: number '%s', format '%s'", any, format);
                    }
                } else {                                                     // not binary
                    if ((intg.startsWith("0x")) || (intg.startsWith("0X"))) {   // hex
                        try {
                            return Integer.parseInt(intg.substring(2), 16);
                        } catch (NumberFormatException nfe) {
                            throw  new TransformationException(nfe, "Parse int failed: number '%s', format '%s'", any, format);
                        }
                    } else {                                                    // may not hex
                        if (Radix.HEX == int_fmt.radix) {                          // hex
                            try {
                                return Integer.parseInt(intg, 16);
                            } catch (NumberFormatException nfe) {
                                throw  new TransformationException(nfe, "Parse int failed: number '%s', format '%s'", any, format);
                            }
                        } else {                                                   // dec
                            try {
                                return Integer.parseInt(intg);
                            } catch (NumberFormatException nfe) {
                                throw  new TransformationException(nfe, "Parse int failed: number '%s', format '%s'", any, format);
                            }
                        }
                    }
                }
            }
        } else {                     // unsigned
            if ((intg.startsWith("0b")) || (intg.startsWith("0B"))) {     // binary
                try {
                    return Integer.parseUnsignedInt(intg.substring(2), 2);
                } catch (NumberFormatException nfe) {
                    throw  new TransformationException(nfe, "Parse uint failed: number '%s', format '%s'", any, format);
                }
            } else {                                                      // may not binary
                if (Radix.BINARY == int_fmt.radix) {                         // binary
                    try {
                        return Integer.parseUnsignedInt(intg, 2);
                    } catch (NumberFormatException nfe) {
                        throw  new TransformationException(nfe, "Parse uint failed: number '%s', format '%s'", any, format);
                    }
                } else {                                                     // not binary
                    if ((intg.startsWith("0x")) || (intg.startsWith("0X"))) {   // hex
                        try {
                            return Integer.parseUnsignedInt(intg.substring(2), 16);
                        } catch (NumberFormatException nfe) {
                            throw  new TransformationException(nfe, "Parse uint failed: number '%s', format '%s'", any, format);
                        }
                    } else {                                                    // may not hex
                        if (Radix.HEX == int_fmt.radix) {                          // hex
                            try {
                                return Integer.parseUnsignedInt(intg, 16);
                            } catch (NumberFormatException nfe) {
                                throw  new TransformationException(nfe, "Parse uint failed: number '%s', format '%s'", any, format);
                            }
                        } else {                                                   // dec
                            try {
                                return Integer.parseUnsignedInt(intg);
                            } catch (NumberFormatException nfe) {
                                throw  new TransformationException(nfe, "Parse uint failed: number '%s', format '%s'", any, format);
                            }
                        }
                    }
                }
            }
        }
    }

    public static String intToString(int i, String format) throws TransformationException {
        return null;
    }

    private PrimitiveTransformations() {}


    private static class integer_format {
        protected final boolean unsigned;
        protected final Radix radix;
        protected final char radixFlag;
        protected final String radixPrefix;
        protected final boolean upperCase;
        protected final char separator;

        protected static final integer_format DEFAULT_FORMAT = new integer_format(false, '\0', '\0');

        protected static integer_format parse(String format) throws TransformationException {
            if (Strings.isNullOrEmpty(format)) {
                return DEFAULT_FORMAT;
            }

            boolean u = false;
            char rf = '\0';
            char sep = '\0';
            for (int i = 0 ; i < format.length() ; ++i) {
                char cu = format.charAt(i);
                switch (cu) {
                    case 'u':
                        u = true;
                        break;
                    case 'b':
                    case 'B':
                    case 'x':
                    case 'X':
                    case 'h':
                    case 'H':
                        rf = cu;
                        break;
                    case '_':
                    case '.':
                    case ',':
                        sep = cu;
                        break;
                    default:
                        throw  new TransformationException("Invalid flag '%c' in integer format '%s'", cu, format);
                }
            }

            return new integer_format(u, rf, sep);
        }

        private integer_format(boolean u, char rf, char sep) {
            unsigned = u;

            radixFlag = rf;
            switch (radixFlag) {
                case 'b':
                    radix = Radix.BINARY;
                    radixPrefix = "0b";
                    upperCase = false;
                    break;
                case 'B':
                    radix = Radix.BINARY;
                    radixPrefix = "0B";
                    upperCase = true;
                    break;
                case 'x':
                    radix = Radix.HEX;
                    radixPrefix = "0x";
                    upperCase = false;
                    break;
                case 'X':
                    radix = Radix.HEX;
                    radixPrefix = "0X";
                    upperCase = true;
                    break;
                case 'h':
                    radix = Radix.HEX;
                    radixPrefix = "";
                    upperCase = false;
                    break;
                case 'H':
                    radix = Radix.HEX;
                    radixPrefix = "";
                    upperCase = true;
                    break;
                default:
                    radix = Radix.DEC;
                    radixPrefix = "";
                    upperCase = false;
            }

            separator = sep;
        }
    }
}
