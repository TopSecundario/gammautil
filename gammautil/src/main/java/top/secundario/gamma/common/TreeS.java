package top.secundario.gamma.common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Function;

public class TreeS {
    public static String TREE_HIERARCHY_TAG = "|---";

    private static void dumpTreeNodeHierarchyLine(final int depth, PrintStream out) {
        for (int i = 0 ; i < depth - 1 ; ++i) {
            out.print(TREE_HIERARCHY_TAG);
        }
    }

    private static String trimTreeNodeHierarchyLine(String line, final int depth) {
        return  line.substring(depth * TREE_HIERARCHY_TAG.length());
    }

    /* dump tree to file */

    public static <T> void dump(TreeNode<T> rootNode) {
        dump(rootNode, System.out);
    }

    public static <T> void dump(TreeNode<T> rootNode, String treeDumpFileS) throws IOException {
        try (var out = new PrintStream(treeDumpFileS, StandardCharsets.UTF_8)) {
            dump(rootNode, out);
        }
    }

    public static <T> void dump(TreeNode<T> rootNode, File treeDumpFile) throws IOException {
        dump(rootNode, treeDumpFile.toString());
    }

    public static <T> void dump(TreeNode<T> rootNode, Path treeDumpFileP) throws IOException {
        dump(rootNode, treeDumpFileP.toString());
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

    /* load tree from file */

    public static TreeNode<String> load(String treeDumpFileS) throws IOException {
        return load(treeDumpFileS, (_s) -> {return _s;});
    }

    public static TreeNode<String> load(File treeDumpFile) throws IOException {
        return load(treeDumpFile.toString());
    }

    public static TreeNode<String> load(Path treeDumpFileP) throws IOException {
        return load(treeDumpFileP.toString());
    }

    public static <T> TreeNode<T> load(String treeDumpFileS, Function<String, ? extends T> transformer) throws IOException {
        try (var fr = new FileReader(treeDumpFileS, StandardCharsets.UTF_8); var br = new BufferedReader(fr)) {
            return load(br, transformer);
        }
    }

    public static <T> TreeNode<T> load(File treeDumpFile, Function<String, ? extends T> transformer) throws IOException {
        return load(treeDumpFile.toString(), transformer);
    }

    public static <T> TreeNode<T> load(Path treeDumpFileP, Function<String, ? extends T> transformer) throws IOException {
        return load(treeDumpFileP.toString(), transformer);
    }

    public static <T> TreeNode<T> load(BufferedReader br, Function<String, ? extends T> transformer) throws IOException {
        TreeNode<T> rootNode = null;
        int last_h = -1;
        TreeNode<T> lastNode = null;

        String line = br.readLine();
        while (ObjectS.isNotNull(line)) {
            int h = Strings.substringCount(line, TREE_HIERARCHY_TAG);
            String content = trimTreeNodeHierarchyLine(line, h);
            T data = transformer.apply(content);
            if (0 == h) {
                /* root */
                rootNode = new TreeNode<>(data);
                last_h = h;
                lastNode = rootNode;
            } else {
                /* NOT root */
                if (h == last_h) {
                    /* sibling of last node */
                    lastNode = lastNode.addNextData(data);
                } else if (h - last_h == 1) {
                    /* new branch */
                    lastNode = lastNode.addFirstChildData(data);
                    last_h = h;
                } else if (h < last_h) {
                    /* ascend against last node */
                    TreeNode<T> parentNode = lastNode.ascend(last_h - h + 1);
                    lastNode = parentNode.addChildData(data);
                    last_h = h;
                } else {
                    System.err.println("Invalid line for a tree branch: " + line);
                }
            }

            line = br.readLine();     // read next line
        }

        return rootNode;
    }

    protected TreeS() {}
}
