package astonteam.sorting.fill;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для {@link FileFiller}: чтение автомобилей из файла.
 */
class FileFillerTest {

    /**
     * Временный каталог для создания тестовых файлов.
     */
    @TempDir
    Path tempDir;

    /**
     * Проверяет, что корректный файл читается в коллекцию с правильными полями.
     */
    @Test
    void fillReadsValidFile() throws IOException {
        Path file = tempDir.resolve("valid.txt");
        Files.writeString(file, "150;Toyota;2015\n200;BMW;2020\n");

        CarArrayList cars = new FileFiller().fill(file);

        assertEquals(2, cars.size());
        assertEquals(150, cars.get(0).getPower());
        assertEquals("Toyota", cars.get(0).getModel());
        assertEquals(2020, cars.get(1).getYear());
    }

    /**
     * Проверяет, что пустые строки файла игнорируются при чтении.
     */
    @Test
    void fillIgnoresEmptyLines() throws IOException {
        Path file = tempDir.resolve("empty_lines.txt");
        Files.writeString(file, "150;Toyota;2015\n\n\n200;BMW;2020\n");

        CarArrayList cars = new FileFiller().fill(file);

        assertEquals(2, cars.size());
    }

    /**
     * Проверяет, что несуществующий файл приводит к IllegalArgumentException.
     */
    @Test
    void fillRejectsNonExistentFile() {
        Path file = tempDir.resolve("nonexistent.txt");
        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }

    /**
     * Проверяет, что файл с некорректной строкой отклоняется с IllegalArgumentException.
     */
    @Test
    void fillRejectsInvalidFileContent() throws IOException {
        Path file = tempDir.resolve("invalid.txt");
        Files.writeString(file, "150;Toyota;2015\ninvalid_line\n200;BMW;2020\n");

        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }

    /**
     * Проверяет, что null-путь приводит к NullPointerException.
     */
    @Test
    void fillRejectsNullPath() {
        assertThrows(NullPointerException.class, () -> new FileFiller().fill(null));
    }

    /**
     * Проверяет, что кириллические названия моделей из UTF-8 файла читаются в коллекцию без искажений.
     */
    @Test
    void fillReadsUtf8CyrillicCorrectly() throws IOException {
        Path file = tempDir.resolve("utf8.txt");
        Files.writeString(file, "150;Лада;2015\n200;Нива;2020\n", StandardCharsets.UTF_8);

        CarArrayList cars = new FileFiller().fill(file);

        assertEquals(2, cars.size());
        assertEquals("Лада", cars.get(0).getModel());
        assertEquals("Нива", cars.get(1).getModel());
    }

    /**
     * Проверяет, что строка с разделителем ',' вместо ';' отклоняется с IllegalArgumentException.
     */
    @Test
    void fillRejectsWrongDelimiter() throws IOException {
        Path file = tempDir.resolve("wrong_delimiter.txt");
        Files.writeString(file, "150,Toyota,2015\n");

        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }

    /**
     * Проверяет, что модель длиннее {@code Limits.MAX_MODEL_LENGTH} отклоняется с IllegalArgumentException.
     */
    @Test
    void fillRejectsTooLongModel() throws IOException {
        Path file = tempDir.resolve("long_model.txt");
        String longModel = "A".repeat(Limits.MAX_MODEL_LENGTH + 1);
        Files.writeString(file, "150;" + longModel + ";2015\n", StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }

    /**
     * Проверяет, что путь к каталогу вместо файла отклоняется с IllegalArgumentException.
     */
    @Test
    void fillRejectsDirectoryPath() {
        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(tempDir));
    }

    /**
     * Проверяет, что файл с числом строк больше {@link Limits#MAX_COLLECTION_SIZE}
     * отклоняется, иначе коллекция превысила бы допустимый размер.
     *
     * @throws IOException при подготовке входного файла
     */
    @Test
    void fillRejectsTooManyLines() throws IOException {
        Path file = tempDir.resolve("too_many.txt");
        StringBuilder content = new StringBuilder();
        for (int i = 0; i <= Limits.MAX_COLLECTION_SIZE; i++) {
            content.append("150;Toyota;2015\n");
        }
        Files.writeString(file, content.toString(), StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }
}