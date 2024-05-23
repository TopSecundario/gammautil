package top.secundario.gamma.common;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.function.Function;

public class TreeS {
    public static String TREE_HIERARCHY_TAG = "|---";

    private static void dumpTreeNodeHierarchyLine(final int depth, PrintStream out) {
        for (int i = 0 ; i < depth - 1 ; ++i) {
            out.print(TREE_HIERARCHY_TAG);
        }
    }


    public static <T> void dump(TreeNode<T> rootNode) {
        dump(rootNode, System.out);
    }

    public static <T> void dump(TreeNode<T> rootNode, PrintStream out) {
        ObjectS.reqNotNull(rootNode);
        ObjectS.reqNotNull(out);

        TreeVisitor<T, Object> dumpVisitor = (_data, _tvCtx) -> {
            dumpTreeNodeHierarchyLine(_tvCtx.getDepth(), out);
            out.println(_data);       // platform-dependent line break
            return TreeVisitIndicator.CONTINUE;
        };

        rootNode.walk(dumpVisitor);
    }

    public static <T> TreeNode<T> load(BufferedReader br, Function<String, T> transformer) {
        T data = transformer.apply("");

        return new TreeNode<>(data);
    }

    protected TreeS() {}
}
