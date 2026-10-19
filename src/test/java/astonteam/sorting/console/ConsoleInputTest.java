package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.common.Limits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link ConsoleInput}: чтение и нормализация строк, чисел, ответов да/нет и работа с ошибками.
 */
class ConsoleInputTest {

    /**
     * Проверяет, что прочитанная строка нормализуется и обрезается по краям.
     */
    @Test
    void readLineNormalizesAndTrims() {
        TestSupport.Outcome outcome = TestSupport.run("  hello world \n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals("hello world", input.readLine("> "));
            input.close();
        });
        assertNull(outcome.error());
    }

    /**
     * Проверяет, что слишком длинная строка отклоняется с сообщением и принимается следующая.
     */
    @Test
    void readLineReportsTooLongLineThenAcceptsNext() {
        String tooLong = "a".repeat(Limits.MAX_LINE_LENGTH + 10);
        TestSupport.Outcome outcome = TestSupport.run(tooLong + "\nok\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals("ok", input.readLine("> "));
            input.close();
        });
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Строка длиннее"));
    }

    /**
     * Проверяет, что чтение непустой строки пропускает пустые и пробельные строки.
     */
    @Test
    void readNonBlankSkipsEmptyLines() {
        TestSupport.run("\n   \nvalue\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals("value", input.readNonBlank("> "));
            input.close();
        });
    }

    /**
     * Проверяет, что корректное число разбирается из ввода.
     */
    @Test
    void readIntParsesValidNumber() {
        TestSupport.run("42\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(42, input.readInt("> "));
            input.close();
        });
    }

    /**
     * Проверяет, что нечисловое значение отклоняется с сообщением и принимается следующий ввод.
     */
    @Test
    void readIntRejectsNonNumberThenAcceptsNext() {
        TestSupport.Outcome outcome = TestSupport.run("abc\n7\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(7, input.readInt("> "));
            input.close();
        });
        assertTrue(outcome.output().contains("не является целым числом"));
    }

    /**
     * Проверяет, что число вне заданного диапазона отклоняется и принимается корректное значение.
     */
    @Test
    void readIntRangeRejectsOutOfRange() {
        TestSupport.run("0\n11\n5\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(5, input.readInt("> ", 1, 10));
            input.close();
        });
    }

    /**
     * Проверяет, что некорректные границы диапазона приводят к IllegalArgumentException.
     */
    @Test
    void readIntRangeRejectsInvalidBounds() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(IllegalArgumentException.class, () -> input.readInt("> ", 10, 1));
            input.close();
        });
    }

    /**
     * Проверяет, что ответы да/нет принимаются как на русском, так и на английском.
     */
    @Test
    void readYesNoAcceptsRussianAndEnglish() {
        TestSupport.run("да\nno\nd\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertTrue(input.readYesNo("?"));
            assertFalse(input.readYesNo("?"));
            assertTrue(input.readYesNo("?"));
            input.close();
        });
    }

    /**
     * Проверяет, что неожиданный ответ отклоняется с сообщением и принимается следующий.
     */
    @Test
    void readYesNoRejectsUnexpectedAnswer() {
        TestSupport.Outcome outcome = TestSupport.run("maybe\nнет\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertFalse(input.readYesNo("?"));
            input.close();
        });
        assertTrue(outcome.output().contains("Ожидается ответ"));
    }

    /**
     * Проверяет, что после превышения максимального числа попыток бросается AttemptsExceededException.
     */
    @Test
    void reportErrorThrowsAfterMaxAttempts() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(AttemptsExceededException.class, () -> {
                for (int i = 0; i < Limits.MAX_ATTEMPTS; i++) {
                    input.reportError("ошибка");
                }
            });
            input.close();
        });
    }

    /**
     * Проверяет, что успешный ввод сбрасывает счётчик неудачных попыток.
     */
    @Test
    void markSuccessResetsFailedCounter() {
        TestSupport.run("a\na\na\na\n7\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(7, input.readInt("> "));
            input.close();
        });
    }

    /**
     * Проверяет, что пустая строка при чтении числа отклоняется с сообщением
     * «Пустой ввод.» и запрашивается повторный ввод.
     */
    @Test
    void readIntRetriesOnEmptyInput() {
        TestSupport.Outcome outcome = TestSupport.run("\n5\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(5, input.readInt("> "));
            input.close();
        });
        assertTrue(outcome.output().contains("Пустой ввод."));
    }

    /**
     * Проверяет, что при окончании потока ввода бросается EndOfInputException.
     */
    @Test
    void endOfInputThrowsEndOfInputException() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(EndOfInputException.class, () -> input.readLine("> "));
            input.close();
        });
    }
}
