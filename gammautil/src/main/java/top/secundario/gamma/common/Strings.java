package top.secundario.gamma.common;

import java.io.File;

public final class Strings {

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
}
