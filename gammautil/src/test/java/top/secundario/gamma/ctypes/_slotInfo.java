package top.secundario.gamma.ctypes;

import static top.secundario.gamma.ctypes.CType.*;

@CStruct
public class _slotInfo {
    @CField(ordinal = 1, ctype = uint16_t)
    public short ueID;

    @CField(ordinal = 0, ctype = uint16_t, name = "Gsfn")
    public short gsfn;

    @CField(ordinal = 2, ctype = int16_t)
    public short timeOffset;

    @CField(ordinal = 3, ctype = int16_t)
    public short reserved;

    @CField(ordinal = 4, ctype = int16_t)
    public short freqOffset;

    @CField(ordinal = 5, ctype = int16_t)
    public short EsN0;

    @CField(ordinal = 6, ctype = int16_t)
    public short RSSI;

    @CField(ordinal = 7, ctype = uint8_t)
    public short crcErrCnt;

    @CField(ordinal = 8, ctype = uint8_t)
    public short crcTotalCnt;

}
