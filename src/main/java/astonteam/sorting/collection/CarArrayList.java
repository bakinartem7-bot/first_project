package astonteam.sorting.collection;

import astonteam.sorting.model.Car;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Собственная коллекция автомобилей на динамическом массиве.
 * Не наследуется от стандартных коллекций: {@code add}, {@code get},
 * {@code set}, {@code remove}, итератор и рост ёмкости реализованы вручную.
 */
public class CarArrayList implements Iterable<Car> {

    // начальная ёмкость внутреннего массива
    private static final int INITIAL_CAPACITY = 10;

    private Car[] array = new Car[INITIAL_CAPACITY];
    private int size = 0;

    /**
     * Возвращает автомобиль по указанному индексу.
     *
     * @param index индекс элемента, от 0 до size() - 1
     * @return автомобиль по указанному индексу
     * @throws IndexOutOfBoundsException если index меньше 0 или не меньше size()
     */
    public Car get(int index) {
        checkIndex(index);
        return array[index];
    }

    /**
     * Добавляет автомобиль в конец коллекции.
     * При необходимости увеличивает ёмкость внутреннего массива.
     * После успешного добавления размер увеличивается на 1.
     *
     * @param element добавляемый автомобиль
     * @return true при успешном добавлении
     * @throws NullPointerException если element равен null
     */
    public boolean add(Car element) {
        Objects.requireNonNull(element, "element");
        ensureCapacity(size + 1);
        array[size++] = element;
        return true;
    }

    /**
     * Заменяет автомобиль по указанному индексу.
     * Размер коллекции не изменяется.
     *
     * @param index   индекс заменяемого элемента, от 0 до size() - 1
     * @param element новый автомобиль
     * @return автомобиль, находившийся по этому индексу до замены
     * @throws IndexOutOfBoundsException если index меньше 0 или не меньше size()
     * @throws NullPointerException      если element равен null
     */
    public Car set(int index, Car element) {
        checkIndex(index);
        Objects.requireNonNull(element, "element");
        Car previous = array[index];
        array[index] = element;
        return previous;
    }

    /**
     * Удаляет автомобиль по указанному индексу и сдвигает последующие элементы влево.
     * После удаления размер уменьшается на 1.
     *
     * @param index индекс удаляемого элемента, от 0 до size() - 1
     * @return удалённый автомобиль
     * @throws IndexOutOfBoundsException если index меньше 0 или не меньше size()
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
     * Возвращает количество элементов в коллекции.
     *
     * @return количество элементов; для пустой коллекции — 0
     */
    public int size() {
        return size;
    }

    /**
     * Возвращает новый итератор для обхода коллекции
     * в порядке индексов от 0 до size() - 1.
     * Неиспользуемые ячейки внутреннего массива не обходятся.
     * <p>
     * Метод hasNext() возвращает false после последнего элемента.
     * Вызов next() после окончания обхода выбрасывает
     * {@link NoSuchElementException}.
     * Метод remove() выбрасывает {@link UnsupportedOperationException}.
     * <p>
     * Изменение коллекции во время обхода не поддерживается:
     * вызывающий код не должен вызывать add() или set()
     * до завершения обхода. Обнаружение таких изменений
     * итератором не гарантируется.
     *
     * @return новый итератор по автомобилям
     */
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

    /**
     * Обеспечивает ёмкость внутреннего массива не меньше minCapacity.
     * Если текущей ёмкости достаточно, ничего не изменяет.
     * Иначе создаёт больший массив и переносит в него элементы,
     * сохраняя их порядок.
     * Размер коллекции size не изменяется.
     *
     * @param minCapacity минимальная требуемая ёмкость
     * @throws IllegalArgumentException если minCapacity меньше 0
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

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс " + index + " вне диапазона 0.." + (size - 1));
        }
    }
}
