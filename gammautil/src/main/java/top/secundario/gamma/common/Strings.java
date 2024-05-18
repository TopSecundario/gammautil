package top.secundario.gamma.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Strings {
    private Strings() {}

    private static final Map<Character, Character> JAVA_ESCAPE_CHAR_MAP = new HashMap<>();
    static {
        JAVA_ESCAPE_CHAR_MAP.put('b', '\b');
        JAVA_ESCAPE_CHAR_MAP.put('t', '\t');
        JAVA_ESCAPE_CHAR_MAP.put('n', '\n');
        JAVA_ESCAPE_CHAR_MAP.put('f', '\f');
        JAVA_ESCAPE_CHAR_MAP.put('r', '\r');
        JAVA_ESCAPE_CHAR_MAP.put('s', ' ');
        JAVA_ESCAPE_CHAR_MAP.put('"', '\"');
        JAVA_ESCAPE_CHAR_MAP.put('\'', '\'');
        JAVA_ESCAPE_CHAR_MAP.put('\\', '\\');
    }


    public static boolean isNullOrEmpty(String s) {
        return  (null == s) || (s.isEmpty());
    }


    public static String normalizeDirSeparator(String pathS) {
        if (isNullOrEmpty(pathS)) {
            return pathS;
        }

        if ('\\' == File.separatorChar) {         /* Windows */
            return pathS.replace('/', '\\');
        } else {                                  /* Others */
            return pathS.replace('\\', '/');
        }
    }

    public static boolean isHexDigit(char cu) {
        return (cu >= '0' && cu <= '9') ||
                (cu >= 'A' && cu <= 'F') ||
                (cu >= 'a' && cu <= 'f');
    }

    private static char octDigitsToCharCU(char[] digits_cu, int offset, int length) {
        int sum = 0;
        int weight = 1;
        int digit = 0;
        for (int i = offset + length - 1 ; i >= offset ; --i) {
            digit = digits_cu[i] - '0';
            sum = digit * weight + sum;
            weight = weight * 8;
        }
        return (char) sum;
    }

    private static int hexDigitsToInt(char[] digits_cu, int offset, int length) {
        int sum = 0;
        int weight = 1;
        int digit = 0;
        for (int i = offset + length - 1 ; i >= offset ; --i) {
            if (digits_cu[i] <= '9')
                digit = digits_cu[i] - '0';
            else if (digits_cu[i] <= 'F')
                digit = digits_cu[i] - 'A' + 10;
            else
                digit = digits_cu[i] - 'a' + 10;
            sum = digit * weight + sum;
            weight = weight * 16;
        }
        return sum;
    }

    private static char hexDigitsToCharCU(char[] digits_cu, int offset, int length) {
        return (char) hexDigitsToInt(digits_cu, offset, length);
    }

    private static int hexDigitsCPToCharCU2(char[] digits_cu, int offset, int length, char[] cuPair) {
        int cp = hexDigitsToInt(digits_cu, offset, length);
        return Character.toChars(cp, cuPair, 0);
    }

    public static String transEscape(String src) {
        if (isNullOrEmpty(src))
            return src;

        // 0: no ; 1: simple ; 2: octal ; 3: unicode code unit(\\u1234); 4: unicode code point(#{U+1234[5[6]]})
        int escapeMode = 0;
        char[] digits_cu = new char[16];
        int digit_cnt_cu = 0;
        char cu = '\0';
        StringBuilder sb = new StringBuilder();
        char[] cu2 = new char[2];

        for (int i_cu = 0 ; i_cu < src.length() ; ++i_cu) {
            cu = src.charAt(i_cu);
            switch (escapeMode) {
                case 0:                          // no
                    if ('\\' == cu) {
                        escapeMode = 1;            // -> simple
                    } else if ('#' == cu) {
                        escapeMode = 4;            // -> unicode code point
                        digit_cnt_cu = 0;
                    } else {
                        sb.append(cu);
                    }
                    break;

                case 1:                          // simple
                    if (JAVA_ESCAPE_CHAR_MAP.containsKey(cu)) {
                        sb.append(JAVA_ESCAPE_CHAR_MAP.get(cu));
                        escapeMode = 0;            // -> no
                    } else if (cu >= '0' && cu <= '7') {
                        escapeMode = 2;            // -> octal
                        digit_cnt_cu = 0;
                        digits_cu[digit_cnt_cu++] = cu;
                    } else if ('u' == cu) {
                        escapeMode = 3;            // -> unicode code unit
                        digit_cnt_cu = 0;
                    } else {
                        throw new IllegalArgumentException("Invalid escape: '\\" +
                                cu +
                                "' in \"" +
                                src +
                                "\"");
                    }
                    break;

                case 2:                          // octal
                    if (cu >= '0' && cu <= '7') {
                        digits_cu[digit_cnt_cu++] = cu;
                        if (3 == digit_cnt_cu) {
                            char oct_cu;
                            if (digits_cu[0] > '3') {
                                oct_cu = octDigitsToCharCU(digits_cu, 0, 2);
                                sb.append(oct_cu);
                                escapeMode = 0;    // -> no
                                sb.append(digits_cu[2]);
                            } else {
                                oct_cu = octDigitsToCharCU(digits_cu, 0, digit_cnt_cu);
                                sb.append(oct_cu);
                                escapeMode = 0;    // -> no
                            }
                            digit_cnt_cu = 0;
                        }
                    } else {
                        char oct_cu = octDigitsToCharCU(digits_cu, 0, digit_cnt_cu);
                        sb.append(oct_cu);

                        if ('\\' == cu) {
                            escapeMode = 1;        // -> simple
                        } else if ('#' == cu) {
                            escapeMode = 4;        // -> unicode code point
                            digit_cnt_cu = 0;
                        } else {
                            escapeMode = 0;        // -> no
                            sb.append(cu);
                        }
                    }
                    break;

                case 3:                          // unicode code unit
                    if (isHexDigit(cu)) {
                        digits_cu[digit_cnt_cu++] = cu;
                        if (4 == digit_cnt_cu) {
                            char hex_cu = hexDigitsToCharCU(digits_cu, 0, digit_cnt_cu);
                            sb.append(hex_cu);
                            escapeMode = 0;        // -> no
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid unicode escape: '" +
                                new String(digits_cu, 0, digit_cnt_cu) +
                                cu +
                                "' in \"" +
                                src +
                                "\"");
                    }
                    break;

                case 4:                          // unicode code point (#{U+1234[5[6]]})
                    if ('#' == cu && 0 == digit_cnt_cu) {
                        sb.append('#');
                        escapeMode = 0;            // -> no
                    } else if ('{' == cu && 0 == digit_cnt_cu) {
                        digits_cu[digit_cnt_cu++] = cu;
                    } else if ('U' == cu && 1 == digit_cnt_cu) {
                        digits_cu[digit_cnt_cu++] = cu;
                    } else if ('+' == cu && 2 == digit_cnt_cu) {
                        digits_cu[digit_cnt_cu++] = cu;
                    } else if (isHexDigit(cu) && (digit_cnt_cu >= 3 && digit_cnt_cu < 9)) {
                        digits_cu[digit_cnt_cu++] = cu;
                    } else if ('}' == cu && (digit_cnt_cu >= 7 && digit_cnt_cu <= 9)) {
                        int n_cu = hexDigitsCPToCharCU2(digits_cu, 3, digit_cnt_cu - 3, cu2);
                        sb.append(cu2[0]);
                        if (2 == n_cu) {
                            sb.append(cu2[1]);
                        }
                        escapeMode = 0;            // -> no
                    } else {
                        throw new IllegalArgumentException("Invalid unicode codepoint format: '" +
                                new String(digits_cu, 0, digit_cnt_cu) +
                                cu +
                                "' in \"" +
                                src +
                                "\"");
                    }
                    break;
            }
        }

        if (0 != escapeMode) {                   // ! no
            if (2 == escapeMode) {               // octal
                char oct_cu = octDigitsToCharCU(digits_cu, 0, digit_cnt_cu);
                sb.append(oct_cu);
            } else {                             // simple ; unicode code unit ; unicode code point
                throw new IllegalArgumentException("Invalid escape: '" +
                        cu +
                        "' or '" +
                        new String(digits_cu, 0, digit_cnt_cu) +
                        "' in \"" +
                        src +
                        "\"");
            }
        }

        return sb.toString();
    }

    public static FileNameExtInfo splitFileNameExt(String fileName) {
        if (ObjectS.isNull(fileName))
            return new FileNameExtInfo(null, null, '\0');
        if (fileName.isEmpty())
            return new FileNameExtInfo("", "", '\0');

        int idx0 = fileName.lastIndexOf('.');
        if (idx0 >= 0) {
            return new FileNameExtInfo(fileName.substring(0, idx0), fileName.substring(idx0 + 1), '.');
        } else {
            int idx1 = fileName.lastIndexOf('_');
            if (idx1 >= 0) {
                return new FileNameExtInfo(fileName.substring(0, idx1), fileName.substring(idx1 + 1), '_');
            } else {
                return new FileNameExtInfo(fileName, "", '\0');
            }
        }
    }

    public static String getFileNameBase(String fileName) {
        return splitFileNameExt(fileName).base();
    }

    public static String getFileNameExt(String fileName) {
        return splitFileNameExt(fileName).ext();
    }

    public static int substringCount(String str, String ss) {
        if (ObjectS.isNull(str) || ObjectS.isNull(ss))
            return 0;

        int count = 0;
        int idx = 0;
        idx = str.indexOf(ss, idx);
        while (idx >= 0) {
            ++count;
            idx = str.indexOf(ss, idx + ss.length());
        }

        return count;
    }
}
