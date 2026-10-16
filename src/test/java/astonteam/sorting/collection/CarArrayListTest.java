package astonteam.sorting.collection;

import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты собственной реализации списка {@link CarArrayList}:
 * проверяют добавление, доступ, замену, удаление, итерацию и рост ёмкости.
 */
class CarArrayListTest {

    /**
     * Создаёт тестовый автомобиль с заданной мощностью и производными моделью и годом.
     *
     * @param power мощность автомобиля
     * @return построенный объект {@link Car}
     */
    private static Car car(int power) {
        return Car.builder().power(power).model("Model" + power).year(2000).build();
    }

    /**
     * Проверяет, что новый список пуст: размер равен нулю и итератор не содержит элементов.
     */
    @Test
    void newListIsEmpty() {
        CarArrayList cars = new CarArrayList();
        assertEquals(0, cars.size());
        assertFalse(cars.iterator().hasNext());
    }

    /**
     * Проверяет, что add возвращает true и элементы сохраняются в порядке добавления.
     */
    @Test
    void addReturnsTrueAndKeepsOrder() {
        CarArrayList cars = new CarArrayList();
        Car first = car(100);
        Car second = car(200);
        Car third = car(300);
        assertTrue(cars.add(first));
        assertTrue(cars.add(second));
        assertTrue(cars.add(third));
        assertEquals(3, cars.size());
        assertSame(first, cars.get(0));
        assertSame(second, cars.get(1));
        assertSame(third, cars.get(2));
    }

    /**
     * Проверяет, что добавление null выбрасывает NullPointerException.
     */
    @Test
    void addRejectsNull() {
        CarArrayList cars = new CarArrayList();
        assertThrows(NullPointerException.class, () -> cars.add(null));
    }

    /**
     * Проверяет, что get с индексом вне допустимого диапазона выбрасывает IndexOutOfBoundsException.
     */
    @Test
    void getRejectsIndexOutsideBounds() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(100));
        assertThrows(IndexOutOfBoundsException.class, () -> cars.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> cars.get(1));
    }

    /**
     * Проверяет, что set заменяет элемент по индексу и возвращает предыдущее значение.
     */
    @Test
    void setReplacesElementAndReturnsPrevious() {
        CarArrayList cars = new CarArrayList();
        Car old = car(100);
        Car replacement = car(150);
        cars.add(old);
        Car previous = cars.set(0, replacement);
        assertSame(old, previous);
        assertSame(replacement, cars.get(0));
        assertEquals(1, cars.size());
    }

    /**
     * Проверяет, что set отклоняет некорректный индекс и null-значение.
     */
    @Test
    void setRejectsInvalidArguments() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(100));
        assertThrows(IndexOutOfBoundsException.class, () -> cars.set(1, car(150)));
        assertThrows(NullPointerException.class, () -> cars.set(0, null));
    }

    /**
     * Проверяет, что remove возвращает удалённый элемент и сдвигает оставшиеся влево.
     */
    @Test
    void removeReturnsRemovedAndShiftsRemaining() {
        CarArrayList cars = new CarArrayList();
        Car first = car(100);
        Car second = car(200);
        Car third = car(300);
        cars.add(first);
        cars.add(second);
        cars.add(third);
        assertSame(first, cars.remove(0));
        assertEquals(2, cars.size());
        assertSame(second, cars.get(0));
        assertSame(third, cars.get(1));
    }

    /**
     * Проверяет, что remove с индексом вне диапазона выбрасывает IndexOutOfBoundsException.
     */
    @Test
    void removeRejectsIndexOutsideBounds() {
        CarArrayList cars = new CarArrayList();
        assertThrows(IndexOutOfBoundsException.class, () -> cars.remove(0));
        cars.add(car(100));
        assertThrows(IndexOutOfBoundsException.class, () -> cars.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> cars.remove(-1));
    }

    /**
     * Проверяет, что итератор обходит элементы по порядку и по завершении
     * next выбрасывает NoSuchElementException.
     */
    @Test
    void iteratorVisitsElementsInOrder() {
        CarArrayList cars = new CarArrayList();
        Car first = car(100);
        Car second = car(200);
        cars.add(first);
        cars.add(second);
        Iterator<Car> iterator = cars.iterator();
        assertTrue(iterator.hasNext());
        assertSame(first, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(second, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    /**
     * Проверяет, что метод remove у итератора не поддерживается.
     */
    @Test
    void iteratorRemoveIsUnsupported() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(100));
        Iterator<Car> iterator = cars.iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    /**
     * Проверяет, что список корректно растёт при превышении начальной ёмкости,
     * сохраняя порядок и размер.
     */
    @Test
    void growsBeyondInitialCapacity() {
        CarArrayList cars = new CarArrayList();
        int count = 25;
        for (int i = 1; i <= count; i++) {
            cars.add(car(i));
        }
        assertEquals(count, cars.size());
        assertEquals(count, cars.get(count - 1).getPower());
        assertEquals(1, cars.get(0).getPower());
    }

    /**
     * Проверяет, что удаление последнего элемента уменьшает размер списка
     * и сохраняет оставшийся элемент.
     */
    @Test
    void removeLastElementShrinksList() {
        CarArrayList cars = new CarArrayList();
        Car keeper = car(100);
        Car last = car(200);
        cars.add(keeper);
        cars.add(last);
        assertSame(last, cars.remove(1));
        assertEquals(1, cars.size());
        assertSame(keeper, cars.get(0));
    }
}
