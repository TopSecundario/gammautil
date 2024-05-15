package top.secundario.gamma.common;

public class TreeVisitContext<T, R> {
    private TreeNode<T> node;
    private R dataFromParent;
    private R dataToChildren;
    private int depth;
    private int step;

    public TreeVisitContext() {
        node = null;
        dataFromParent = null;
        dataToChildren = null;
        depth = 0;
        step = 0;
    }

    public TreeNode<T> getNode() {
        return node;
    }

    public void setNode(TreeNode<T> node) {
        this.node = node;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void incDepth() {
        ++depth;
    }

    public void decDepth() {
        --depth;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void incStep() {
        ++step;
    }

    public R getDataFromParent() {
        return dataFromParent;
    }

    public void setDataFromParent(R dataFromParent) {
        this.dataFromParent = dataFromParent;
    }

    public R getDataToChildren() {
        return dataToChildren;
    }

    public void setDataToChildren(R dataToChildren) {
        this.dataToChildren = dataToChildren;
    }
}
