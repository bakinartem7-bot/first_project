package astonteam.sorting.console;

/**
 * Исключение, возникающее при превышении допустимого числа неудачных попыток ввода.
 */
public final class AttemptsExceededException extends RuntimeException {

    /** Идентификатор версии сериализации. */
    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с сообщением о превышении числа попыток.
     *
     * @param attempts допустимое число попыток ввода
     */
    public AttemptsExceededException(int attempts) {
        super("Превышено число попыток ввода (" + attempts + ").");
    }
}
