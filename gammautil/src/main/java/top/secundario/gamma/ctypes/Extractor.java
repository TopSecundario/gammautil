package top.secundario.gamma.ctypes;

public interface Extractor {
    public Object extract(CodeStream cs, Transformer xfrmr, Object cStructureObj) throws CTypesException;
}
