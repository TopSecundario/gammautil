package top.secundario.gamma.common;

import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.*;

public class TestRef {

    @Test
    public void test_Ref() {
        Ref<String> rs = new Ref<>();

        assertNull(rs.get());
        assertNull(rs.toString());

        String s = new String("hello");
        rs.set(s);
        assertSame(s, rs.get());
        assertEquals(s, rs.toString());

        Path p = Path.of("D:\\idea_java_wksp\\test1\\src\\adv_lang_examples");
        Ref<Path> rp = new Ref<>(p);
        assertSame(p, rp.get());
        assertEquals(p.toString(), rp.toString());
    }
}
