package top.secundario.gamma.common;

import java.util.function.Predicate;

public class TreeNode<T> {
    private T data;

    private TreeNode<T> parent;
    private TreeNode<T> firstChild;
    private TreeNode<T> previous;
    private TreeNode<T> next;


    public TreeNode() {
        this(null);
    }

    public TreeNode(T data) {
        setData(data);
        linkEmpty();
    }

    public TreeNode(T data, TreeNode<T> parent) {
        setData(data);
        link(parent);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public <V extends T> V getDataAs(Class<V> clazzV) {
        return (V) data;
    }

    public TreeNode<T> addChildData(T childData) {
        return  new TreeNode<>(childData, this);
    }

    public TreeNode<T> addFirstChildData(T childData) {
        TreeNode<T> newNode = new TreeNode<>(childData);
        newNode.linkFirstChild(this);
        return newNode;
    }

    public TreeNode<T> addNextData(T nextData) throws IllegalArgumentException {
        TreeNode<T> newNode = new TreeNode<>(nextData);
        newNode.linkAfter(this);
        return newNode;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public T getParentData() {
        return parent.data;
    }

    public TreeNode<T> getFirstChild() {
        return firstChild;
    }

    public TreeNode<T> getPrevious() {
        return previous;
    }

    public TreeNode<T> getNext() {
        return next;
    }

    public TreeNode<T> getLastChild() {
        TreeNode<T> lastChild = null;
        TreeNode<T> cursor = firstChild;
        while (ObjectS.isNotNull(cursor)) {
            lastChild = cursor;
            cursor = cursor.next;
        }
        return lastChild;
    }

    public boolean isRoot() {
        return ObjectS.isNull(parent);
    }

    public boolean isLeaf() {
        return ObjectS.isNull(firstChild);
    }

    public boolean isLeaf(Predicate<? super T> predicate) {
        return predicate.test(data);
    }

    public void simpleWalk(TreeSimpleVisitor<? super T> visitor) {
        _simpleWalk(this, visitor);
    }

    public TreeVisitIndicator controllableWalk(TreeControllableVisitor<? super T> visitor) {
        TreeVisitor<T, Object> wrappedVisitor = (_data, _tvCtx) -> {return visitor.visit(_data);};

        TreeVisitContext<T, Object> tvCtx = new TreeVisitContext<>();
        tvCtx.incDepth();
        TreeVisitIndicator indicator = _walk(this, wrappedVisitor, tvCtx,
                (_data, _tpvCtx) -> {return TreeVisitIndicator.AS_PRE_VISIT;}, new TreePostVisitContext<>());
        tvCtx.decDepth();

        return indicator;
    }

    public <R> TreeVisitIndicator walk(TreeVisitor<T, R> visitor) {
        return walk(visitor, new TreeVisitContext<>());
    }

    public <R> TreeVisitIndicator walk(TreeVisitor<T, R> visitor, TreeVisitContext<T, R> tvCtx) {
        return walk(visitor, tvCtx, (_data, _tpvCtx) -> {return TreeVisitIndicator.AS_PRE_VISIT;});
    }

    public <R> TreeVisitIndicator walk(TreeVisitor<T, R> visitor, TreeVisitContext<T, R> tvCtx, TreePostVisitor<T, R> postVisitor) {
        if (ObjectS.isNull(tvCtx)) {
            tvCtx = new TreeVisitContext<>();
        }

        tvCtx.incDepth();
        TreeVisitIndicator indicator = _walk(this, visitor, tvCtx, postVisitor, new TreePostVisitContext<>());
        tvCtx.decDepth();

        return indicator;
    }

    private static <T> void _simpleWalk(TreeNode<T> node, TreeSimpleVisitor<? super T> visitor) {
        /* visit itself */
        visitor.visit(node.data);

        /* visit children */
        TreeNode<T> child = node.firstChild;
        while (null != child) {
            _simpleWalk(child, visitor);      /* visit a child */
            child = child.next;
        }
    }

    private static <T, R> TreeVisitIndicator _walk(TreeNode<T> node,
                                                   TreeVisitor<T, R> visitor,
                                                   TreeVisitContext<T, R> tvCtx,
                                                   TreePostVisitor<T, R> postVisitor,
                                                   TreePostVisitContext<T, R> tpvCtx)
    {
        /* visit itself */
        tvCtx.setNode(node);
        tvCtx.incStep();
        R savedDataFromParent = tvCtx.getDataFromParent();
        int savedDepth = tvCtx.getDepth();
        int savedStep = tvCtx.getStep();
        TreeVisitIndicator indicator = visitor.visit(node.data, tvCtx);
        if (TreeVisitIndicator.TERMINATE == indicator) {
            return indicator;            /* TERMINATE */
        }

        if (TreeVisitIndicator.SKIP_SUB_TREE != indicator) {
            /* visit children */
            R dataToChildren = tvCtx.getDataToChildren();
            TreeNode<T> child = node.firstChild;
            while (null != child) {
                tvCtx.setDataFromParent(dataToChildren);
                tvCtx.incDepth();
                TreeVisitIndicator ind = _walk(child, visitor, tvCtx, postVisitor, tpvCtx);      /* visit a child */
                tvCtx.decDepth();
                if (TreeVisitIndicator.TERMINATE == ind) {
                    return ind;                  /* TERMINATE */
                }
                if (TreeVisitIndicator.SKIP_SIBLING == ind) {
                    break;
                }
                child = child.next;
            }
        }

        /* post visit */
        tpvCtx.setNode(node);
        tpvCtx.setDataFromParent(savedDataFromParent);
        tpvCtx.setDepth(savedDepth);
        tpvCtx.setStep(savedStep);
        TreeVisitIndicator indicator_ = postVisitor.postVisit(node.data, tpvCtx);

        return  (TreeVisitIndicator.AS_PRE_VISIT == indicator_) ? indicator : indicator_;
    }

    public TreeNode<T> ascend(int nHierarchy) {
        TreeNode<T> current = this;
        for (int i = 0 ; i < nHierarchy ; ++i) {
            current = current.parent;
            if (ObjectS.isNull(current))
                break;
        }
        return current;
    }

    protected void link(TreeNode<T> parent) {
        this.parent = parent;
        this.firstChild = null;

        TreeNode<T> lastChildOfParent = parent.getLastChild();
        if (ObjectS.isNotNull(lastChildOfParent)) {
            lastChildOfParent.next = this;
            this.previous = lastChildOfParent;
        } else {
            parent.firstChild = this;
            this.previous = null;
        }
        this.next = null;
    }

    protected void linkFirstChild(TreeNode<T> parent) {
        this.parent = parent;
        this.firstChild = null;
        this.previous = null;

        if (ObjectS.isNotNull(parent.firstChild)) {
            parent.firstChild.previous = this;
            this.next = parent.firstChild;
        } else {
            this.next = null;
        }
        parent.firstChild = this;
    }

    protected void linkAfter(TreeNode<T> previousNode) throws IllegalArgumentException {
        if (previousNode.isRoot())
            throw  new IllegalArgumentException("Can't link a node after Tree's root!");

        this.parent = previousNode.parent;
        this.firstChild = null;

        this.previous = previousNode;
        previousNode.next = this;

        this.next = null;
    }

    protected void linkEmpty() {
        parent = null;
        firstChild = null;
        previous = null;
        next = null;
    }
}
