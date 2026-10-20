package astonteam.sorting.validation;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

/**
 * Валидатор полей автомобиля.
 */
public final class CarValidator {

    /** Символ-разделитель полей автомобиля в файле: он не может входить в модель. */
    private static final char MODEL_DELIMITER = ';';

    /**
     * Создаёт валидатор автомобилей.
     */
    public CarValidator() {
    }

    /**
     * Проверяет мощность, модель и год автомобиля.
     *
     * @param power мощность двигателя
     * @param model название модели
     * @param year  год выпуска
     * @return результат валидации
     */
    public ValidationResult validate(int power, String model, int year) {
        ValidationResult result = validatePower(power);
        if (!result.isValid()) {
            return result;
        }
        result = validateModel(model);
        if (!result.isValid()) {
            return result;
        }
        return validateYear(year);
    }

    /**
     * Проверяет мощность автомобиля.
     *
     * @param power мощность двигателя
     * @return результат проверки мощности
     */
    public ValidationResult validatePower(int power) {
        if (power < 0) {
            return ValidationResult.error("Мощность не может быть отрицательной.");
        }
        if (power < Limits.MIN_POWER || power > Limits.MAX_POWER) {
            return ValidationResult.error(
                    "Мощность должна быть в диапазоне от "
                            + Limits.MIN_POWER
                            + " до "
                            + Limits.MAX_POWER
                            + "."
            );
        }
        return ValidationResult.ok();
    }

    /**
     * Проверяет название модели автомобиля.
     *
     * @param model название модели
     * @return результат проверки модели
     */
    public ValidationResult validateModel(String model) {
        if (model == null || model.isBlank()) {
            return ValidationResult.error("Модель не должна быть пустой.");
        }

        if (model.length() > Limits.MAX_MODEL_LENGTH) {
            return ValidationResult.error(
                    "Модель не должна быть длиннее "
                            + Limits.MAX_MODEL_LENGTH
                            + " символов."
            );
        }
        if (model.indexOf(MODEL_DELIMITER) >= 0) {
            return ValidationResult.error("Модель не должна содержать символ «;».");
        }
        if (TextSanitizer.hasControlChars(model)) {
            return ValidationResult.error("Модель не должна содержать управляющие символы.");
        }
        return ValidationResult.ok();
    }

    /**
     * Проверяет год выпуска автомобиля.
     *
     * @param year год выпуска
     * @return результат проверки года
     */
    public ValidationResult validateYear(int year) {
        if (year < Limits.MIN_YEAR || year > Limits.MAX_YEAR) {
            return ValidationResult.error(
                    "Год выпуска должен быть в диапазоне от "
                            + Limits.MIN_YEAR
                            + " до "
                            + Limits.MAX_YEAR
                            + "."
            );
        }

        return ValidationResult.ok();
    }
}
