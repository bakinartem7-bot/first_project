package astonteam.sorting.console;

/**
 * Исключение, возникающее при завершении потока ввода до чтения данных.
 */
public final class EndOfInputException extends RuntimeException {

    /** Идентификатор версии сериализации. */
    private static final long serialVersionUID = 1L;

    /** Создаёт исключение с сообщением о закрытии потока ввода. */
    public EndOfInputException() {
        super("Поток ввода закрыт");
    }
}
