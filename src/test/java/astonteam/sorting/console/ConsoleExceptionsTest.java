package astonteam.sorting.console;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleExceptionsTest {

    @Test
    void attemptsExceededMessageMentionsCount() {
        AttemptsExceededException exception = new AttemptsExceededException(5);
        assertTrue(exception.getMessage().contains("5"));
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void endOfInputHasMessage() {
        EndOfInputException exception = new EndOfInputException();
        assertEquals("Поток ввода закрыт", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }
}
