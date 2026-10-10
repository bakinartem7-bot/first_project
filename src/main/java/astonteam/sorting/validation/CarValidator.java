package astonteam.sorting.validation;

public final class CarValidator {

    private static final int MIN_POWER = 1;
    private static final int MAX_POWER = 2000;
    private static final int MIN_YEAR = 1886;
    private static final int MAX_YEAR = 2026;

    public ValidationResult validate(int power, String model, int year) {
        if (power < 0) {
            return ValidationResult.error("мощность не может быть отрицательной");
        }
        if (power < MIN_POWER || power > MAX_POWER) {
            return ValidationResult.error("мощность вне диапазона " + MIN_POWER + ".." + MAX_POWER);
        }
        if (model == null || model.isBlank()) {
            return ValidationResult.error("модель не должна быть пустой");
        }
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return ValidationResult.error("год вне диапазона " + MIN_YEAR + ".." + MAX_YEAR);
        }
        return ValidationResult.ok();
    }
}
