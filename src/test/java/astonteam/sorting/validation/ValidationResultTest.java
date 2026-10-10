package astonteam.sorting.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationResultTest {

    @Test
    void okIsValidWithEmptyMessage() {
        ValidationResult result = ValidationResult.ok();
        assertTrue(result.isValid());
        assertTrue(result.valid());
        assertEquals("", result.message());
    }

    @Test
    void errorIsInvalidWithMessage() {
        ValidationResult result = ValidationResult.error("плохо");
        assertFalse(result.isValid());
        assertEquals("плохо", result.message());
    }

    @Test
    void errorWithNullMessageUsesDefault() {
        assertEquals("неизвестная ошибка", ValidationResult.error(null).message());
    }
}
