package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link SortingMenu}: сортировка коллекции различными алгоритмами и по разным полям.
 */
class SortingMenuTest {

    /**
     * Проверяет, что сортировка слиянием по мощности упорядочивает коллекцию.
     */
    @Test
    void whenMergeSortByPowerThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "2\n1\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input);
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals(100, cars.get(0).getPower());
        assertEquals(150, cars.get(1).getPower());
        assertTrue(outcome.output().contains("Алгоритм: MergeSort"));
    }

    /**
     * Проверяет, что сортировка слиянием по модели упорядочивает коллекцию и порядок вывода.
     */
    @Test
    void whenMergeSortByModelThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Audi").power(100).year(2020).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "2\n2\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input);
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals("Audi", cars.get(0).getModel());
        assertEquals("Toyota", cars.get(1).getModel());
        assertTrue(outcome.output().contains("Алгоритм: MergeSort"));
        String output = outcome.output();
        assertTrue(
                output.indexOf("Audi") < output.indexOf("Toyota"),
                "Audi должна идти перед Toyota в выводе"
        );
    }

    /**
     * Проверяет, что сортировка вставками по году упорядочивает коллекцию.
     */
    @Test
    void whenInsertionSortByYearThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2019).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "1\n3\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input);
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals(2019, cars.get(0).getYear());
        assertEquals(2020, cars.get(1).getYear());
        assertTrue(outcome.output().contains("Алгоритм: InsertionSort"));
    }

    /**
     * Проверяет, что сортировка по чётной мощности упорядочивает коллекцию.
     */
    @Test
    void whenEvenPowerSortThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(101).year(2020).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "3\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input);
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals(100, cars.get(0).getPower());
        assertEquals(150, cars.get(1).getPower());
        assertEquals(101, cars.get(2).getPower());
        assertTrue(outcome.output().contains("Алгоритм: EvenPowerInsertionSort"));
    }

    /**
     * Проверяет, что при пустой коллекции выводится соответствующее сообщение.
     */
    @Test
    void whenCollectionIsEmptyThenPrintsMessage() {
        CarArrayList cars = new CarArrayList();
        TestSupport.Outcome outcome = TestSupport.run(
                "",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input);
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сначала заполните коллекцию автомобилями."));
    }


}