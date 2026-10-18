package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchMenuTest {

    private CarArrayList cars;

    @BeforeEach
    void setUp() {
        cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2021));
        cars.add(car(2000, "toyota", 1999));
        cars.add(car(100, "BMW", 2021));
    }

    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    private TestSupport.Outcome runSearch(String input, CarArrayList cars) {
        return TestSupport.run(input, () -> {
            try (ConsoleInput consoleInput = new ConsoleInput()) {
                // Передаем new ResultSaver(), чтобы тест компилировался
                SearchMenu.searchCars(cars, consoleInput, new ResultSaver());
            }
        });
    }

    @Test
    void whenSearchingByPowerThenPrintsMatches() {
        // ДОБАВЛЕНО: \nнет\n (отвечаем "нет" на вопрос о сохранении)
        TestSupport.Outcome outcome = runSearch("1\n150\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertFalse(outcome.output().contains("Car{power=100, model='BMW', year=2021}"));
    }

    @Test
    void whenSearchingByModelThenPrintsAllMatchesIgnoringCase() {
        TestSupport.Outcome outcome = runSearch("2\nTOYOTA\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertTrue(outcome.output().contains("Car{power=2000, model='toyota', year=1999}"));
    }

    @Test
    void whenSearchingByYearThenPrintsAllMatches() {
        TestSupport.Outcome outcome = runSearch("3\n2021\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertTrue(outcome.output().contains("Car{power=100, model='BMW', year=2021}"));
    }

    @Test
    void whenSearchingByWholeCarThenPrintsOnlyExactMatch() {
        TestSupport.Outcome outcome = runSearch("4\n150\nTOYOTA\n2021\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertFalse(outcome.output().contains("Car{power=2000, model='toyota', year=1999}"));
    }

    @Test
    void whenSearchHasNoMatchesThenPrintsMessage() {
        // Здесь совпадений нет, поэтому программа не спросит про сохранение,
        // но добавить "нет" безопасно
        TestSupport.Outcome outcome = runSearch("1\n999\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Совпадений не найдено."));
    }

    @Test
    void whenCollectionIsEmptyThenPrintsMessage() {
        CarArrayList emptyCars = new CarArrayList();
        TestSupport.Outcome outcome = runSearch("", emptyCars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сначала заполните коллекцию автомобилями."));
    }

    @Test
    void whenBackIsSelectedThenReturnsWithoutSearching() {
        TestSupport.Outcome outcome = runSearch("5\n", cars);

        assertNull(outcome.error());
        assertFalse(outcome.output().contains("Найденные автомобили:"));
    }
}