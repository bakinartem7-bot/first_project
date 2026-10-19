package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты меню поиска: поиск по мощности, модели, году и всей машине,
 * отсутствие совпадений, пустая коллекция и возврат без поиска.
 *
 * <p>Файлы результатов пишутся во временный каталог: настоящий каталог
 * {@code results/} тестами не трогается.</p>
 */
class SearchMenuTest {

    /** Коллекция автомобилей, по которой выполняется поиск. */
    private CarArrayList cars;

    /** Временный каталог для файлов результатов. */
    @TempDir
    Path tempDir;

    /**
     * Перенаправляет каталог результатов во временный каталог
     * и наполняет коллекцию тремя тестовыми автомобилями.
     */
    @BeforeEach
    void setUp() {
        System.setProperty(ResultSaver.RESULTS_DIRECTORY_PROPERTY, tempDir.toString());
        cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2021));
        cars.add(car(2000, "toyota", 1999));
        cars.add(car(100, "BMW", 2021));
    }

    /**
     * Возвращает систему в исходное состояние после теста.
     */
    @AfterEach
    void resetResultsDirectory() {
        System.clearProperty(ResultSaver.RESULTS_DIRECTORY_PROPERTY);
    }

    /**
     * Создаёт тестовый автомобиль.
     *
     * @param power мощность двигателя
     * @param model название модели
     * @param year  год выпуска
     * @return созданный автомобиль
     */
    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    /**
     * Запускает поиск с заданным пользовательским вводом.
     *
     * @param input строки, подаваемые программе в стандартный ввод
     * @param cars  коллекция, в которой выполняется поиск
     * @return результат выполнения: перехваченный вывод и возможное исключение
     */
    private TestSupport.Outcome runSearch(String input, CarArrayList cars) {
        return TestSupport.run(input, () -> {
            try (ConsoleInput consoleInput = new ConsoleInput()) {
                SearchMenu.searchCars(cars, consoleInput, new ResultSaver());
            }
        });
    }

    /**
     * Проверяет, что поиск по мощности выводит совпадение и не выводит
     * остальные автомобили.
     */
    @Test
    void whenSearchingByPowerThenPrintsMatches() {
        TestSupport.Outcome outcome = runSearch("1\n150\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Мощность: 150; Модель: Toyota; Год: 2021"));
        assertFalse(outcome.output().contains("Мощность: 100; Модель: BMW; Год: 2021"));
    }

    /**
     * Проверяет, что поиск по модели регистронезависим и выводит все
     * совпадения.
     */
    @Test
    void whenSearchingByModelThenPrintsAllMatchesIgnoringCase() {
        TestSupport.Outcome outcome = runSearch("2\nTOYOTA\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Мощность: 150; Модель: Toyota; Год: 2021"));
        assertTrue(outcome.output().contains("Мощность: 2000; Модель: toyota; Год: 1999"));
    }

    /**
     * Проверяет, что поиск по году выводит все автомобили этого года.
     */
    @Test
    void whenSearchingByYearThenPrintsAllMatches() {
        TestSupport.Outcome outcome = runSearch("3\n2021\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Мощность: 150; Модель: Toyota; Год: 2021"));
        assertTrue(outcome.output().contains("Мощность: 100; Модель: BMW; Год: 2021"));
    }

    /**
     * Проверяет, что поиск всей машины выводит только точное совпадение
     * по всем трём полям.
     */
    @Test
    void whenSearchingByWholeCarThenPrintsOnlyExactMatch() {
        TestSupport.Outcome outcome = runSearch("4\n150\nTOYOTA\n2021\nнет\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Мощность: 150; Модель: Toyota; Год: 2021"));
        assertFalse(outcome.output().contains("Мощность: 2000; Модель: toyota; Год: 1999"));
    }

    /**
     * Проверяет, что при ответе «да» результат поиска дописывается в history.txt
     * с шапкой действия и данными найденных автомобилей.
     *
     * @throws Exception при чтении записанного файла
     */
    @Test
    void whenSearchingWithAppendThenWritesHistory() throws Exception {
        TestSupport.Outcome outcome = runSearch("1\n150\nда\n", cars);

        assertNull(outcome.error());
        Path history = tempDir.resolve("history.txt");
        assertTrue(Files.exists(history));
        String content = Files.readString(history);
        assertTrue(content.contains("Результат поиска"));
        assertTrue(content.contains("150;Toyota;2021"));
    }

    /**
     * Проверяет, что при отсутствии совпадений выводится сообщение
     * об этом; вопрос о сохранении не задаётся, поэтому его ответ
     * не передаётся во ввод.
     */
    @Test
    void whenSearchHasNoMatchesThenPrintsMessage() {
        TestSupport.Outcome outcome = runSearch("1\n999\n", cars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Совпадений не найдено."));
    }

    /**
     * Проверяет, что поиск по пустой коллекции просит сначала заполнить
     * её автомобилями.
     */
    @Test
    void whenCollectionIsEmptyThenPrintsMessage() {
        CarArrayList emptyCars = new CarArrayList();
        TestSupport.Outcome outcome = runSearch("", emptyCars);

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сначала заполните коллекцию автомобилями."));
    }

    /**
     * Проверяет, что выбор возврата завершает меню поиска без поискового
     * запроса и вывода результатов.
     */
    @Test
    void whenBackIsSelectedThenReturnsWithoutSearching() {
        TestSupport.Outcome outcome = runSearch("5\n", cars);

        assertNull(outcome.error());
        assertFalse(outcome.output().contains("Найденные автомобили:"));
    }
}