package top.secundario.gamma.ctypes;

public class OutputFormat {

    /* interface for generators */

    @FunctionalInterface
    public static interface CStructHeadGenerator {
        public String generateHead(String cFieldName, CStructures.CStructureInfo cStructInfo, Object cStructObj, int hierarchy, OutputFormat of) throws CTypesException;
    }

    @FunctionalInterface
    public static interface CStructTailGenerator {
        public String generateTail(CStructures.CStructureInfo cStructInfo, Object cStructObj, int hierarchy, OutputFormat of) throws CTypesException;
    }

    @FunctionalInterface
    public static interface CFieldGenerator {
        public String generate(String cFieldName, String formattedValue, CStructures.CFieldInfo cFieldInfo, int hierarchy, OutputFormat of) throws CTypesException;
    }

    @FunctionalInterface
    public static interface CArrayHeadGenerator {
        public String generateHead(CStructures.CFieldInfo arrayCFieldInfo, int arrayActualLength, int hierarchy, OutputFormat of) throws CTypesException;
    }

    @FunctionalInterface
    public static interface CArrayTailGenerator {
        public String generateTail(CStructures.CFieldInfo arrayCFieldInfo, int hierarchy, OutputFormat of) throws CTypesException;
    }

    @FunctionalInterface
    public static interface CArrayItemPromptGenerator {
        public String generateItemPrompt(CStructures.CFieldInfo arrayCFieldInfo, int itemIndex, int hierarchy, OutputFormat of) throws CTypesException;
    }

    @FunctionalInterface
    public static interface CArrayItemGenerator {
        public String generateItem(int itemIndex, String formattedValue, String cFieldName, CStructures.CFieldInfo arrayCFieldInfo, int hierarchy, OutputFormat of) throws CTypesException;
    }


    /* default generators */

    public static final CStructHeadGenerator DefaultCStructHeadGenerator = new CStructHeadGenerator() {
        @Override
        public String generateHead(String cFieldName, CStructures.CStructureInfo cStructInfo, Object cStructObj, int hierarchy, OutputFormat of) throws CTypesException {
            return  of.generateIndent(hierarchy) + cFieldName + "(struct " + cStructInfo.name + ")\n";
        }
    };

    public static final CStructTailGenerator DefaultCStructTailGenerator = new CStructTailGenerator() {
        @Override
        public String generateTail(CStructures.CStructureInfo cStructInfo, Object cStructObj, int hierarchy, OutputFormat of) throws CTypesException {
            return "";
        }
    };

    public static final CFieldGenerator DefaultCFieldGenerator = new CFieldGenerator() {
        @Override
        public String generate(String cFieldName, String formattedValue, CStructures.CFieldInfo cFieldInfo, int hierarchy, OutputFormat of) throws CTypesException {
            return  of.generateIndent(hierarchy) + cFieldName + " : " + formattedValue + "\n";
        }
    };

    public static final CArrayHeadGenerator DefaultCArrayHeadGenerator = new CArrayHeadGenerator() {
        @Override
        public String generateHead(CStructures.CFieldInfo arrayCFieldInfo, int arrayActualLength, int hierarchy, OutputFormat of) throws CTypesException {
            return  of.generateIndent(hierarchy) + arrayCFieldInfo.name + "[" + arrayActualLength + "]\n";
        }
    };

    public static final CArrayItemPromptGenerator DefaultCArrayItemPromptGenerator = new CArrayItemPromptGenerator() {
        @Override
        public String generateItemPrompt(CStructures.CFieldInfo arrayCFieldInfo, int itemIndex, int hierarchy, OutputFormat of) throws CTypesException {
            return  arrayCFieldInfo.name + "[" + itemIndex + "]";
        }
    };

    public static final CArrayTailGenerator DefaultCArrayTailGenerator = new CArrayTailGenerator() {
        @Override
        public String generateTail(CStructures.CFieldInfo arrayCFieldInfo, int hierarchy, OutputFormat of) throws CTypesException {
            return "";
        }
    };

