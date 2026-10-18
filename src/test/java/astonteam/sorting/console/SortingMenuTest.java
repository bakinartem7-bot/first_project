package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SortingMenuTest {

    @Test
    void whenMergeSortByPowerThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
        // ДОБАВЛЕНО: \nнет\n в конец ввода
        TestSupport.Outcome outcome = TestSupport.run(
                "2\n1\nнет\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals(100, cars.get(0).getPower());
        assertEquals(150, cars.get(1).getPower());
        assertTrue(outcome.output().contains("Алгоритм: MergeSort"));
    }

    @Test
    void whenMergeSortByModelThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Audi").power(100).year(2020).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "2\n2\nнет\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
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

    @Test
    void whenInsertionSortByYearThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2019).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "1\n3\nнет\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals(2019, cars.get(0).getYear());
        assertEquals(2020, cars.get(1).getYear());
        assertTrue(outcome.output().contains("Алгоритм: InsertionSort"));
    }

    @Test
    void whenEvenPowerSortThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(101).year(2020).build());
        TestSupport.Outcome outcome = TestSupport.run(
                "3\nнет\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
                    }
                }
        );
        assertNull(outcome.error());
        assertEquals(100, cars.get(0).getPower());
        assertEquals(150, cars.get(1).getPower());
        assertEquals(101, cars.get(2).getPower());
        assertTrue(outcome.output().contains("Алгоритм: EvenPowerInsertionSort"));
    }

    @Test
    void whenCollectionIsEmptyThenPrintsMessage() {
        CarArrayList cars = new CarArrayList();
        TestSupport.Outcome outcome = TestSupport.run(
                "",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сначала заполните коллекцию автомобилями."));
    }
}