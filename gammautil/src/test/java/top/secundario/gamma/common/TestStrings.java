package top.secundario.gamma.common;

import org.junit.Test;

import static org.junit.Assert.*;

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
}
