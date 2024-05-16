package top.secundario.gamma.common;

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

    public TreeNode<T> getParent() {
        return parent;
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

    public void simpleWalk(TreeSimpleVisitor<? super T> visitor) {
        _simpleWalk(this, visitor);
    }

    public TreeVisitIndicator controllableWalk(TreeControllableVisitor<? super T> visitor) {
        TreeVisitor<T, Object> wrappedVisitor = (_data, _tvCtx) -> {return visitor.visit(_data);};

        TreeVisitContext<T, Object> tvCtx = new TreeVisitContext<>();
        tvCtx.incDepth();
        TreeVisitIndicator indicator = _walk(this, wrappedVisitor, tvCtx);
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

    private static <T, R> TreeVisitIndicator _walk(TreeNode<T> node, TreeVisitor<T, R> visitor, TreeVisitContext<T, R> tvCtx) {
        /* visit itself */
        tvCtx.setNode(node);
        tvCtx.incStep();
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
                TreeVisitIndicator ind = _walk(child, visitor, tvCtx);      /* visit a child */
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

        return indicator;
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

    protected void linkEmpty() {
        parent = null;
        firstChild = null;
        previous = null;
        next = null;
    }
}
