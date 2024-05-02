package top.secundario.gamma.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSomeGUI {

    @Test
    public void test_testThreadName() {
        System.out.println();
        assertEquals("main", Thread.currentThread().getName());
    }
}
