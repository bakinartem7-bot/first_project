package astonteam.sorting.console;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class ConsoleInput implements AutoCloseable {

    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    private int failedAttempts;

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

    public void reportError(String message) {
        System.out.println(message);
        failedAttempts++;
        if (failedAttempts >= Limits.MAX_ATTEMPTS) {
            failedAttempts = 0;
            throw new AttemptsExceededException(Limits.MAX_ATTEMPTS);
        }
    }

    public void markSuccess() {
        failedAttempts = 0;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            failedAttempts = 0;
        }
    }

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

    private record Line(String value, boolean truncated) {
    }
}
