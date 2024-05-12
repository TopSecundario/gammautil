package top.secundario.gamma.common;

public class TreeVisitResult<R> {

    private final TreeVisitIndicator _indicator;
    private final R _resultToSubTree;

    public TreeVisitResult(TreeVisitIndicator indicator, R resultToSubTree) {
        _indicator = indicator;
        _resultToSubTree = resultToSubTree;
    }

    public TreeVisitIndicator indicator() {
        return _indicator;
    }

    public R resultToSubTree() {
        return _resultToSubTree;
    }

    public R result() {
        return resultToSubTree();
    }
}
