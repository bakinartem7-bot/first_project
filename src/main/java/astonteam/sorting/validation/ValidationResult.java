package astonteam.sorting.validation;

/**
 * Результат валидации с флагом успеха и сообщением.
 *
 * @param valid   признак успешной валидации
 * @param message сообщение об ошибке
 */
public record ValidationResult(boolean valid, String message) {

    /**
     * Создаёт успешный результат валидации.
     *
     * @return результат без ошибки
     */
    public static ValidationResult ok() {
        return new ValidationResult(true, "");
    }

    /**
     * Создаёт результат с ошибкой.
     *
     * @param message сообщение об ошибке, либо {@code null} для значения по умолчанию
     * @return результат с ошибкой
     */
    public static ValidationResult error(String message) {
        return new ValidationResult(false, message == null ? "неизвестная ошибка" : message);
    }

    /**
     * Проверяет, прошла ли валидация успешно.
     *
     * @return {@code true}, если валидация успешна
     */
    public boolean isValid() {
        return valid;
    }
}