    protected CStructHeadGenerator      cStructHeadGenerator;
    protected CStructTailGenerator      cStructTailGenerator;
    protected String                    cFieldSeparator;
    protected CFieldGenerator           cFieldGenerator;
    protected CArrayHeadGenerator       cArrayHeadGenerator;
    protected CArrayItemPromptGenerator cArrayItemPromptGenerator;
    protected String                    cArrayItemSeparator;
    protected CArrayTailGenerator       cArrayTailGenerator;
    protected String                    indent;
    /** Array item, could be null, primitive validate only */
    protected CArrayItemGenerator       cArrayItemGenerator;


    public OutputFormat() {
        cStructHeadGenerator = DefaultCStructHeadGenerator;
        cStructTailGenerator = DefaultCStructTailGenerator;
        cFieldSeparator = "";
        cFieldGenerator = DefaultCFieldGenerator;
        cArrayHeadGenerator = DefaultCArrayHeadGenerator;
        cArrayItemPromptGenerator = DefaultCArrayItemPromptGenerator;
        cArrayItemSeparator = "";
        cArrayTailGenerator = DefaultCArrayTailGenerator;
        indent = "|---";
        cArrayItemGenerator = null;
    }

    public CStructHeadGenerator getCStructHeadGenerator() {
        return cStructHeadGenerator;
    }

    public void setCStructHeadGenerator(CStructHeadGenerator cStructHeadGenerator) {
        this.cStructHeadGenerator = cStructHeadGenerator;
    }

    public CStructTailGenerator getCStructTailGenerator() {
        return cStructTailGenerator;
    }

    public void setCStructTailGenerator(CStructTailGenerator cStructTailGenerator) {
        this.cStructTailGenerator = cStructTailGenerator;
    }

    public String getCFieldSeparator() {
        return cFieldSeparator;
    }

    public void setCFieldSeparator(String cFieldSeparator) {
        this.cFieldSeparator = cFieldSeparator;
    }

    public CFieldGenerator getCFieldGenerator() {
        return cFieldGenerator;
    }

    public void setCFieldGenerator(CFieldGenerator cFieldGenerator) {
        this.cFieldGenerator = cFieldGenerator;
    }

    public CArrayHeadGenerator getCArrayHeadGenerator() {
        return cArrayHeadGenerator;
    }

    public void setCArrayHeadGenerator(CArrayHeadGenerator cArrayHeadGenerator) {
        this.cArrayHeadGenerator = cArrayHeadGenerator;
    }

    public CArrayItemPromptGenerator getCArrayItemPromptGenerator() {
        return cArrayItemPromptGenerator;
    }

    public void setCArrayItemPromptGenerator(CArrayItemPromptGenerator cArrayItemPromptGenerator) {
        this.cArrayItemPromptGenerator = cArrayItemPromptGenerator;
    }

    public String getCArrayItemSeparator() {
        return cArrayItemSeparator;
    }

    public void setCArrayItemSeparator(String cArrayItemSeparator) {
        this.cArrayItemSeparator = cArrayItemSeparator;
    }

    public CArrayTailGenerator getCArrayTailGenerator() {
        return cArrayTailGenerator;
    }

    public void setCArrayTailGenerator(CArrayTailGenerator cArrayTailGenerator) {
        this.cArrayTailGenerator = cArrayTailGenerator;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String generateIndent(int hierarchy) {
        return indent.repeat(hierarchy);
    }

    public CArrayItemGenerator getCArrayItemGenerator() {
        return cArrayItemGenerator;
    }

    public void setCArrayItemGenerator(CArrayItemGenerator cArrayItemGenerator) {
        this.cArrayItemGenerator = cArrayItemGenerator;
    }


    public static final OutputFormat DefaultOutputFormat = new OutputFormat();
}
