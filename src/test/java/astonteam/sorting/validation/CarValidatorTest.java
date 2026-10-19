package astonteam.sorting.validation;

import astonteam.sorting.common.Limits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link CarValidator}: проверка корректности валидации
 * характеристик автомобиля (мощность, модель, год).
 */
class CarValidatorTest {

    /**
     * Экземпляр валидатора, используемый во всех тестах.
     */
    private final CarValidator validator = new CarValidator();

    /**
     * Проверяет, что корректный автомобиль проходит валидацию.
     */
    @Test
    void acceptsValidCar() {
        assertTrue(validator.validate(150, "Toyota", 2015).isValid());
    }

    /**
     * Проверяет, что граничные допустимые значения (1..2000 л.с., 1886..2026)
     * принимаются валидатором.
     */
    @Test
    void acceptsBoundaryValues() {
        assertTrue(validator.validate(1, "A", 1886).isValid());
        assertTrue(validator.validate(2000, "A", 2026).isValid());
    }

    /**
     * Проверяет, что отрицательная мощность отклоняется с соответствующим
     * сообщением об ошибке.
     */
    @Test
    void rejectsNegativePower() {
        ValidationResult result = validator.validate(-1, "Toyota", 2015);
        assertFalse(result.isValid());
        assertEquals("мощность не может быть отрицательной", result.message());
    }

    /**
     * Проверяет, что мощность вне диапазона 1..2000 отклоняется валидатором.
     */
    @Test
    void rejectsPowerOutOfRange() {
        assertFalse(validator.validate(0, "Toyota", 2015).isValid());
        assertFalse(validator.validate(2001, "Toyota", 2015).isValid());
    }

    /**
     * Проверяет, что год вне диапазона 1886..2026 отклоняется валидатором.
     */
    @Test
    void rejectsYearOutOfRange() {
        assertFalse(validator.validate(150, "Toyota", 1885).isValid());
        assertFalse(validator.validate(150, "Toyota", 2027).isValid());
    }

    /**
     * Проверяет, что {@code null} или пустая/состоящая из пробелов модель
     * отклоняется валидатором.
     */
    @Test
    void rejectsBlankOrNullModel() {
        assertFalse(validator.validate(150, null, 2015).isValid());
        assertFalse(validator.validate(150, "   ", 2015).isValid());
    }

    /**
     * Проверяет, что сообщения об ошибках точно описывают причину
     * каждого нарушения правил валидации.
     */
    @Test
    void errorMessagesDescribeProblem() {
        assertEquals("мощность вне диапазона 1..2000", validator.validate(0, "A", 2000).message());
        assertEquals("модель не должна быть пустой", validator.validate(1, "", 2000).message());
        assertEquals("год вне диапазона 1886..2026", validator.validate(1, "A", 3000).message());
    }

    /**
     * Проверяет, что модель длиннее {@link Limits#MAX_MODEL_LENGTH} символов
     * отклоняется с понятным сообщением.
     */
    @Test
    void rejectsTooLongModel() {
        String tooLong = "A".repeat(Limits.MAX_MODEL_LENGTH + 1);

        ValidationResult result = validator.validate(150, tooLong, 2015);

        assertFalse(result.isValid());
        assertTrue(result.message().contains("длиннее " + Limits.MAX_MODEL_LENGTH));
    }
}
