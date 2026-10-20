package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты {@link CarInputReader}: проверяют валидацию отдельных полей,
 * создание автомобиля и управление счётчиком неудачных попыток.
 */
class CarInputReaderTest {

    /**
     * Проверяет, что конструкторы отклоняют {@code null}-зависимости.
     */
    @Test
    void constructorRejectsNullDependencies() {
        assertThrows(
                NullPointerException.class,
                () -> new CarInputReader(null)
        );

        TestSupport.Outcome outcome = TestSupport.run(
                "",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        assertThrows(
                                NullPointerException.class,
                                () -> new CarInputReader(input, null)
                        );
                    }
                }
        );
        assertNull(outcome.error());
    }

    /**
     * Проверяет, что после некорректной мощности метод повторяет
     * запрос и возвращает следующее допустимое значение.
     */
    @Test
    void readPowerRetriesAfterInvalidValue() {
        TestSupport.Outcome outcome = TestSupport.run(
                "-100\n150\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader =
                                new CarInputReader(input, new CarValidator());

                        assertEquals(150, reader.readPower());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Мощность не может быть отрицательной."));
    }

    /**
     * Проверяет, что после некорректной модели метод повторяет
     * запрос и возвращает следующую допустимую модель.
     */
    @Test
    void readModelRetriesAfterInvalidValue() {
        TestSupport.Outcome outcome = TestSupport.run(
                "Invalid;Model\nToyota\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        assertEquals("Toyota", reader.readModel());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Модель не должна содержать символ «;»."));
    }

    /**
     * Проверяет, что после некорректного года метод повторяет
     * запрос и возвращает следующий допустимый год.
     */
    @Test
    void readYearRetriesAfterInvalidValue() {
        TestSupport.Outcome outcome = TestSupport.run(
                "1800\n2022\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        assertEquals(2022, reader.readYear());
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Год выпуска должен быть в диапазоне"));
    }

    /**
     * Проверяет создание автомобиля из корректно введённых полей.
     */
    @Test
    void readCarBuildsCarFromValidFields() {
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        Car car = reader.readCar();
                        assertEquals(150, car.getPower());
                        assertEquals("Toyota", car.getModel());
                        assertEquals(2020, car.getYear());
                    }
                }
        );
        assertNull(outcome.error());
    }

    /**
     * Проверяет, что ошибки разных полей одного автомобиля суммируются:
     * успешный ввод мощности не сбрасывает счётчик до завершения
     * ввода всего автомобиля.
     */
    @Test
    void readCarAccumulatesErrorsAcrossFields() {
        TestSupport.Outcome outcome = TestSupport.run(
                "ошибка\n"
                        + "ошибка\n"
                        + "ошибка\n"
                        + "ошибка\n"
                        + "150\n"
                        + "\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        reader.readCar();
                    }
                }
        );
        assertInstanceOf(AttemptsExceededException.class, outcome.error());
    }

    /**
     * Проверяет, что после успешного ввода целого автомобиля счётчик
     * ошибок сбрасывается перед вводом следующего автомобиля.
     */
    @Test
    void successfulCarResetsAttemptsBeforeNextCar() {
        String inputData =
                "ошибка\nошибка\nошибка\nошибка\n"
                        + "150\nToyota\n2020\n"
                        + "ошибка\nошибка\nошибка\nошибка\n"
                        + "100\nAudi\n2019\n";
        TestSupport.Outcome outcome = TestSupport.run(
                inputData,
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        Car first = reader.readCar();
                        Car second = reader.readCar();
                        assertEquals("Toyota", first.getModel());
                        assertEquals("Audi", second.getModel());
                    }
                }
        );
        assertNull(outcome.error());
    }

    /**
     * Проверяет, что успешный ввод отдельного критерия сбрасывает
     * счётчик ошибок перед следующим критерием.
     */
    @Test
    void successfulStandaloneCriterionResetsAttempts() {
        String inputData =
                "ошибка\nошибка\nошибка\nошибка\n150\n"
                        + "ошибка\nошибка\nошибка\nошибка\n100\n";
        TestSupport.Outcome outcome = TestSupport.run(
                inputData,
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        assertEquals(150, reader.readPower());
                        assertEquals(100, reader.readPower());
                    }
                }
        );
        assertNull(outcome.error());
    }

    /**
     * Проверяет использование пользовательских текстов приглашений
     * при вводе автомобиля.
     */
    @Test
    void readCarUsesCustomPrompts() {
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n2020\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarInputReader reader = new CarInputReader(input, new CarValidator());
                        reader.readCar(
                                "Тестовая мощность: ",
                                "Тестовая модель: ",
                                "Тестовый год: "
                        );
                    }
                }
        );
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Тестовая мощность:"));
        assertTrue(outcome.output().contains("Тестовая модель:"));
        assertTrue(outcome.output().contains("Тестовый год:"));
    }
}