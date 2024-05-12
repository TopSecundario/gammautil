package top.secundario.gamma.common;

public class TreeVisitContext<T, R> {
    private TreeNode<T> node;
    private R result;
    private int depth;
    private int step;

    public TreeVisitContext() {
        node = null;
        result = null;
        depth = 0;
        step = 0;
    }

    public TreeNode<T> getNode() {
        return node;
    }

    public void setNode(TreeNode<T> node) {
        this.node = node;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
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
}
