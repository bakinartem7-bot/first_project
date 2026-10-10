package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import astonteam.sorting.common.Limits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleInputTest {

    @Test
    void readLineNormalizesAndTrims() {
        TestSupport.Outcome outcome = TestSupport.run("  hello world \n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals("hello world", input.readLine("> "));
            input.close();
        });
        assertNull(outcome.error());
    }

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

    @Test
    void readNonBlankSkipsEmptyLines() {
        TestSupport.run("\n   \nvalue\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals("value", input.readNonBlank("> "));
            input.close();
        });
    }

    @Test
    void readIntParsesValidNumber() {
        TestSupport.run("42\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(42, input.readInt("> "));
            input.close();
        });
    }

    @Test
    void readIntRejectsNonNumberThenAcceptsNext() {
        TestSupport.Outcome outcome = TestSupport.run("abc\n7\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(7, input.readInt("> "));
            input.close();
        });
        assertTrue(outcome.output().contains("не является целым числом"));
    }

    @Test
    void readIntRangeRejectsOutOfRange() {
        TestSupport.run("0\n11\n5\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(5, input.readInt("> ", 1, 10));
            input.close();
        });
    }

    @Test
    void readIntRangeRejectsInvalidBounds() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(IllegalArgumentException.class, () -> input.readInt("> ", 10, 1));
            input.close();
        });
    }

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

    @Test
    void readYesNoRejectsUnexpectedAnswer() {
        TestSupport.Outcome outcome = TestSupport.run("maybe\nнет\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertFalse(input.readYesNo("?"));
            input.close();
        });
        assertTrue(outcome.output().contains("Ожидается ответ"));
    }

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

    @Test
    void markSuccessResetsFailedCounter() {
        TestSupport.run("a\na\na\na\n7\n", () -> {
            ConsoleInput input = new ConsoleInput();
            assertEquals(7, input.readInt("> "));
            input.close();
        });
    }

    @Test
    void endOfInputThrowsEndOfInputException() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(EndOfInputException.class, () -> input.readLine("> "));
            input.close();
        });
    }
}
