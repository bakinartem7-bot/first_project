package astonteam.sorting.collection;

import astonteam.sorting.model.Car;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Objects;

public final class CarList extends AbstractList<Car> {

    private static final int DEFAULT_CAPACITY = 10;

    private Car[] data;
    private int size;

    public CarList() {
        this(DEFAULT_CAPACITY);
    }

    public CarList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Начальная ёмкость не может быть отрицательной: " + initialCapacity);
        }
        this.data = new Car[initialCapacity];
    }

    @Override
    public Car get(int index) {
        Objects.checkIndex(index, size);
        return data[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(Car car) {
        ensureCapacity(size + 1);
        data[size++] = car;
        modCount++;
        return true;
    }

    @Override
    public void add(int index, Car car) {
        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = car;
        size++;
        modCount++;
    }

    @Override
    public Car set(int index, Car car) {
        Objects.checkIndex(index, size);
        Car old = data[index];
        data[index] = car;
        return old;
    }

    @Override
    public Car remove(int index) {
        Objects.checkIndex(index, size);
        Car old = data[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(data, index + 1, data, index, moved);
        }
        data[--size] = null;
        modCount++;
        return old;
    }

    @Override
    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
        modCount++;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int grown = data.length + (data.length >> 1);
            data = Arrays.copyOf(data, Math.max(minCapacity, grown));
        }
    }
}
