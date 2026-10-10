package astonteam.sorting.collection;

import astonteam.sorting.model.Car;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class CarArrayList implements Iterable<Car> {

    private static final int INITIAL_CAPACITY = 10;

    private Car[] array = new Car[INITIAL_CAPACITY];
    private int size = 0;
    public Car get(int index) {
        checkIndex(index);
        return array[index];
    }
    public boolean add(Car element) {
        Objects.requireNonNull(element, "element");
        ensureCapacity(size + 1);
        array[size++] = element;
        return true;
    }
    public Car set(int index, Car element) {
        checkIndex(index);
        Objects.requireNonNull(element, "element");
        Car previous = array[index];
        array[index] = element;
        return previous;
    }

    public Car remove(int index) {
        checkIndex(index);
        Car removed = array[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(array, index + 1, array, index, moved);
        }
        array[--size] = null;
        return removed;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Car> iterator() {
        return new Iterator<>() {

            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public Car next() {
                if (cursor >= size) {
                    throw new NoSuchElementException("Элементы коллекции закончились");
                }
                return array[cursor++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление через итератор не поддерживается");
            }
        };
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new IllegalArgumentException("Минимальная ёмкость не может быть отрицательной: " + minCapacity);
        }
        if (minCapacity <= array.length) {
            return;
        }
        int grown = array.length + (array.length >> 1);
        int newCapacity = Math.max(minCapacity, grown);
        Car[] bigger = new Car[newCapacity];
        System.arraycopy(array, 0, bigger, 0, size);
        array = bigger;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс " + index + " вне диапазона 0.." + (size - 1));
        }
    }
}
