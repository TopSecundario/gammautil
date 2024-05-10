package top.secundario.gamma.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestObjectS {

    @Test
    public void test_isNull() {
        Object obj0 = null;

        assertTrue(ObjectS.isNull(obj0));

        assertTrue(ObjectS.isNotNull(new Object()));
    }

    @Test
    public void test_classOf() {
        assertTrue(ObjectS.classOf(Integer.valueOf(1), Integer.class));
        assertFalse(ObjectS.classOf(Integer.valueOf(2), Number.class));
    }

    @Test
    public void test_cast() {
        Number nn = Integer.valueOf(3);
        Integer ii = ObjectS.cast(nn, Integer.class);
        assertSame(nn, ii);
    }


    private abstract class Animal {

    }

    private class Human extends Animal {

    }

    @Test
    public void test_classOf_2() {
        Animal animal = new Human();
        Ref<Human> rh = new Ref<>();
        boolean flag = ObjectS.classOf(animal, Human.class, rh);
        assertTrue(flag);
        assertSame(animal, rh.get());

        String s = "cat";
        rh.set(null);
        flag = ObjectS.classOf(s, Human.class, rh);
        assertFalse(flag);
        assertNull(rh.get());
    }
}
