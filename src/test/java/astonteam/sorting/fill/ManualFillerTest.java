package astonteam.sorting.fill;

import astonteam.sorting.TestSupport;
import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.validation.CarValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import astonteam.sorting.console.AttemptsExceededException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Тесты для {@link ManualFiller}: ручной ввод автомобилей через консоль с валидацией и повторами.
 */
class ManualFillerTest {

    /**
     * Валидатор автомобилей, используемый заполняющим ручным способом.
     */
    private final CarValidator validator = new CarValidator();

    /**
     * Проверяет, что заполнение читает из ввода ровно запрошенное количество машин.
     */
    @Test
    void fillReadsRequestedNumberOfCars() {
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n2015\n90\nBMW\n2010\n", () -> {
                    ConsoleInput input = new ConsoleInput();
                    CarArrayList cars = new ManualFiller(input, validator).fill(2);
                    assertEquals(2, cars.size());
                    assertEquals("Toyota", cars.get(0).getModel());
                    assertEquals(150, cars.get(0).getPower());
                    assertEquals("BMW", cars.get(1).getModel());
                    assertEquals(2010, cars.get(1).getYear());
                    input.close();
                });
        assertNull(outcome.error());
    }

    /**
     * Проверяет, что неположительный размер коллекции приводит к IllegalArgumentException.
     */
    @Test
    void fillRejectsNonPositiveSize() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            ManualFiller filler = new ManualFiller(input, validator);
            assertThrows(IllegalArgumentException.class, () -> filler.fill(0));
            assertThrows(IllegalArgumentException.class, () -> filler.fill(-1));
            input.close();
        });
    }

    /**
     * Проверяет, что размер коллекции больше допустимого лимита приводит к IllegalArgumentException.
     */
    @Test
    void fillRejectsTooBigSize() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            ManualFiller filler = new ManualFiller(input, validator);
            assertThrows(IllegalArgumentException.class,
                    () -> filler.fill(Limits.MAX_COLLECTION_SIZE + 1));
            input.close();
        });
    }

    /**
     * Проверяет, что после ввода нечислового значения запрашивается повторный ввод.
     */
    @Test
    void fillRetriesAfterNonNumericField() {
        TestSupport.run("abc\n150\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            CarArrayList cars = new ManualFiller(input, validator).fill(1);
            assertEquals(1, cars.size());
            assertEquals(150, cars.get(0).getPower());
            input.close();
        });
    }

    /**
     * Проверяет, что после неудачной валидации введённых данных запрашивается повторный ввод.
     */
    @Test
    void fillRetriesAfterValidationFailure() {
        TestSupport.run("0\nToyota\n2015\n150\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            CarArrayList cars = new ManualFiller(input, validator).fill(1);
            assertEquals(1, cars.size());
            assertEquals(150, cars.get(0).getPower());
            input.close();
        });
    }

    /**
     * Проверяет, что пустая модель отклоняется и ввод повторяется.
     */
    @Test
    void fillRetriesAfterEmptyModel() {
        TestSupport.run("150\n\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            CarArrayList cars = new ManualFiller(input, validator).fill(1);
            assertEquals("Toyota", cars.get(0).getModel());
            input.close();
        });
    }

    /**
     * Проверяет, что модель длиннее {@link Limits#MAX_MODEL_LENGTH} отклоняется
     * и запрашивается повторный ввод модели.
     */
    @Test
    void fillRetriesAfterTooLongModel() {
        String tooLong = "A".repeat(Limits.MAX_MODEL_LENGTH + 1);
        TestSupport.run("150\n" + tooLong + "\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            CarArrayList cars = new ManualFiller(input, validator).fill(1);
            assertEquals(1, cars.size());
            assertEquals("Toyota", cars.get(0).getModel());
            input.close();
        });
    }

    /**
     * Проверяет, что при окончании потока ввода бросается EndOfInputException.
     */
    @Test
    void fillFailsOnEndOfInput() {
        TestSupport.run("150\n", () -> {
            ConsoleInput input = new ConsoleInput();
            ManualFiller filler = new ManualFiller(input, validator);
            assertThrows(EndOfInputException.class, () -> filler.fill(1));
            input.close();
        });
    }

    /**
     * Проверяет, что конструктор отклоняет null-зависимости (ввод и валидатор).
     */
    @Test
    void constructorRejectsNullDependencies() {
        assertThrows(NullPointerException.class, () -> new ManualFiller(null, validator));
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(NullPointerException.class, () -> new ManualFiller(input, null));
            input.close();
        });
    }

    /**
     * Проверяет, что после успешного ввода автомобиля счётчик ошибок
     * сбрасывается перед вводом следующего автомобиля.
     */
    @Test
    void successfulCarResetsFailedAttemptsBeforeNextCar() {
        String inputData =
                "ошибка\nошибка\nошибка\nошибка\n" +
                        "150\nToyota\n2015\n" +
                        "ошибка\nошибка\nошибка\nошибка\n" +
                        "100\nAudi\n2020\n";

        TestSupport.Outcome outcome = TestSupport.run(
                inputData,
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        CarArrayList cars =
                                new ManualFiller(input, validator).fill(2);

                        assertEquals(2, cars.size());
                        assertEquals("Toyota", cars.get(0).getModel());
                        assertEquals("Audi", cars.get(1).getModel());
                    }
                }
        );

        assertNull(outcome.error());
    }

    /**
     * Проверяет, что пять ошибок при вводе одного автомобиля
     * прерывают заполнение коллекции.
     */
    @Test
    void fiveErrorsWithinOneCarThrowAttemptsExceededException() {
        TestSupport.Outcome outcome = TestSupport.run(
                "ошибка\nошибка\nошибка\nошибка\nошибка\n",
                () -> {
                    try (ConsoleInput input = new ConsoleInput()) {
                        new ManualFiller(input, validator).fill(1);
                    }
                }
        );

        assertInstanceOf(
                AttemptsExceededException.class,
                outcome.error()
        );
    }
}
