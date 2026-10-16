package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link SearchMenu}: поиск автомобилей из консольного меню по различным критериям.
 */
class SearchMenuTest {

    /**
     * Коллекция автомобилей, наполняемая перед каждым тестом.
     */
    private CarArrayList cars;

    /**
     * Подготавливает перед каждым тестом коллекцию машин с тестовыми данными.
     */
    @BeforeEach
    void setUp() {
        cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2021));
        cars.add(car(2000, "toyota", 1999));
        cars.add(car(100, "BMW", 2021));
    }

    /**
     * Создаёт тестовый автомобиль с указанными параметрами.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year год выпуска автомобиля
     * @return построенный объект {@link Car}
     */
    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    /**
     * Запускает поиск через меню с заданным вводом и коллекцией.
     *
     * @param input данные, подаваемые в консольный ввод
     * @param cars коллекция автомобилей для поиска
     * @return результат выполнения с перехваченным выводом и возможной ошибкой
     */
    private TestSupport.Outcome runSearch(String input, CarArrayList cars) {
        return TestSupport.run(input, () -> {
            try (ConsoleInput consoleInput = new ConsoleInput()) {
                SearchMenu.searchCars(cars, consoleInput);
            }
        });
    }

    /**
     * Проверяет, что поиск по мощности выводит найденные совпадения.
     */
    @Test
    void whenSearchingByPowerThenPrintsMatches() {
        TestSupport.Outcome outcome = runSearch("1\n150\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertFalse(outcome.output().contains("Car{power=100, model='BMW', year=2021}"));
    }

    /**
     * Проверяет, что поиск по модели выводит все совпадения без учёта регистра.
     */
    @Test
    void whenSearchingByModelThenPrintsAllMatchesIgnoringCase() {
        TestSupport.Outcome outcome = runSearch("2\nTOYOTA\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertTrue(outcome.output().contains("Car{power=2000, model='toyota', year=1999}"));
    }

    /**
     * Проверяет, что поиск по году выводит все совпадения.
     */
    @Test
    void whenSearchingByYearThenPrintsAllMatches() {
        TestSupport.Outcome outcome = runSearch("3\n2021\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertTrue(outcome.output().contains("Car{power=100, model='BMW', year=2021}"));
    }

    /**
     * Проверяет, что поиск по целому автомобилю выводит только точное совпадение.
     */
    @Test
    void whenSearchingByWholeCarThenPrintsOnlyExactMatch() {
        TestSupport.Outcome outcome = runSearch("4\n150\nTOYOTA\n2021\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Car{power=150, model='Toyota', year=2021}"));
        assertFalse(outcome.output().contains("Car{power=2000, model='toyota', year=1999}"));
    }

    /**
     * Проверяет, что при отсутствии совпадений выводится соответствующее сообщение.
     */
    @Test
    void whenSearchHasNoMatchesThenPrintsMessage() {
        TestSupport.Outcome outcome = runSearch("1\n999\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Совпадений не найдено."));
    }

    /**
     * Проверяет, что при пустой коллекции выводится соответствующее сообщение.
     */
    @Test
    void whenCollectionIsEmptyThenPrintsMessage() {
        CarArrayList emptyCars = new CarArrayList();

        TestSupport.Outcome outcome = runSearch("", emptyCars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сначала заполните коллекцию автомобилями."));
    }

    /**
     * Проверяет, что при выборе возврата меню закрывается без поиска.
     */
    @Test
    void whenBackIsSelectedThenReturnsWithoutSearching() {
        TestSupport.Outcome outcome = runSearch("5\n", cars);

        assertNull(outcome.error());
        assertFalse(outcome.output().contains("Найденные автомобили:"));
    }
}