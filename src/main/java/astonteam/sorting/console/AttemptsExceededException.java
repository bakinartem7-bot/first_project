package astonteam.sorting.console;

public final class AttemptsExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AttemptsExceededException(int attempts) {
        super("Превышено число попыток ввода (" + attempts + "). Возврат в меню.");
    }
}
