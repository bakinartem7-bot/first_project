package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsertionSortTest {
    private CarArrayList cars;

    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    @BeforeEach
    void setUp() {
        cars = new CarArrayList();
        for (int i = 0; i < 100; i++) {
            cars.add(
                    car(100 + i, "Model" + i, 2000 + (i % 27))
            );
        }
    }

    @Test
    void whenSortingByPowerThenCollectionIsSorted() {
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getPower() <= cars.get(i).getPower(),
                    "Коллекция не отсортирована по мощности на позиции " + i
            );
        }
    }

    @Test
    void whenSortingByYearThenCollectionIsSorted() {
        new InsertionSort("year").sort(cars, CarComparators.BY_YEAR);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getYear() <= cars.get(i).getYear(),
                    "Коллекция не отсортирована по году на позиции " + i
            );
        }
    }


    @Test
    void whenSortingByModelThenCollectionIsSorted() {
        new InsertionSort("model").sort(cars, CarComparators.BY_MODEL);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getModel().compareTo(cars.get(i).getModel()) <= 0,
                    "Коллекция не отсортирована по модели на позиции " + i
            );
        }
    }

    @Test
    void whenSortingEmptyCollectionThenNoException() {
        CarArrayList emptyCars = new CarArrayList();
        new InsertionSort("power").sort(emptyCars, CarComparators.BY_POWER);
        assertEquals(0, emptyCars.size(),
                "Пустая коллекция должна оставаться пустой после сортировки");
    }

    @Test
    void whenSortingSingleElementCollectionThenNoException() {
        CarArrayList singleCar = new CarArrayList();
        singleCar.add(car(150, "Solo", 2020));
        new InsertionSort("power").sort(singleCar, CarComparators.BY_POWER);
        assertEquals("Solo", singleCar.get(0).getModel(),
                "Элемент коллекции должен оставаться неизменным после сортировки");
        assertEquals(2020, singleCar.get(0).getYear(),
                "Элемент коллекции должен оставаться неизменным после сортировки");
        assertEquals(150, singleCar.get(0).getPower(),
                "Элемент коллекции должен оставаться неизменным после сортировки");
        assertEquals(1, singleCar.size(),
                "Коллекция с одним элементом должна оставаться неизменной после сортировки");
    }

    @Test
    void whenSortingAlreadySortedCollectionThenNoException() {
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getPower() <= cars.get(i).getPower(),
                    "Коллекция не отсортирована по мощности на позиции " + i
            );
        }
    }

    @Test
    void whenCollectionHasDuplicatesThenSortedCorrectly() {
        cars.add(car(150, "Model50", 2025));
        cars.add(car(150, "Model50", 2025));
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getPower() <= cars.get(i).getPower(),
                    "Коллекция не отсортирована по мощности на позиции " + i
            );
        }
    }

    @Test
    void whenSortingReverseOrderedCollectionThenItBecomesSorted() {
        CarArrayList testCars = new CarArrayList();

        testCars.add(car(300, "C", 2022));
        testCars.add(car(200, "B", 2021));
        testCars.add(car(100, "A", 2020));
        new InsertionSort("power").sort(testCars, CarComparators.BY_POWER);
        assertEquals(100, testCars.get(0).getPower());
        assertEquals(200, testCars.get(1).getPower());
        assertEquals(300, testCars.get(2).getPower());
    }

    @Test
    void whenEqualPowerThenOriginalOrderIsPreserved() {
        Car first = car(150, "First", 2020);
        Car second = car(150, "Second", 2021);
        CarArrayList testCars = new CarArrayList();
        testCars.add(first);
        testCars.add(second);
        new InsertionSort("power").sort(testCars, CarComparators.BY_POWER);
        assertEquals("First", testCars.get(0).getModel());
        assertEquals("Second", testCars.get(1).getModel());
    }

    @Test
    void whenNullComparatorThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new InsertionSort("power").sort(cars, null)
        );
        assertEquals("comparator", exception.getMessage());
    }

    @Test
    void whenNullCollectionThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new InsertionSort("power")
                        .sort(null, CarComparators.BY_POWER)
        );
        assertEquals("cars", exception.getMessage());
    }
}