package top.secundario.gamma.common;

/**
 * Abstract Config Model Element.
 */
public abstract class AbstractCMElement extends TreeNode<AbstractCMElement> {
    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    protected AbstractCMElement(String name) {
        this(name, null);
    }

    protected AbstractCMElement(String name, String desc) {
        super(null);
        init(name, desc);
    }

    protected AbstractCMElement(String name, String desc, AbstractCMElement parent) {
        super(null, parent);
        init(name, desc);
    }

    private void init(String name, String desc) {
        setData(this);
        setName(name);
        setDesc(desc);
    }
}
