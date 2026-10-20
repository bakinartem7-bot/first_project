package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Тесты меню сортировки: выбор алгоритма и ключа сортировки,
 * порядок сохранения коллекции и обработка пустой коллекции.
 *
 * <p>Все файлы результатов пишутся во временный каталог, тесты не трогают
 * настоящий каталог {@code results/}.</p>
 */
class SortingMenuTest {

    /** Временный каталог для файлов результатов. */
    @TempDir
    Path tempDir;

    /**
     * Перенаправляет каталог результатов во временный каталог.
     */
    @BeforeEach
    void useTemporaryResultsDirectory() {
        System.setProperty(ResultSaver.RESULTS_DIRECTORY_PROPERTY, tempDir.toString());
    }

    /**
     * Возвращает систему в исходное состояние после теста.
     */
    @AfterEach
    void resetResultsDirectory() {
        System.clearProperty(ResultSaver.RESULTS_DIRECTORY_PROPERTY);
    }

    /**
     * Проверяет, что merge-сортировка по мощности располагает автомобили
     * по возрастанию мощности.
     */
    @Test
    void whenMergeSortByPowerThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
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

    /**
     * Проверяет, что merge-сортировка по модели располагает автомобили
     * по алфавиту и выводит их в том же порядке.
     */
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

    /**
     * Проверяет, что сортировка вставками по году располагает автомобили
     * по возрастанию года выпуска.
     */
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

    /**
     * Проверяет, что сортировка чётных вставками меняет местами только
     * автомобили с чётной мощностью, не затрагивая нечётный.
     */
    @Test
    void whenEvenPowerInsertionSortThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(101).year(2020).build());
        Car oddCar = cars.get(2);
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
        assertSame(oddCar, cars.get(2));
        assertTrue(outcome.output().contains("Алгоритм: EvenPowerInsertionSort"));
    }

    /**
     * Проверяет, что сортировка чётных слиянием меняет местами только
     * автомобили с чётной мощностью, не затрагивая нечётный.
     */
    @Test
    void whenEvenPowerMergeSortThenCollectionIsSorted() {
        CarArrayList cars = new CarArrayList();
        cars.add(Car.builder().model("Toyota").power(150).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(100).year(2020).build());
        cars.add(Car.builder().model("Toyota").power(101).year(2020).build());
        Car oddCar = cars.get(2);
        TestSupport.Outcome outcome = TestSupport.run(
                "4\nнет\n",
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
        assertSame(oddCar, cars.get(2));
        assertTrue(outcome.output().contains("Алгоритм: EvenPowerMergeSort"));
    }

    /**
     * Проверяет, что сортировка пустой коллекции сообщает о необходимости
     * сначала заполнить коллекцию и не завершает программу с ошибкой.
     */
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

    /**
     * Проверяет, что выбор пункта "Назад в главное меню"
     * возвращает в главное меню.
     */
    @Test
    void whenBackSelectedInAlgorithmMenuThenCollectionIsNotChanged() {
        CarArrayList cars = new CarArrayList();
        Car first = Car.builder()
                .model("Toyota")
                .power(150)
                .year(2020)
                .build();
        Car second = Car.builder()
                .model("Audi")
                .power(100)
                .year(2019)
                .build();

        cars.add(first);
        cars.add(second);

        TestSupport.Outcome outcome = TestSupport.run(
                "5\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
                    }
                }
        );

        assertNull(outcome.error());
        assertSame(first, cars.get(0));
        assertSame(second, cars.get(1));
        assertFalse(outcome.output().contains("Сортировка завершена."));
    }

    /**
     * Проверяет, что выбор пункта "Назад к выбору алгоритма"
     * возвращает в меню выбора.
     */
    @Test
    void whenBackSelectedInFieldMenuThenReturnsToAlgorithmMenu() {
        CarArrayList cars = new CarArrayList();
        Car first = Car.builder()
                .model("Toyota")
                .power(150)
                .year(2020)
                .build();
        Car second = Car.builder()
                .model("Audi")
                .power(100)
                .year(2019)
                .build();

        cars.add(first);
        cars.add(second);

        TestSupport.Outcome outcome = TestSupport.run(
                "1\n4\n5\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        SortingMenu.sortCars(cars, input, new ResultSaver());
                    }
                }
        );

        assertNull(outcome.error());
        assertSame(first, cars.get(0));
        assertSame(second, cars.get(1));
        assertFalse(outcome.output().contains("Сортировка завершена."));
    }
}