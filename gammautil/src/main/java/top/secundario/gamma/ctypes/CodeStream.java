package top.secundario.gamma.ctypes;

public class CodeStream {
    public final byte[] bytes;
    public final int offset;
    protected int cursor;
    public final int length;
    public final int limit;

    public CodeStream(byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
        this.cursor = this.offset;
        this.limit = this.offset + this.length;
    }

    public void advance(int adv) {
        this.cursor += adv;
    }

    public boolean isSatisfy(int nBytes) {
        return  (this.limit - this.cursor) >= nBytes;
    }
}
