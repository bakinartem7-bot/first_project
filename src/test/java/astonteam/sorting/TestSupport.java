package astonteam.sorting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Вспомогательный класс тестов: перенаправляет ввод/вывод консоли,
 * чтобы тесты могли эмулировать диалог с программой через System.in/System.out.
 */
public final class TestSupport {

    /**
     * Закрытый конструктор: класс содержит только статические методы
     * и не предназначен для создания экземпляров.
     */
    private TestSupport() {
    }

    /**
     * Результат выполнения тестируемого действия: перехваченный вывод консоли
     * и непойманное исключение (или {@code null}, если исключений не было).
     *
     * @param output текст, напечатанный в System.out во время выполнения
     * @param error  RuntimeException, выбросившийся при выполнении, либо {@code null}
     */
    public record Outcome(String output, RuntimeException error) {
    }

    /**
     * Запускает переданное действие, подменяя System.in указанным вводом
     * и перехватывая System.out; исходные потоки восстанавливаются после выполнения.
     *
     * @param input  текст, который будет доступен программе как стандартный ввод
     * @param action выполняемое действие (например, вызов {@code Main.main})
     * @return результат выполнения: захваченный вывод и возможное исключение
     */
    public static Outcome run(String input, Runnable action) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        RuntimeException error = null;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capture);
            action.run();
        } catch (RuntimeException e) {
            error = e;
        } finally {
            capture.flush();
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return new Outcome(buffer.toString(StandardCharsets.UTF_8), error);
    }
}
