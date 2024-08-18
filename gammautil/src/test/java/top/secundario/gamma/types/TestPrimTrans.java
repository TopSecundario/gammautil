package top.secundario.gamma.types;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static top.secundario.gamma.types.PrimitiveTransformations.String_to_int;
import static top.secundario.gamma.types.PrimitiveTransformations.intToString;

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

    @Test
    public void test_intToString_0() throws TransformationException {
        assertEquals("0", intToString(0, ""));
        assertEquals("100", intToString(100, ""));
        assertEquals("-100", intToString(-100, ""));
        assertEquals("9,6500,0000", intToString(965000000, ","));
        assertEquals("-9,7065", intToString(-97065, ","));

        assertEquals("0b1010_1011_1100_1101_0000_0001_0010_0011", intToString(0xABCD_0123, "ub_"));
        assertEquals("0B111_1111_1111_1111_1111_1111_1111_1111", intToString(0x7FFF_FFFF, "B_"));

        assertEquals("abcd_0123", intToString(0xABCD_0123, "uh_"));
        assertEquals("7FFF_FFFF", intToString(0x7FFF_FFFF, "H_"));

        assertEquals("0xabcd_0123", intToString(0xABCD_0123, "ux_"));
        assertEquals("0X7FFF_FFFF", intToString(0x7FFF_FFFF, "X_"));
    }
}
