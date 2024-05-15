package top.secundario.gamma.common;

public class CfgItem extends CfgNode {
    public CfgItem(String name, int id, CfgNode parent) {
        this(name, null, id, parent);
    }

    public CfgItem(String name, String desc, int id, CfgNode parent) {
        super(name, desc, id, parent);
    }

    public String toString() {
        return String.format("CfgItem[name='%s', desc='%s', id=%d]", getName(), getDesc(), getIdentifier());
    }
}
