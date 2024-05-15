package top.secundario.gamma.common;

public class CfgModel extends AbstractCMElement {
    private String version;

    public CfgModel(String name) {
        this(name, null);
    }

    public CfgModel(String name, String desc) {
        super(name, desc);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return String.format("CfgModel[name='%s', desc='%s', version='%s']", getName(), getDesc(), getVersion());
    }
}
