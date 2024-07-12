package top.secundario.gamma.ctypes;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;
import static top.secundario.gamma.ctypes.ByteOrder.LITTLE_ENDIAN;
import static top.secundario.gamma.ctypes.CType.*;

@CStruct(byteOrder = LITTLE_ENDIAN)
class bar {
    @CField(ordinal = 0, format = "0x%X")
    public short bh;

    @CField(ordinal = 1,format = "0x%X(%<d)", ctype = uint8_t)
    public short bByt;
}

class FreqFormatter implements Formatter {
    @Override
    public String format(Object v, String jStyleFmtStr, CType cType) throws CTypesException {
        long l = ((Number) v).longValue() / 1000;
        return l + " kHz";
    }
}

@CStruct
class foo {
    @CField(ordinal = 0, ctype = uint8_t)
    public short byt;

    @CField(ordinal = 1, ctype = int16_t, format = "0x%04X", enumFmt = "0: feature1 , 1: feature2 , 2: feature3, 3:feature4")
    public short h;

    @CField(ordinal = 2, ctype = uint32_t)
    public int i;

    @CField(ordinal = 3, ctype = uint64_t, formatter = FreqFormatter.class)
    public long l;

    @CField(ordinal = 4)
    public bar obj_bar;

    @CField(ordinal = 5, ctype = uint8_t)
    public short aByt_Len;

    @CField(ordinal = 6, arrLenInd = "aByt_Len", ctype = uint16_t, format = "0x%X")
    public short[] aByt;

    @CField(ordinal = 7, arrLenInd = "0x2")
    public bar[] aObjBars;
}

public class TestCTypes {

    @Test
    public void test_AllInOne() throws Exception {
        byte[] bytes = new byte[]{
                0x01,
                0x00, 0x02,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFD,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04,
                0x00, 0x05,
                0x06,
                0x03,
                0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C,

                (byte) 0xA2,0x34, 0x56,
                0x78, (byte) 0x90, (byte) 0xAB,
        };


        CStructures.CStructureInfo csi = CStructures.parse(foo.class);
        Object f = CStructures.unpack(csi, bytes, 0, bytes.length);

        try (var baos = new ByteArrayOutputStream();
             var ps = new PrintStream(baos, true, StandardCharsets.UTF_8))
        {
            CStructures.dump(f, csi, ps);

            String actual = baos.toString(StandardCharsets.UTF_8);
            String expected = Files.readString(Path.of("src/test/resources/dump.txt"), StandardCharsets.UTF_8);
            assertEquals(expected, actual);
        }

        try (var baos = new ByteArrayOutputStream();
             var ps = new PrintStream(baos, true, StandardCharsets.UTF_8))
        {
            CStructures.dump(f, csi, JSONOutputFormat.JSON_OF, ps);

            String actual = baos.toString(StandardCharsets.UTF_8);
            String expected = Files.readString(Path.of("src/test/resources/dump.json"), StandardCharsets.UTF_8);
            assertEquals(expected, actual);
        }

        assertEquals(3, CStructures.length(bar.class));
        assertEquals(16, CStructures.length(_slotInfo.class));
    }
}
