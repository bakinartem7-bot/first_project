package astonteam.sorting.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link ValidationResult}: проверка корректного создания
 * успешного и неуспешного результатов валидации.
 */
class ValidationResultTest {

    /**
     * Проверяет, что {@code ValidationResult.ok()} создаёт валидный результат
     * с пустым сообщением об ошибке.
     */
    @Test
    void okIsValidWithEmptyMessage() {
        ValidationResult result = ValidationResult.ok();
        assertTrue(result.isValid());
        assertTrue(result.valid());
        assertEquals("", result.message());
    }

    /**
     * Проверяет, что {@code ValidationResult.error(...)} создаёт невалидный
     * результат с переданным сообщением об ошибке.
     */
    @Test
    void errorIsInvalidWithMessage() {
        ValidationResult result = ValidationResult.error("плохо");
        assertFalse(result.isValid());
        assertEquals("плохо", result.message());
    }

    /**
     * Проверяет, что при передаче {@code null} в качестве сообщения
     * используется сообщение по умолчанию «неизвестная ошибка».
     */
    @Test
    void errorWithNullMessageUsesDefault() {
        assertEquals("неизвестная ошибка", ValidationResult.error(null).message());
    }
}
