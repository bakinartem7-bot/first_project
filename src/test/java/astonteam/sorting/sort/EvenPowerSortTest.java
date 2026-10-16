package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

class EvenPowerSortTest {
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
    void whenSortingThenOddPowerCarsStayAtTheirPositions() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(300, "Even-300", 2022));
        testCars.add(car(101, "Odd-101", 2021));
        testCars.add(car(100, "Even-100", 2020));
        testCars.add(car(99, "Odd-99", 2019));
        testCars.add(car(200, "Even-200", 2018));

        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);

        assertEquals(100, testCars.get(0).getPower());
        assertEquals(101, testCars.get(1).getPower());
        assertEquals(200, testCars.get(2).getPower());
        assertEquals(99, testCars.get(3).getPower());
        assertEquals(300, testCars.get(4).getPower());
    }

    @Test
    void whenSortingEmptyCollectionThenNoException() {
        CarArrayList emptyCars = new CarArrayList();
        new EvenPowerSort("power").sort(emptyCars, CarComparators.BY_POWER);
        assertEquals(0, emptyCars.size(),
                "Пустая коллекция должна оставаться пустой после сортировки");
    }

    @Test
    void whenSortingSingleElementCollectionThenNoException() {
        CarArrayList singleCar = new CarArrayList();
        singleCar.add(car(150, "Solo", 2020));
        new EvenPowerSort("power").sort(singleCar, CarComparators.BY_POWER);
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
    void whenSortingAlreadySortedCollectionThenEvenCarsStaySortedAndOddCarsStayInPlace() {
        Car oddCarAtIndex1 = cars.get(1);
        Car oddCarAtIndex3 = cars.get(3);

        new EvenPowerSort("power").sort(cars, CarComparators.BY_POWER);

        int previousEvenPower = Integer.MIN_VALUE;
        for (Car car : cars) {
            if (car.getPower() % 2 == 0) {
                assertTrue(car.getPower() >= previousEvenPower);
                previousEvenPower = car.getPower();
            }
        }

        assertSame(oddCarAtIndex1, cars.get(1));
        assertSame(oddCarAtIndex3, cars.get(3));
    }

    @Test
    void whenSortingReverseOrderedCollectionThenItBecomesSorted() {
        CarArrayList testCars = new CarArrayList();

        testCars.add(car(300, "C", 2022));
        testCars.add(car(200, "B", 2021));
        testCars.add(car(100, "A", 2020));
        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);
        assertEquals(100, testCars.get(0).getPower());
        assertEquals(200, testCars.get(1).getPower());
        assertEquals(300, testCars.get(2).getPower());
    }

    @Test
    void whenEvenCarsHaveEqualPowerThenTheirOriginalOrderIsPreserved() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(200, "First", 2020));
        testCars.add(car(100, "Smaller", 2021));
        testCars.add(car(200, "Second", 2022));

        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);

        assertEquals("Smaller", testCars.get(0).getModel());
        assertEquals("First", testCars.get(1).getModel());
        assertEquals("Second", testCars.get(2).getModel());
    }

    @Test
    void whenThereAreNoEvenPowerCarsThenCollectionStaysUnchanged() {
        CarArrayList testCars = new CarArrayList();
        Car first = car(101, "First", 2020);
        Car second = car(99, "Second", 2021);

        testCars.add(first);
        testCars.add(second);

        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);

        assertSame(first, testCars.get(0));
        assertSame(second, testCars.get(1));
    }

    @Test
    void whenNullComparatorThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new EvenPowerSort("power").sort(cars, null)
        );
        assertEquals("comparator", exception.getMessage());
    }

    @Test
    void whenNullCollectionThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new EvenPowerSort("power")
                        .sort(null, CarComparators.BY_POWER)
        );
        assertEquals("cars", exception.getMessage());
    }
}