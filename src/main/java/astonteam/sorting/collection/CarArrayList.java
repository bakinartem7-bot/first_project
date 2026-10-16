package astonteam.sorting.collection;

import astonteam.sorting.model.Car;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Динамический массив автомобилей с ручным управлением ёмкостью.
 */
public class CarArrayList implements Iterable<Car> {

    /** Начальная ёмкость массива. */
    private static final int INITIAL_CAPACITY = 10;

    /** Внутреннее хранилище элементов. */
    private Car[] array = new Car[INITIAL_CAPACITY];
    /** Текущее количество элементов. */
    private int size = 0;
    /**
     * Возвращает элемент по индексу.
     *
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    public Car get(int index) {
        checkIndex(index);
        return array[index];
    }
    /**
     * Добавляет элемент в конец коллекции.
     *
     * @param element добавляемый элемент
     * @return {@code true} после успешного добавления
     * @throws NullPointerException если элемент равен {@code null}
     */
    public boolean add(Car element) {
        Objects.requireNonNull(element, "element");
        ensureCapacity(size + 1);
        array[size++] = element;
        return true;
    }
    /**
     * Заменяет элемент по индексу.
     *
     * @param index   индекс заменяемого элемента
     * @param element новый элемент
     * @return ранее хранившийся элемент
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     * @throws NullPointerException      если элемент равен {@code null}
     */
    public Car set(int index, Car element) {
        checkIndex(index);
        Objects.requireNonNull(element, "element");
        Car previous = array[index];
        array[index] = element;
        return previous;
    }

    /**
     * Удаляет элемент по индексу.
     *
     * @param index индекс удаляемого элемента
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
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

    /**
     * Возвращает количество элементов.
     *
     * @return текущий размер коллекции
     */
    public int size() {
        return size;
    }

    /**
     * Возвращает итератор по элементам коллекции.
     *
     * @return итератор автомобилей
     */
    @Override
    public Iterator<Car> iterator() {
        return new Iterator<>() {

            /** Текущая позиция итератора. */
            private int cursor = 0;

            /**
             * Проверяет наличие следующего элемента.
             *
             * @return {@code true}, если есть следующий элемент
             */
            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            /**
             * Возвращает следующий элемент.
             *
             * @return следующий элемент
             * @throws NoSuchElementException если элементы закончились
             */
            @Override
            public Car next() {
                if (cursor >= size) {
                    throw new NoSuchElementException("Элементы коллекции закончились");
                }
                return array[cursor++];
            }

            /**
             * Удаление через итератор не поддерживается.
             *
             * @throws UnsupportedOperationException всегда
             */
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление через итератор не поддерживается");
            }
        };
    }

    /**
     * Увеличивает ёмкость массива при необходимости.
     *
     * @param minCapacity требуемая минимальная ёмкость
     * @throws IllegalArgumentException если ёмкость отрицательна
     */
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

    /**
     * Проверяет корректность индекса.
     *
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс " + index + " вне диапазона 0.." + (size - 1));
        }
    }
}
