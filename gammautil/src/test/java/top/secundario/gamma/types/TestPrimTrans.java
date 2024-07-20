package top.secundario.gamma.types;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static top.secundario.gamma.types.PrimitiveTransformations.String_to_int;

public class TestPrimTrans {

    @Test
    public void test__String_to_int__0() throws TransformationException {
        assertEquals(0, String_to_int("0", ""));

        assertEquals(6500_0000, String_to_int("65,000,000", ""));
        assertEquals(-273, String_to_int("-273", ""));
        assertEquals(255, String_to_int("0xFF", ""));
        assertEquals(0x83, String_to_int("0b1000_0011", ""));

        assertEquals(0x2D82, String_to_int("0010_1101_1000_0010", "B"));
        assertEquals(0x7FFF_FFFF, String_to_int("7FFF_FFFF", "h"));

        assertEquals(0xABCD0123, String_to_int("0xABCD0123", "u"));
        assertEquals(0xABCD0123, String_to_int("ABCD_0123", "ux"));
    }
}
