package top.secundario.gamma.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertArrayEquals;

public class Test__double_vector {

    @Test
    public void test_size_and_isEmpty() {
        double_vector dv0 = new double_vector();
        assertTrue(dv0.isEmpty());
        assertEquals(0, dv0.size());

        double_vector dv1 = double_vector.of(1.0);
        assertFalse(dv1.isEmpty());
        assertEquals(1, dv1.size());

        double_vector dv6 = double_vector.of(0.2, 0.4, 0.6, 1.0, 1.1, 1.3);
        assertFalse(dv6.isEmpty());
        assertEquals(6, dv6.size());
    }

    @Test
    public void test_addLast() {
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};

        double_vector dv = new double_vector(4);
        for (double d : expected) {
            dv.addLast(d);
        }

        assertTrue(compareDoubleArray(expected, dv.toArray()));
    }

    @Test
    public void test_addFirst() {
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};

        double_vector dv = new double_vector(3);
        for (int i = expected.length - 1 ; i >= 0 ; --i) {
            dv.addFirst(expected[i]);
        }

        assertTrue(compareDoubleArray(expected, dv.toArray()));
    }

    @Test
    public void test_insert() {
        double_vector dv = new double_vector(4);
        dv.add(2.0);
        dv.insert(0, 1.0);
        dv.insert(2, 4.0);
        dv.insert(2, 3.0);
        dv.add(new double[]{5.0, 6.0, 7.0, 8.0}, 0, 4);

        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
        assertTrue(compareDoubleArray(expected, dv.toArray()));
    }

    @Test
    public void test_add__index() {
        double_vector dv = new double_vector(4);
        dv.add(new double[]{2.0}, 0, 1);
        dv.add(0, 1.0);
        dv.add(2, 4.0);
        dv.add(2, 3.0);
        dv.add(new double[]{5.0, 6.0, 7.0, 8.0}, 0, 4);

        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
        assertTrue(compareDoubleArray(expected, dv.toArray()));
    }

    @Test
    public void test_get() {
        double_vector dv = new double_vector(3);
        dv.add(2.0);
        dv.add(3.0);
        dv.add(4.0);
        dv.addFirst(1.0);

        assertEquals(Double.doubleToLongBits(1.0), Double.doubleToLongBits(dv.getFirst()));
        assertEquals(Double.doubleToLongBits(2.0), Double.doubleToLongBits(dv.get(1)));
        assertEquals(Double.doubleToLongBits(3.0), Double.doubleToLongBits(dv.get(2)));
        assertEquals(Double.doubleToLongBits(4.0), Double.doubleToLongBits(dv.getLast()));
    }

    @Test
    public void test__fast_get() {
        double_vector dv = double_vector.of(1.0, 2.0, 3.0, 4.0);

        assertEquals(Double.doubleToLongBits(1.0), Double.doubleToLongBits(dv.fast_get(0)));
        assertEquals(Double.doubleToLongBits(2.0), Double.doubleToLongBits(dv.fast_get(1)));
        assertEquals(Double.doubleToLongBits(3.0), Double.doubleToLongBits(dv.fast_get(2)));
        assertEquals(Double.doubleToLongBits(4.0), Double.doubleToLongBits(dv.fast_get(3)));
    }

    @Test
    public void test_set() {
        double_vector dv = double_vector.of(0.0, 0.0, 0.0, 0.0);

        dv.setFirst(1.0);
        dv.set(1, 2.0);
        dv.set(2, 3.0);
        dv.setLast(4.0);

        assertTrue(compareDoubleArray(new double[]{1.0, 2.0, 3.0, 4.0}, dv.toArray()));
    }

    @Test
    public void test_remove() {
        double_vector dv = double_vector.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);

        dv.removeFirst();   // 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0
        dv.removeLast();    // 2.0, 3.0, 4.0, 5.0, 6.0, 7.0
        dv.remove(2, 4);     // 2.0, 3.0, 6.0, 7.0
        dv.remove(1);   // 2.0, 6.0, 7.0

        List<Double> l = new ArrayList<>();
        dv.foreach(l::add);
        double[] actual = new double[l.size()];
        for (int i = 0 ; i < actual.length ; ++i) {
            actual[i] = l.get(i);
        }

        assertTrue(compareDoubleArray(new double[]{2.0, 6.0, 7.0}, actual));
    }

    private static boolean compareDoubleArray(double[] a1, double[] a2) {
        if (a1.length != a2.length) {
            return false;
        }

        for (int i = 0 ; i < a1.length ; ++i) {
            if (Double.doubleToLongBits(a1[i]) != Double.doubleToLongBits(a2[i])) {
                return false;
            }
        }
        return true;
    }
}
