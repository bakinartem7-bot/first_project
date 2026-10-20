package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.search.CarCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты {@link CountMenu}: проверяют ввод искомого автомобиля,
 * валидацию его полей и вывод количества найденных вхождений.
 */
class CountMenuTest {

    /**
     * Создаёт тестовый автомобиль.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
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
     * Проверяет, что для пустой коллекции выводится сообщение
     * о необходимости сначала добавить автомобили.
     */
    @Test
    void whenCollectionIsEmptyThenPrintsMessage() {
        CarArrayList cars = new CarArrayList();
        TestSupport.Outcome outcome = TestSupport.run(
                "",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains(
                "Сначала заполните коллекцию автомобилями."
        ));
    }

    /**
     * Проверяет корректный подсчёт нескольких одинаковых автомобилей.
     */
    @Test
    void whenCollectionContainsMatchesThenPrintsTheirCount() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2020));
        cars.add(car(100, "Audi", 2019));
        cars.add(car(150, "Toyota", 2020));
        cars.add(car(150, "Toyota", 2021));
        cars.add(car(150, "Toyota", 2020));
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains(
                "Количество вхождений в коллекцию: 3"
        ));
    }

    /**
     * Проверяет, что при отсутствии совпадений выводится нулевой результат.
     */
    @Test
    void whenCollectionHasNoMatchesThenPrintsZero() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(100, "Audi", 2019));
        cars.add(car(200, "Bmw", 2021));
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Количество вхождений в коллекцию: 0"));
    }

    /**
     * Проверяет, что отрицательная мощность отклоняется,
     * после чего повторно запрашивается только мощность.
     */
    @Test
    void whenPowerIsNegativeThenRequestsPowerAgain() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2020));
        TestSupport.Outcome outcome = TestSupport.run(
                "-150\n150\nToyota\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Мощность не может быть отрицательной."));
        assertTrue(outcome.output().contains("Количество вхождений в коллекцию: 1"));
    }

    /**
     * Проверяет, что некорректная модель отклоняется,
     * после чего повторно запрашивается только модель.
     */
    @Test
    void whenModelIsInvalidThenRequestsModelAgain() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2020));
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nInvalid;Model\nToyota\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Модель не должна содержать символ «;»."));
        assertTrue(outcome.output().contains("Количество вхождений в коллекцию: 1"));
    }

    /**
     * Проверяет, что некорректный год отклоняется,
     * после чего повторно запрашивается только год.
     */
    @Test
    void whenYearIsInvalidThenRequestsYearAgain() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2020));
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n1800\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Год выпуска должен быть в диапазоне"));
        assertTrue(outcome.output().contains("Количество вхождений в коллекцию: 1"));
    }

    /**
     * Проверяет, что пять ошибок в пределах ввода одного автомобиля
     * приводят к {@link AttemptsExceededException}.
     */
    @Test
    void whenFiveErrorsOccurThenThrowsAttemptsExceededException() {
        CarArrayList cars = new CarArrayList();
        cars.add(car(150, "Toyota", 2020));
        String inputData =
                "ошибка\n"
                        + "ошибка\n"
                        + "ошибка\n"
                        + "ошибка\n"
                        + "150\n"
                        + "\n";
        TestSupport.Outcome outcome = TestSupport.run(
                inputData,
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CountMenu.countCars(cars, input, new CarCounter());
                    }
                }
        );
        assertInstanceOf(AttemptsExceededException.class, outcome.error());
        assertTrue(outcome.output().contains("Модель не должна быть пустой."));
    }
}