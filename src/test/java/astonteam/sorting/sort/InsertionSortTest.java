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

    @BeforeEach
    void setUp() {
        cars = new CarArrayList();
        for (int i = 0; i < 100; i++) {
            cars.add(
                    Car.builder()
                            .model("Model" + i)
                            .year(2000 + (i % 27))
                            .power(100 + i)
                            .build()
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
        singleCar.add(Car.builder().model("Solo").year(2020).power(150).build());
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
        cars.add(Car.builder()
                .model("Model50")
                .year(2025)
                .power(150)
                .build());
        cars.add(Car.builder()
                .model("Model50")
                .year(2025)
                .power(150)
                .build());
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getPower() <= cars.get(i).getPower(),
                    "Коллекция не отсортирована по мощности на позиции " + i
            );
        }
    }

    @Test
    void whenEqualPowerThenOriginalOrderIsPreserved() {
        Car first = Car.builder()
                .model("First")
                .year(2020)
                .power(150)
                .build();
        Car second = Car.builder()
                .model("Second")
                .year(2021)
                .power(150)
                .build();
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