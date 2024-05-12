package top.secundario.gamma.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class TestTreeNode {
    private TreeNode<String> createSolarSystem() {
        /*
        太阳系
        |--太阳
        |--行星
        |--|--水星
        |--|--金星
        |--|--地月系
        |--|--|--地球
        |--|--|--月球
        |--|--火星系
        |--|--|--火星
        |--|--|--福波斯
        |--|--|--德摩斯
        |--矮行星
        |--|--冥王星
         */

        TreeNode<String> solarSystemRoot = new TreeNode<>("太阳系");

        TreeNode<String> sun = new TreeNode<>("太阳", solarSystemRoot);
        TreeNode<String> planets = new TreeNode<>("行星", solarSystemRoot);
        TreeNode<String> dwarfPlanets = new TreeNode<>("矮行星", solarSystemRoot);

        TreeNode<String> mercury = new TreeNode<>("水星", planets);
        TreeNode<String> venus = new TreeNode<>("金星", planets);
        TreeNode<String> earthSystem = new TreeNode<>("地月系", planets);
        TreeNode<String> marsSystem = new TreeNode<>("火星系", planets);

        TreeNode<String> earth = new TreeNode<>("地球", earthSystem);
        TreeNode<String> luna = new TreeNode<>("月球", earthSystem);

        TreeNode<String> mars = new TreeNode<>("火星", marsSystem);
        TreeNode<String> phobos = new TreeNode<>("福波斯", marsSystem);
        TreeNode<String> deimos = new TreeNode<>("德摩斯", marsSystem);

        TreeNode<String> pluto = new TreeNode<>("冥王星", dwarfPlanets);

        return solarSystemRoot;
    }

    @Test
    public void test_simpleWalk() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球",
                "火星系", "火星", "福波斯", "德摩斯", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        rootNode.simpleWalk(walkedSeq::add);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_simpleWalk_0() {
        TreeNode<String> rootNode = new TreeNode<>("root");
        String[] expectedSeq = new String[]{"root"};

        final List<String> walkedSeq = new ArrayList<>();
        rootNode.simpleWalk(walkedSeq::add);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_Continuous() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球",
                "火星系", "火星", "福波斯", "德摩斯", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvContinuous = (data) -> {
            walkedSeq.add(data);
            return TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvContinuous);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_TerminateLeaf() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvTerminate = (data) -> {
            walkedSeq.add(data);
            return  ("月球".equals(data)) ? TreeVisitIndicator.TERMINATE : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvTerminate);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_TerminateRoot() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvTerminate = (data) -> {
            walkedSeq.add(data);
            return  ("太阳系".equals(data)) ? TreeVisitIndicator.TERMINATE : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvTerminate);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_TerminateMid() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球",
                "火星系"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvTerminate = (data) -> {
            walkedSeq.add(data);
            return  ("火星系".equals(data)) ? TreeVisitIndicator.TERMINATE : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvTerminate);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_SkipSubTree_Mid1() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球",
                "火星系", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvSkipSubTree = (data) -> {
            walkedSeq.add(data);
            return  ("火星系".equals(data)) ? TreeVisitIndicator.SKIP_SUB_TREE : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvSkipSubTree);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_SkipSubTree_Mid2() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvSkipSubTree = (data) -> {
            walkedSeq.add(data);
            return  ("行星".equals(data)) ? TreeVisitIndicator.SKIP_SUB_TREE : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvSkipSubTree);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_SkipSubTree_root() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvSkipSubTree = (data) -> {
            walkedSeq.add(data);
            return  ("太阳系".equals(data)) ? TreeVisitIndicator.SKIP_SUB_TREE : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvSkipSubTree);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_SkipSibling_FirstLeaf() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球",
                "火星系", "火星", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvSkipSibling = (data) -> {
            walkedSeq.add(data);
            return  ("火星".equals(data)) ? TreeVisitIndicator.SKIP_SIBLING : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvSkipSibling);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_SkipSibling_MidLeaf() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "地月系", "地球", "月球",
                "火星系", "火星", "福波斯", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvSkipSibling = (data) -> {
            walkedSeq.add(data);
            return  ("福波斯".equals(data)) ? TreeVisitIndicator.SKIP_SIBLING : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvSkipSibling);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }

    @Test
    public void test_controllableWalk_SkipSibling_MidLeaf_Mid1() {
        TreeNode<String> rootNode = createSolarSystem();
        String[] expectedSeq = new String[]{"太阳系", "太阳", "行星", "水星", "金星", "矮行星", "冥王星"};

        final List<String> walkedSeq = new ArrayList<>();
        TreeControllableVisitor<String> tcvSkipSibling = (data) -> {
            walkedSeq.add(data);
            return  ("金星".equals(data)) ? TreeVisitIndicator.SKIP_SIBLING : TreeVisitIndicator.CONTINUE;
        };
        rootNode.controllableWalk(tcvSkipSibling);

        assertArrayEquals(expectedSeq, walkedSeq.toArray());
    }
}
