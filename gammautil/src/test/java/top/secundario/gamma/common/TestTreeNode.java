package top.secundario.gamma.common;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.Assert.*;

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


    record NodeInfo<T>(TreeNode<T> node, int step, int depth) {
        public String toString() {
            return String.format("('%s', %d, %d)", node.getData(), step, depth);
        }
    }

    private List<NodeInfo<String>> createWorldRegionDivision() throws IOException {
        List<NodeInfo<String>> nodeInfoList = new ArrayList<>();

        Path path = Path.of("src/test/resources/world.txt");
        try (var lines = Files.lines(path)) {
            int step = 0;
            int last_h = -1;
            TreeNode<String> lastNode = null;

            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();

                int h = Strings.substringCount(line, "|---");
                if (0 == h) {
                    /* root */
                    TreeNode<String> rootNode = new TreeNode<>(line);
                    ++step;
                    nodeInfoList.add(new NodeInfo<>(rootNode, step, h + 1));
                    last_h = h;
                    lastNode = rootNode;
                } else {
                    /* NOT root */
                    if (h == last_h) {
                        /* sibling of last node */
                        TreeNode<String> currentNode = lastNode.addNextData(line);
                        ++step;
                        nodeInfoList.add(new NodeInfo<>(currentNode, step, h + 1));
                        lastNode = currentNode;
                    } else if (h - last_h == 1) {
                        /* new branch */
                        TreeNode<String> currentNode = lastNode.addFirstChildData(line);
                        ++step;
                        nodeInfoList.add(new NodeInfo<>(currentNode, step, h + 1));
                        last_h = h;
                        lastNode = currentNode;
                    } else if (h < last_h) {
                        /* ascend against last node */
                        TreeNode<String> parentNode = lastNode.ascend(last_h - h + 1);
                        TreeNode<String> currentNode = parentNode.addChildData(line);
                        ++step;
                        nodeInfoList.add(new NodeInfo<>(currentNode, step, h + 1));
                        last_h = h;
                        lastNode = currentNode;
                    } else {
                        System.err.println("Invalid line for a tree branch: " + line);
                    }
                }
            }
        }

        return nodeInfoList;
    }

    @Test
    public void test_walk_base() throws IOException {
        final List<NodeInfo<String>> nodeInfoList = createWorldRegionDivision();
        final Ref<Integer> sn = new Ref<>(0);
        TreeNode<String> root = nodeInfoList.get(0).node;

        TreeVisitor<String, Object> visitor = (_data, _tvCtx) -> {
            int _sn = sn.get();
            NodeInfo<String> expectedNodeInfo = nodeInfoList.get(_sn);
            ++_sn;
            sn.set(_sn);

            assertSame(expectedNodeInfo.node, _tvCtx.getNode());
            assertEquals(expectedNodeInfo.node.getData(), _data);
            assertEquals(expectedNodeInfo.step, _tvCtx.getStep());
            assertEquals(expectedNodeInfo.depth, _tvCtx.getDepth());

            return TreeVisitIndicator.CONTINUE;
        };

        root.walk(visitor, null);
    }

    @Test
    public void test_walk_dataPassing() {
        /*
        米家
        |---景茂·雍水岸
        |---|---虚拟房
        |---|---|---全开模拟窗
        |---|---|---中枢网关
        |---|---|---灯泡
        |---|---主卧
        |---|---|---温湿度计
        |---|---|---电小酷智能插座
        |---|---未分配房间
        |---|---|---空气净化器
        |---|---|---无线路由器
        |---马鞍村老家
        |---|---魏雅文卧室
        |---|---|---全效空气净化器
         */
        TreeNode<String> root = new TreeNode<>("米家");

        TreeNode<String> jm = root.addFirstChildData("景茂·雍水岸");

        TreeNode<String> xnf = jm.addChildData("虚拟房");

        TreeNode<String> qkmnc = xnf.addFirstChildData("全开模拟窗");
        qkmnc.addNextData("中枢网关").addNextData("灯泡");

        TreeNode<String> zw = jm.addChildData("主卧");
        zw.addChildData("温湿度计");
        zw.addChildData("电小酷智能插座");

        TreeNode<String> wfpfj = zw.addNextData("未分配房间");

        wfpfj.addFirstChildData("空气净化器");
        wfpfj.addChildData("无线路由器");

        TreeNode<String> mac = root.addChildData("马鞍村老家");
        mac.addChildData("魏雅文卧室").addChildData("全效空气净化器");


        final Map<String, Object> expectedDataPassingMap = new HashMap<>();

        TreeVisitContext<String, Object> tvCtx = new TreeVisitContext<>();

        Object dataPassToRoot = new Object();
        tvCtx.setDataFromParent(dataPassToRoot);
        expectedDataPassingMap.put("_ROOT_", dataPassToRoot);

        TreeVisitor<String, Object> visitor = (_data, _tvCtx) -> {
            TreeNode<String> _node = _tvCtx.getNode();
            if (_node.isRoot()) {
                assertSame(expectedDataPassingMap.get("_ROOT_"), _tvCtx.getDataFromParent());
                Object toChildren = new Object();
                _tvCtx.setDataToChildren(toChildren);
                expectedDataPassingMap.put(_data, toChildren);
            } else if (_node.isLeaf()) {
                String parent = _node.getParentData();
                assertSame(expectedDataPassingMap.get(parent), _tvCtx.getDataFromParent());
            } else {
                String parent = _node.getParentData();
                assertSame(expectedDataPassingMap.get(parent), _tvCtx.getDataFromParent());
                Object toChildren = new Object();
                _tvCtx.setDataToChildren(toChildren);
                expectedDataPassingMap.put(_data, toChildren);
            }
            return TreeVisitIndicator.CONTINUE;
        };

        root.walk(visitor, tvCtx);
    }

    @Test
    public void test_walk_postVisit() throws IOException {
        TreeNode<String> rootNode = createSolarSystem();
        StringBuilder sb = new StringBuilder();

        TreeVisitContext<String, StringBuilder> tvCtx = new TreeVisitContext<>();
        tvCtx.setDataFromParent(sb);

        TreeVisitor<String, StringBuilder> visitor = (_data, _tvCtx) -> {
            TreeNode<String> _node = _tvCtx.getNode();
            StringBuilder _sb = _tvCtx.getDataFromParent();
            if (! _node.isLeaf()) {
                _sb.append("{\"" + _data + "\" : [");
            } else {  /* leaf node */
                _sb.append('"').append(_data).append('"');
                if (ObjectS.isNotNull(_node.getNext())) {
                    _sb.append(", ");
                }
            }
            _tvCtx.setDataToChildren(_sb);
            return TreeVisitIndicator.CONTINUE;
        };

        TreePostVisitor<String, StringBuilder> postVisitor = (_data, _tpvCtx) -> {
            TreeNode<String> _node = _tpvCtx.getNode();
            StringBuilder _sb = _tpvCtx.getDataFromParent();
            if (! _node.isLeaf()) {
                _sb.append("]}");
                if (ObjectS.isNotNull(_node.getNext())) {
                    _sb.append(", ");
                }
            }
            return TreeVisitIndicator.AS_PRE_VISIT;
        };

        rootNode.walk(visitor, tvCtx, postVisitor);

        Path path = Path.of("src/test/resources/solar_system.json");
        String expectedJSON = Files.readString(path);
        assertEquals(expectedJSON, sb.toString());
    }
}
