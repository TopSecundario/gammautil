package top.secundario.gamma.common;

public class TreePostVisitContext<T, R> {
    private TreeNode<T> node;
    private R dataFromParent;
    private int depth;
    private int step;

    public TreePostVisitContext() {
        this.node = null;
        this.dataFromParent = null;
        this.depth = 0;
        this.step = 0;
    }

    public TreePostVisitContext(TreeNode<T> node, R dataFromParent, int depth, int step) {
        this.node = node;
        this.dataFromParent = dataFromParent;
        this.depth = depth;
        this.step = step;
    }

    public TreeNode<T> getNode() {
        return node;
    }

    public void setNode(TreeNode<T> node) {
        this.node = node;
    }

    public R getDataFromParent() {
        return dataFromParent;
    }

    public void setDataFromParent(R dataFromParent) {
        this.dataFromParent = dataFromParent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
