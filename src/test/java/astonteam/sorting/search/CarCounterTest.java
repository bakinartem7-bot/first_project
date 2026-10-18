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

    private static Car car(int power, String model, int year) {
        return Car.builder().power(power).model(model).year(year).build();
    }

    @Test
    void shouldReturnZeroForEmptyCollection() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);
        assertEquals(0, counter.countOccurrences(cars, target));
    }

    @Test
    void shouldCountSingleMatch() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);
        cars.add(target);
        assertEquals(1, counter.countOccurrences(cars, target));
    }

    @Test
    void shouldReturnZeroWhenNoMatches() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        Car target = car(150, "Toyota", 2015);
        cars.add(car(100, "BMW", 2000));
        assertEquals(0, counter.countOccurrences(cars, target));
    }

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

    @Test
    void shouldThrowNullPointerExceptionForNullCollection() {
        CarCounter counter = new CarCounter();
        Car target = car(150, "Toyota", 2015);
        assertThrows(NullPointerException.class, () -> counter.countOccurrences(null, target));
    }

    @Test
    void shouldThrowNullPointerExceptionForNullTarget() {
        CarCounter counter = new CarCounter();
        CarArrayList cars = new CarArrayList();
        assertThrows(NullPointerException.class, () -> counter.countOccurrences(cars, null));
    }
}
