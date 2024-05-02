package top.secundario.gamma.common;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestObjectS {

    @Test
    public void test_isNull() {
        Object obj0 = null;

        assertTrue(ObjectS.isNull(obj0));

        assertTrue(ObjectS.isNotNull(new Object()));
    }
}
