package astonteam.sorting.validation;

import  astonteam.sorting.common.Limits;

public final class CarValidator {


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
        if (year < Limits.MIN_YEAR || year > Limits.MAX_YEAR) {
            return ValidationResult.error("год вне диапазона " + Limits.MIN_YEAR + ".." + Limits.MAX_YEAR);
        }
        return ValidationResult.ok();
    }
}
