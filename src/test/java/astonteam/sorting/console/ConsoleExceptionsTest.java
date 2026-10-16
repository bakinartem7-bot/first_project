package astonteam.sorting.console;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для консольных исключений {@link AttemptsExceededException} и {@link EndOfInputException}.
 */
class ConsoleExceptionsTest {

    /**
     * Проверяет, что сообщение AttemptsExceededException содержит количество попыток, а само исключение — RuntimeException.
     */
    @Test
    void attemptsExceededMessageMentionsCount() {
        AttemptsExceededException exception = new AttemptsExceededException(5);
        assertTrue(exception.getMessage().contains("5"));
        assertTrue(exception instanceof RuntimeException);
    }

    /**
     * Проверяет, что EndOfInputException имеет ожидаемое сообщение и является RuntimeException.
     */
    @Test
    void endOfInputHasMessage() {
        EndOfInputException exception = new EndOfInputException();
        assertEquals("Поток ввода закрыт", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }
}
