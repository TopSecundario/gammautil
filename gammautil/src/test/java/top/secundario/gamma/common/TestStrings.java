package top.secundario.gamma.common;

import org.junit.Test;

import static org.junit.Assert.*;
import static java.lang.System.out;
import static java.lang.System.err;

public class TestStrings {

    @Test
    public void test_isNullOrEmpty() {
        assertTrue(Strings.isNullOrEmpty(null));
        assertTrue(Strings.isNullOrEmpty(""));
        assertFalse(Strings.isNullOrEmpty("s"));
    }

    @Test
    public void test_normalizeDirSeparator() {
        assertNull(Strings.normalizeDirSeparator(null));
        assertEquals("", Strings.normalizeDirSeparator(""));
        assertEquals("C:\\dir1\\dir2\\dir3\\file4", Strings.normalizeDirSeparator("C:\\dir1/dir2\\dir3/file4"));
    }

    @Test
    public void test_isHexDigit() {
        for (char cu = '0' ; cu <= '9' ; ++cu) {
            assertTrue(Strings.isHexDigit(cu));
        }
        for (char cu = 'A' ; cu <= 'F' ; ++cu) {
            assertTrue(Strings.isHexDigit(cu));
        }
        for (char cu = 'a' ; cu <= 'f' ; ++cu) {
            assertTrue(Strings.isHexDigit(cu));
        }
        assertFalse(Strings.isHexDigit('/'));
        assertFalse(Strings.isHexDigit(':'));
        assertFalse(Strings.isHexDigit('@'));
        assertFalse(Strings.isHexDigit('G'));
        assertFalse(Strings.isHexDigit('`'));
        assertFalse(Strings.isHexDigit('g'));
    }

    @Test
    public void test_transEscape() {
        assertNull(Strings.transEscape(null));
        assertEquals("", Strings.transEscape(""));

        String src = "no escape，没有转义";
        String translated = src;
        assertEquals(translated, Strings.transEscape(src));

        src = "\\n";
        translated = "\n";
        assertEquals(translated, Strings.transEscape(src));

        src = "种RGB水稻\\t当赛博坦农民\\r\\n";
        translated = "种RGB水稻\t当赛博坦农民\r\n";
        assertEquals(translated, Strings.transEscape(src));

        src = "\\\\";
        translated = "\\";
        assertEquals(translated, Strings.transEscape(src));

        src = "种\\122\\107\\102水稻\\t当赛博坦农民\\r\\n";
        translated = "种RGB水稻\t当赛博坦农民\r\n";
        assertEquals(translated, Strings.transEscape(src));

        src = "\\7\\77\\377\\378\\012\\477";
        translated = "\7\77\377\378\12\477";
        assertEquals(translated, Strings.transEscape(src));

        src = "种\\122\\107\\102水稻\\t当赛博坦\\u519c\\u6c11";
        translated = "种RGB水稻\t当赛博坦农民";
        assertEquals(translated, Strings.transEscape(src));

        src = "种\\122\\107\\102水稻\\t当赛博坦\\u519c\\u6c11\\nI love#{U+1F600}";
        translated = "种RGB水稻\t当赛博坦农民\nI love\uD83D\uDE00";
        assertEquals(translated, Strings.transEscape(src));

        src = "##1\\t\\12";
        translated = "#1\t\12";
        assertEquals(translated, Strings.transEscape(src));

        err.println(assertThrows(IllegalArgumentException.class, () -> Strings.transEscape("\\x")));
        err.println(assertThrows(IllegalArgumentException.class, () -> Strings.transEscape("\\t\\u123xVV")));
        err.println(assertThrows(IllegalArgumentException.class, () -> Strings.transEscape("\\122\\107\\102#{1F600}")));
        err.println(assertThrows(IllegalArgumentException.class, () -> Strings.transEscape("\\u519")));
        err.println(assertThrows(IllegalArgumentException.class, () -> Strings.transEscape("#{U+110000}WenHua")));
    }
}
