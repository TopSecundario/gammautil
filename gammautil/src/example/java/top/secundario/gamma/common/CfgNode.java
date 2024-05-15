package top.secundario.gamma.common;

/**
 * Config Node.
 */
public class CfgNode extends AbstractCMElement {
    private int id;


    public CfgNode(String name, int id, CfgNode parent) {
        this(name, null, id, parent);
    }

    public CfgNode(String name, String desc, int id, CfgNode parent) {
        super(name, desc, parent);
        setIdentifier(id);
    }

    public CfgNode(String name, int id, CfgModel parent) {
        this(name, null, id, parent);
    }

    public CfgNode(String name, String desc, int id, CfgModel parent) {
        super(name, desc, parent);
        setIdentifier(id);
    }

    public int getIdentifier() {
        return id;
    }

    public void setIdentifier(int id) {
        this.id = id;
    }

    public String toString() {
        return String.format("CfgNode[name='%s', desc='%s', id=%d]", getName(), getDesc(), getIdentifier());
    }
}
