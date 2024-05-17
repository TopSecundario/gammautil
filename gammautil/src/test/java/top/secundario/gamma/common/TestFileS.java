package top.secundario.gamma.common;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;

public class TestFileS {

    private Path writeIntoTempFile(String perfix, String...contents) throws IOException {
        Path path = Files.createTempFile(perfix, null);
        PrintStream ps = new PrintStream(path.toFile(), StandardCharsets.UTF_8);
        ArrayS.forEach(contents, ps::print);
        ps.close();
        return path;
    }

    @Test
    public void test_detectLineSeparator_case1() throws IOException {
        Path path = writeIntoTempFile("test_detectLineSeparator", "hello\n", "world\n");
        String lineSeparator = FileS.detectLineSeparator(path, StandardCharsets.UTF_8);
        assertEquals("\n", lineSeparator);
    }

    @Test
    public void test_detectLineSeparator_case2() throws IOException {
        Path path = writeIntoTempFile("test_detectLineSeparator_2", "hello\r\n", "world\r\n");
        String lineSeparator = FileS.detectLineSeparator(path, StandardCharsets.UTF_8);
        assertEquals("\r\n", lineSeparator);
    }
}
