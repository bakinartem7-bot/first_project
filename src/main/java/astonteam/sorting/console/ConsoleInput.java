package astonteam.sorting.console;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Чтение пользовательского ввода из консоли с проверкой длины, формата и лимита ошибок.
 * Реализует {@link AutoCloseable} для освобождения потока чтения.
 */
public final class ConsoleInput implements AutoCloseable {

    /** Поток чтения символов из стандартного ввода. */
    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    /** Счётчик подряд идущих неудачных попыток ввода. */
    private int failedAttempts;

    /**
     * Читает строку из консоли, отклоняя строки длиннее допустимого предела.
     *
     * @param prompt приглашение к вводу
     * @return нормализованная (без краевых пробелов) строка
     * @throws AttemptsExceededException если превышено допустимое число ошибок ввода
     * @throws EndOfInputException       если поток ввода завершён
     */
    public String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            Line line = readBoundedLine();
            if (line.truncated()) {
                reportError("Строка длиннее " + Limits.MAX_LINE_LENGTH + " символов.");
                continue;
            }
            return TextSanitizer.normalize(line.value());
        }
    }

    /**
     * Читает строку, повторяя запрос до тех пор, пока ввод не окажется непустым.
     *
     * @param prompt приглашение к вводу
     * @return непустая нормализованная строка
     * @throws AttemptsExceededException если превышено допустимое число ошибок ввода
     * @throws EndOfInputException       если поток ввода завершён
     */
    public String readNonBlank(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                markSuccess();
                return line;
            }
            reportError("Строка не должна быть пустой.");
        }
    }

    /**
     * Читает целое число из консоли, повторяя запрос при некорректном вводе.
     *
     * @param prompt приглашение к вводу
     * @return введённое целое число
     * @throws AttemptsExceededException если превышено допустимое число ошибок ввода
     * @throws EndOfInputException       если поток ввода завершён
     */
    public int readInt(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (line.isEmpty()) {
                reportError("Пустой ввод.");
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                reportError("\"" + line + "\" не является целым числом.");
            }
        }
    }

    /**
     * Читает целое число из консоли, гарантируя попадание в заданный диапазон.
     *
     * @param prompt приглашение к вводу
     * @param min    нижняя граница диапазона (включительно)
     * @param max    верхняя граница диапазона (включительно)
     * @return введённое число из диапазона [min, max]
     * @throws IllegalArgumentException если {@code min} больше {@code max}
     * @throws AttemptsExceededException если превышено допустимое число ошибок ввода
     * @throws EndOfInputException       если поток ввода завершён
     */
    public int readInt(String prompt, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min больше max: " + min + " > " + max);
        }
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                markSuccess();
                return value;
            }
            reportError("Число должно быть в диапазоне от " + min + " до " + max + ".");
        }
    }

    /**
     * Запрашивает положительный или отрицательный ответ («да»/«нет»).
     *
     * @param prompt приглашение к вводу
     * @return {@code true} для утвердительного ответа, {@code false} для отрицательного
     * @throws AttemptsExceededException если превышено допустимое число ошибок ввода
     * @throws EndOfInputException       если поток ввода завершён
     */
    public boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt + " (да/нет): ").toLowerCase(Locale.ROOT);
            switch (line) {
                case "да", "д", "y", "yes" -> {
                    markSuccess();
                    return true;
                }
                case "нет", "н", "n", "no" -> {
                    markSuccess();
                    return false;
                }
                default -> reportError("Ожидается ответ \"да\" или \"нет\".");
            }
        }
    }

    /**
     * Выводит сообщение об ошибке ввода и увеличивает счётчик неудачных попыток.
     *
     * @param message текст сообщения об ошибке
     * @throws AttemptsExceededException если после этого сообщения достигнут предел попыток
     */
    public void reportError(String message) {
        System.out.println(message);
        failedAttempts++;
        if (failedAttempts >= Limits.MAX_ATTEMPTS) {
            failedAttempts = 0;
            throw new AttemptsExceededException(Limits.MAX_ATTEMPTS);
        }
    }

    /** Сбрасывает счётчик неудачных попыток после успешного ввода. */
    public void markSuccess() {
        failedAttempts = 0;
    }

    /** Закрывает поток чтения из консоли; ошибки закрытия игнорируются. */
    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            failedAttempts = 0;
        }
    }

    /**
     * Считывает одну строку, ограниченную по длине, без блокирующего ожидания завершения потока.
     *
     * @return прочитанная строка вместе с признаком превышения максимальной длины
     * @throws EndOfInputException       если поток ввода завершён на пустой строке
     * @throws UncheckedIOException      при ошибке чтения из консоли
     */
    private Line readBoundedLine() {
        StringBuilder builder = new StringBuilder();
        boolean anyChars = false;
        boolean truncated = false;
        try {
            int ch;
            while ((ch = reader.read()) != -1) {
                if (ch == '\n') {
                    break;
                }
                if (ch == '\r') {
                    continue;
                }
                anyChars = true;
                if (builder.length() < Limits.MAX_LINE_LENGTH) {
                    builder.append((char) ch);
                } else {
                    truncated = true;
                }
            }
            if (ch == -1 && !anyChars) {
                throw new EndOfInputException();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения из консоли", e);
        }
        return new Line(builder.toString(), truncated);
    }

    /**
     * Результат чтения строки: её содержимое и признак обрезания по максимальной длине.
     *
     * @param value     прочитанные символы
     * @param truncated {@code true}, если строка превысила допустимую длину
     */
    private record Line(String value, boolean truncated) {
    }
}
