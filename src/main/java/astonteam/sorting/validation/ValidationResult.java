package astonteam.sorting.validation;

public record ValidationResult(boolean valid, String message) {

    public static ValidationResult ok() {
        return new ValidationResult(true, "");
    }

    public static ValidationResult error(String message) {
        return new ValidationResult(false, message == null ? "неизвестная ошибка" : message);
    }

    public boolean isValid() {
        return valid;
    }
}
