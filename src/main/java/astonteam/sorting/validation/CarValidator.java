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
        if (power < 0) {
            return ValidationResult.error("мощность не может быть отрицательной");
        }
        if (power < Limits.MIN_POWER || power > Limits.MAX_POWER) {
            return ValidationResult.error("мощность вне диапазона " + Limits.MIN_POWER + ".." + Limits.MAX_POWER);
        }
        if (model == null || model.isBlank()) {
            return ValidationResult.error("модель не должна быть пустой");
        }
        if (model.length() > Limits.MAX_MODEL_LENGTH) {
            return ValidationResult.error("Модель длиннее " + Limits.MAX_MODEL_LENGTH + " символов");
        }
        if (model.indexOf(MODEL_DELIMITER) >= 0) {
            return ValidationResult.error("модель не должна содержать символ «;» - он используется как разделитель в файле");
        }
        if (TextSanitizer.hasControlChars(model)) {
            return ValidationResult.error("модель содержит управляющие символы");
        }
        if (year < Limits.MIN_YEAR || year > Limits.MAX_YEAR) {
            return ValidationResult.error("год вне диапазона " + Limits.MIN_YEAR + ".." + Limits.MAX_YEAR);
        }
        return ValidationResult.ok();
    }
}
