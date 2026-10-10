package astonteam.sorting.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarValidatorTest {

    private final CarValidator validator = new CarValidator();

    @Test
    void acceptsValidCar() {
        assertTrue(validator.validate(150, "Toyota", 2015).isValid());
    }

    @Test
    void acceptsBoundaryValues() {
        assertTrue(validator.validate(1, "A", 1886).isValid());
        assertTrue(validator.validate(2000, "A", 2026).isValid());
    }

    @Test
    void rejectsNegativePower() {
        ValidationResult result = validator.validate(-1, "Toyota", 2015);
        assertFalse(result.isValid());
        assertEquals("мощность не может быть отрицательной", result.message());
    }

    @Test
    void rejectsPowerOutOfRange() {
        assertFalse(validator.validate(0, "Toyota", 2015).isValid());
        assertFalse(validator.validate(2001, "Toyota", 2015).isValid());
    }

    @Test
    void rejectsYearOutOfRange() {
        assertFalse(validator.validate(150, "Toyota", 1885).isValid());
        assertFalse(validator.validate(150, "Toyota", 2027).isValid());
    }

    @Test
    void rejectsBlankOrNullModel() {
        assertFalse(validator.validate(150, null, 2015).isValid());
        assertFalse(validator.validate(150, "   ", 2015).isValid());
    }

    @Test
    void errorMessagesDescribeProblem() {
        assertEquals("мощность вне диапазона 1..2000", validator.validate(0, "A", 2000).message());
        assertEquals("модель не должна быть пустой", validator.validate(1, "", 2000).message());
        assertEquals("год вне диапазона 1886..2026", validator.validate(1, "A", 3000).message());
    }
}
