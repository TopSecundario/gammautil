package top.secundario.gamma.common;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class TestTreeS {
    private TreeNode<String> createFamily() {
        TreeNode<String> rootNode = new TreeNode<>("魏建生");

        TreeNode<String> h2_1 = rootNode.addChildData("魏雪周");
        TreeNode<String> h2_2 = rootNode.addChildData("魏小林");
        TreeNode<String> h2_3 = rootNode.addChildData("魏小平");

        h2_1.addChildData("周淑君");
        h2_1.addChildData("周焰燃");

        TreeNode<String> h3 = h2_2.addChildData("魏立川");
        h2_3.addChildData("魏梦思").addNextData("魏立明");

        h3.addFirstChildData("魏雅文");

        return rootNode;
    }

    @Test
    public void test_dump() throws IOException {
        TreeNode<String> familyTree = createFamily();

        try (var baos = new ByteArrayOutputStream();
             var out = new PrintStream(baos, true, StandardCharsets.UTF_8))
        {
            TreeS.dump(familyTree, out);

            String actualResult = baos.toString(StandardCharsets.UTF_8);

            Path path = Path.of("src/test/resources/family.txt");
            String expectedResult = Files.readString(path);

            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void test_load() throws IOException {
        Path pathOrigin = Path.of("src/test/resources/world.txt");
        TreeNode<String> worldTree = TreeS.load(pathOrigin);

        Path pathNew = Files.createTempFile("world_new.txt", null);
        TreeS.dump(worldTree, pathNew);

        String newDump = Files.readString(pathNew);
        String expectedOrigin = Files.readString(pathOrigin);
        assertEquals(expectedOrigin, newDump);
    }
}
