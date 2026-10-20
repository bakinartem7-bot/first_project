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
        assertEquals("Мощность не может быть отрицательной.", result.message());
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
        assertEquals(
                "Мощность должна быть в диапазоне от 1 до 2000.",
                validator.validate(0, "A", 2000).message());
        assertEquals(
                "Модель не должна быть пустой.",
                validator.validate(1, "", 2000).message());
        assertEquals(
                "Год выпуска должен быть в диапазоне от 1886 до 2026.",
                validator.validate(1, "A", 3000).message());
    }

    /**
     * Проверяет, что отдельная проверка мощности принимает
     * минимальное и максимальное допустимые значения.
     */
    @Test
    void validatePowerAcceptsBoundaryValues() {
        assertTrue(validator.validatePower(Limits.MIN_POWER).isValid());
        assertTrue(validator.validatePower(Limits.MAX_POWER).isValid());
    }

    /**
     * Проверяет, что отдельная проверка мощности отклоняет
     * отрицательные значения и значения за границами диапазона.
     */
    @Test
    void validatePowerRejectsInvalidValues() {
        ValidationResult negative = validator.validatePower(-1);
        ValidationResult belowMinimum = validator.validatePower(Limits.MIN_POWER - 1);
        ValidationResult aboveMaximum = validator.validatePower(Limits.MAX_POWER + 1);
        assertFalse(negative.isValid());
        assertFalse(belowMinimum.isValid());
        assertFalse(aboveMaximum.isValid());
        assertEquals("Мощность не может быть отрицательной.", negative.message());
        assertEquals(
                "Мощность должна быть в диапазоне от "
                        + Limits.MIN_POWER
                        + " до "
                        + Limits.MAX_POWER
                        + ".",
                belowMinimum.message()
        );
        assertEquals(
                "Мощность должна быть в диапазоне от "
                        + Limits.MIN_POWER
                        + " до "
                        + Limits.MAX_POWER
                        + ".",
                aboveMaximum.message()
        );
    }

    /**
     * Проверяет, что отдельная проверка модели принимает
     * корректное название.
     */
    @Test
    void validateModelAcceptsValidValue() {
        ValidationResult result = validator.validateModel("Toyota Camry");
        assertTrue(result.isValid());
    }

    /**
     * Проверяет, что отдельная проверка модели отклоняет
     * null, пустое значение, слишком длинную строку,
     * разделитель файла и управляющие символы.
     */
    @Test
    void validateModelRejectsInvalidValues() {
        ValidationResult nullModel = validator.validateModel(null);
        ValidationResult blankModel = validator.validateModel("   ");
        ValidationResult longModel = validator.validateModel("A".repeat(Limits.MAX_MODEL_LENGTH + 1));
        ValidationResult delimiterModel = validator.validateModel("Toyota;Camry");
        ValidationResult controlCharacterModel = validator.validateModel("Toyota\nCamry");
        assertFalse(nullModel.isValid());
        assertFalse(blankModel.isValid());
        assertFalse(longModel.isValid());
        assertFalse(delimiterModel.isValid());
        assertFalse(controlCharacterModel.isValid());
        assertEquals("Модель не должна быть пустой.", nullModel.message());
        assertEquals("Модель не должна быть пустой.", blankModel.message());
        assertEquals(
                "Модель не должна быть длиннее "
                        + Limits.MAX_MODEL_LENGTH
                        + " символов.",
                longModel.message()
        );
        assertEquals("Модель не должна содержать символ «;».", delimiterModel.message());
        assertEquals( "Модель не должна содержать управляющие символы.", controlCharacterModel.message());
    }

    /**
     * Проверяет, что отдельная проверка года принимает
     * минимальный и максимальный допустимые годы.
     */
    @Test
    void validateYearAcceptsBoundaryValues() {
        assertTrue(validator.validateYear(Limits.MIN_YEAR).isValid());
        assertTrue(validator.validateYear(Limits.MAX_YEAR).isValid());
    }

    /**
     * Проверяет, что отдельная проверка года отклоняет
     * значения за границами допустимого диапазона.
     */
    @Test
    void validateYearRejectsValuesOutsideRange() {
        ValidationResult belowMinimum = validator.validateYear(Limits.MIN_YEAR - 1);
        ValidationResult aboveMaximum = validator.validateYear(Limits.MAX_YEAR + 1);
        assertFalse(belowMinimum.isValid());
        assertFalse(aboveMaximum.isValid());
        String expectedMessage =
                "Год выпуска должен быть в диапазоне от "
                        + Limits.MIN_YEAR
                        + " до "
                        + Limits.MAX_YEAR
                        + ".";
        assertEquals(expectedMessage, belowMinimum.message());
        assertEquals(expectedMessage, aboveMaximum.message());
    }
}
