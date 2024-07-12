package top.secundario.gamma.ctypes;

import java.util.HashMap;
import java.util.Map;

public class CStructureContext {
    /** c-structure name -> CStructureInfo */
    private Map<String, CStructures.CStructureInfo> cStructureMap;

    public CStructureContext() {
        cStructureMap = new HashMap<>();
    }

    public CStructures.CStructureInfo getCStructureInfoByName(String name) {
        return cStructureMap.get(name);
    }

    public CStructures.CStructureInfo putCStructureInfo(CStructures.CStructureInfo cStructureInfo) {
        return cStructureMap.put(cStructureInfo.name, cStructureInfo);
    }
}
