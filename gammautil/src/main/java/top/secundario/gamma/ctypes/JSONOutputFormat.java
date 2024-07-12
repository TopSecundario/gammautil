package top.secundario.gamma.ctypes;

public class JSONOutputFormat extends OutputFormat {

    public static final CStructHeadGenerator JSONCStructHeadGenerator = new CStructHeadGenerator() {
        @Override
        public String generateHead(String cFieldName, CStructures.CStructureInfo cStructInfo, Object cStructObj, int hierarchy, OutputFormat of) throws CTypesException {
            return  "{\"" + cFieldName + "\":{\"C\":\"struct " + cStructInfo.name + "\",\"FL\":[";
        }
    };

    public static final CStructTailGenerator JSONCStructTailGenerator = new CStructTailGenerator() {
        @Override
        public String generateTail(CStructures.CStructureInfo cStructInfo, Object cStructObj, int hierarchy, OutputFormat of) throws CTypesException {
            return  "]}}";
        }
    };

    public static final CFieldGenerator JSONCFieldGenerator = new CFieldGenerator() {
        @Override
        public String generate(String cFieldName, String formattedValue, CStructures.CFieldInfo cFieldInfo, int hierarchy, OutputFormat of) throws CTypesException {
            return  "{\"" + cFieldName + "\":\"" + formattedValue + "\"}";
        }
    };

    public static final CArrayHeadGenerator JSONCArrayHeadGenerator = new CArrayHeadGenerator() {
        @Override
        public String generateHead(CStructures.CFieldInfo arrayCFieldInfo, int arrayActualLength, int hierarchy, OutputFormat of) throws CTypesException {
            return  "{\"" + arrayCFieldInfo.name + "\":[";
        }
    };

    public static final CArrayItemPromptGenerator JSONCArrayItemPromptGenerator = new CArrayItemPromptGenerator() {
        @Override
        public String generateItemPrompt(CStructures.CFieldInfo arrayCFieldInfo, int itemIndex, int hierarchy, OutputFormat of) throws CTypesException {
            return  arrayCFieldInfo.name + "[" + itemIndex + "]";
        }
    };

    public static final CArrayTailGenerator JSONCArrayTailGenerator = new CArrayTailGenerator() {
        @Override
        public String generateTail(CStructures.CFieldInfo arrayCFieldInfo, int hierarchy, OutputFormat of) throws CTypesException {
            return  "]}";
        }
    };

    public static final CArrayItemGenerator JSONCArrayItemGenerator = new CArrayItemGenerator() {
        @Override
        public String generateItem(int itemIndex, String formattedValue, String cFieldName, CStructures.CFieldInfo arrayCFieldInfo, int hierarchy, OutputFormat of) throws CTypesException {
            return  "\"" + formattedValue + "\"";
        }
    };


    public JSONOutputFormat() {
        cStructHeadGenerator = JSONCStructHeadGenerator;
        cStructTailGenerator = JSONCStructTailGenerator;
        cFieldSeparator = ",";
        cFieldGenerator = JSONCFieldGenerator;
        cArrayHeadGenerator = JSONCArrayHeadGenerator;
        cArrayItemPromptGenerator = JSONCArrayItemPromptGenerator;
        cArrayItemSeparator = ",";
        cArrayTailGenerator = JSONCArrayTailGenerator;
        indent = "";
        cArrayItemGenerator = JSONCArrayItemGenerator;
    }


    public static final OutputFormat JSON_OF = new JSONOutputFormat();
}
