package astonteam.sorting.search;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для {@link CarCounter}: проверка многопоточного подсчёта вхождений.
 */
class CarCounterTest {

    /**
     * Создаёт тестовый автомобиль с указанными параметрами.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year год выпуска автомобиля
     * @return построенный объект {@link Car}
     */
    private static Car car(int power, String model, int year) {
        return Car.builder().power(power).model(model).year(year).build();
    }

    /**
     * Проверяет, что для пустой коллекции подсчёт возвращает ноль.
     */
    @Test
    void shouldReturnZeroForEmptyCollection() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);
        assertEquals(0, counter.countOccurrences(cars, target));
    }

    /**
     * Проверяет, что при единственном совпадении подсчёт возвращает единицу.
     */
    @Test
    void shouldCountSingleMatch() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);
        cars.add(target);
        assertEquals(1, counter.countOccurrences(cars, target));
    }

    /**
     * Проверяет, что при отсутствии совпадений подсчёт возвращает ноль.
     */
    @Test
    void shouldReturnZeroWhenNoMatches() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);
        cars.add(car(100, "BMW", 2000));
        assertEquals(0, counter.countOccurrences(cars, target));
    }

    /**
     * Проверяет, что при нескольких совпадениях и неравномерном разбиении на потоки
     * подсчитывается точное число вхождений.
     */
    @Test
    void shouldCountMultipleMatchesAndHandleUnevenSplit() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);

        // Добавляем 10 элементов. Цель встречается 3 раза.
        // При разбиении на потоки (например, 4 или 8) массив разделится неравномерно,
        // тест проверит корректность границ диапазонов.
        cars.add(target);
        cars.add(car(100, "BMW", 2000));
        cars.add(target);
        cars.add(car(200, "Audi", 2010));
        cars.add(car(150, "Toyota", 2016)); // Похожая, но другая (год)
        cars.add(target);
        cars.add(car(100, "BMW", 2000));
        cars.add(car(100, "BMW", 2000));
        cars.add(car(100, "BMW", 2000));
        cars.add(car(100, "BMW", 2000));

        assertEquals(3, counter.countOccurrences(cars, target));
    }

    /**
     * Проверяет, что передача null-коллекции приводит к NullPointerException.
     */
    @Test
    void shouldThrowNullPointerExceptionForNullCollection() {
        CarCounter counter = new CarCounter();
        Car target = car(150, "Toyota", 2015);
        assertThrows(NullPointerException.class, () -> counter.countOccurrences(null, target));
    }

    /**
     * Проверяет, что передача null-цели сравнения приводит к NullPointerException.
     */
    @Test
    void shouldThrowNullPointerExceptionForNullTarget() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        assertThrows(NullPointerException.class, () -> counter.countOccurrences(cars, null));
    }
}
