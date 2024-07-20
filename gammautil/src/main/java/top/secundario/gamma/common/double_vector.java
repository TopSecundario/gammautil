package top.secundario.gamma.common;

import java.util.function.DoubleConsumer;

public class double_vector {

    public static double_vector of(double...varargs) {
        double_vector dv = new double_vector(varargs.length);
        dv.add(varargs, 0, varargs.length);
        return dv;
    }


    private static final int INITIAL_CAPACITY = 128;

    private double[] storage;
    private int offset;
    private int _size;


    public double_vector() {
        this(INITIAL_CAPACITY);
    }

    public double_vector(int initialCapacity) {
        if (initialCapacity < 0) {
            throw  new NegativeArraySizeException("initialCapacity is " + initialCapacity);
        }
        if (0 == initialCapacity) {
            initialCapacity = INITIAL_CAPACITY;
        }

        storage = new double[initialCapacity];
        offset = 0;
        _size = 0;
    }

    public int size() {
        return _size;
    }

    public boolean isEmpty() {
        return 0 == _size;
    }

    public void addLast(double d) {
        ensureCapacityAtTail(1);

        storage[offset + _size] = d;
        ++_size;
    }

    public void addFirst(double d) {
        ensureCapacityAtHead(1);

        --offset;
        storage[offset] = d;
        ++_size;
    }

    public void add(double d) {
        addLast(d);
    }

    public void add(int index, double d) {
        insert(index, d);
    }

    public void add(double[] a, int ofs, int s) {
        ensureCapacityAtTail(s);
        System.arraycopy(a, ofs, storage, _size + offset, s);
        _size = _size + s;
    }

    public double get(int index) {
        if ((index < 0) || (index >= _size)) {
            throw  new IndexOutOfBoundsException(String.format("Index out of bounds[0, %d): %d", _size, index));
        }

        return storage[offset + index];
    }

    public double fast_get(int index) {
        return storage[offset + index];
    }

    public double getFirst() {
        return get(0);
    }

    public double getLast() {
        return get(_size - 1);
    }

    public boolean set(int index, double d) {
        if ((index < 0) || (index >= _size)) {
            return false;
        }

        storage[offset + index] = d;
        return true;
    }

    public boolean setFirst(double d) {
        return set(0, d);
    }

    public boolean setLast(double d) {
        return set(_size - 1, d);
    }

    /**
     * Remove range.
     * @param startIndex  Start index, inclusive
     * @param limitIndex  Limit index, exclusive
     */
    public void remove(int startIndex, int limitIndex) {
        if (startIndex >= limitIndex) {
            throw  new IllegalArgumentException(String.format("startIndex(%d) >= limitIndex(%d)", startIndex, limitIndex));
        }
        if ((startIndex < 0) || (startIndex >= _size)) {
            throw  new IndexOutOfBoundsException(String.format("startIndex out of bounds[0, %d): %d", _size, startIndex));
        }
        if (limitIndex > _size) {
            throw  new IndexOutOfBoundsException(String.format("limitIndex out of bounds(0, %d]: %d", _size, limitIndex));
        }

        int distance = limitIndex - startIndex;
        moveForward(limitIndex + offset, _size + offset, distance);
        _size = _size - (limitIndex - startIndex);
    }

    public void remove(int index) {
        remove(index, index + 1);
    }

    public void removeFirst() {
        remove(0);
    }

    public void removeLast() {
        remove(_size - 1);
    }

    public void insert(int index, double d) {
        if ((index < 0) || (index > _size)) {
            throw  new IndexOutOfBoundsException(String.format("index out of bounds[0, %d]: %d", _size, index));
        }

        if (index < _size) {
            ensureCapacityAtTail(1);
            moveBackward(index + offset, _size + offset, 1);
            storage[index + offset] = d;
            ++_size;
        } else {
            addLast(d);
        }
    }

    public void foreach(DoubleConsumer consumer) {
        int lim = _size + offset;
        for (int loc = offset ; loc < lim ; ++loc) {
            consumer.accept(storage[loc]);
        }
    }

    public double[] toArray() {
        double[] a = new double[_size];
        System.arraycopy(storage, offset, a, 0, _size);
        return a;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int lim = _size + offset;
        for (int loc = offset ; loc < lim ; ++loc) {
            sb.append(storage[loc]);
            if (loc < lim - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private double[] extendStorage(int inc) throws ArrayStorageExtendException {
        int nc = storage.length + Integer.max(storage.length, inc);                      // new capacity
        if (nc < 0) {    // overflow
            nc = Integer.MAX_VALUE - 8;
        }
        if (nc - storage.length < inc) {    // extend fail
            throw  new ArrayStorageExtendException(storage.length, nc, inc);
        }

        double[] ns = new double[nc];                      // new storage
        return ns;
    }

    private void extendStorageAtTail(int inc) throws ArrayStorageExtendException {
        double[] ns = extendStorage(inc);
        System.arraycopy(storage, offset, ns, offset, _size);
        storage = ns;
    }

    private void extendStorageAtHead(int inc) throws ArrayStorageExtendException {
        double[] ns = extendStorage(inc);
        int no = offset + (ns.length - storage.length);    // new offset
        System.arraycopy(storage, offset, ns, no, _size);
        storage = ns;
        offset = no;
    }

    private int tailCapacity() {
        return storage.length - (offset + _size);
    }

    private int headCapacity() {
        return offset;
    }

    private void ensureCapacityAtTail(int inc) {
        if (tailCapacity() < inc) {
            extendStorageAtTail(inc);
        }
    }

    private void ensureCapacityAtHead(int inc) {
        if (headCapacity() < inc) {
            extendStorageAtHead(inc);
        }
    }

    /**
     * Move range backward(to tail direction).
     * @param startLoc  Start location in storage, inclusive
     * @param limitLoc  End location in storage, exclusive
     * @param distance  Distance to move, positive
     */
    private void moveBackward(int startLoc, int limitLoc, int distance) {
        for (int loc = limitLoc - 1 ; loc >= startLoc ; --loc) {
            storage[loc + distance] = storage[loc];
        }
    }

    /**
     * Move range forward(to head direction).
     * @param startLoc  Start location in storage, inclusive
     * @param limitLoc  End location in storage, exclusive
     * @param distance  Distance to move, positive
     */
    private void moveForward(int startLoc, int limitLoc, int distance) {
        for (int loc = startLoc ; loc < limitLoc ; ++loc) {
            storage[loc - distance] = storage[loc];
        }
    }
}
