package top.secundario.gamma.common;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class FileS {

    public static String detectLineSeparator(Path pathToTextFile, Charset cs) {
        try (var fr = new FileReader(pathToTextFile.toFile(), cs)) {
            int ch = fr.read();
            while ((-1 != ch) && ('\r' != ch) && ('\n' != ch)) {
                ch = fr.read();
            }
            if (-1 != ch) {
                if ('\r' == ch) {
                    int ch1 = fr.read();
                    if ('\n' == ch1) {
                        return "\r\n";
                    } else {
                        return System.lineSeparator();
                    }
                } else {
                    return "\n";
                }
            } else {
                return System.lineSeparator();
            }
        } catch (IOException e) {
            System.err.printf("when detectLineSeparator for '%s' : %s%n", pathToTextFile, e);
            return System.lineSeparator();
        }
    }

    protected FileS() {}
}
