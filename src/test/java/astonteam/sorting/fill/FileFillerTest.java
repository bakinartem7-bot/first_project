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
    //Тест на корректное чтение кириллицы (UTF-8)
    @Test
    void fillReadsUtf8CyrillicCorrectly() throws IOException {
        Path file = tempDir.resolve("utf8.txt");
        Files.writeString(file, "150;Лада;2015\n200;Нива;2020\n", StandardCharsets.UTF_8);

        CarArrayList cars = new FileFiller().fill(file);

        assertEquals(2, cars.size());
        assertEquals("Лада", cars.get(0).getModel());
        assertEquals("Нива", cars.get(1).getModel());
    }
    //Тест на неверный разделитель (вместо ';' используется ',')
    @Test
    void fillRejectsWrongDelimiter() throws IOException {
        Path file = tempDir.resolve("wrong_delimiter.txt");
        Files.writeString(file, "150,Toyota,2015\n");

        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }
    //Тест на проверку длины модели (превышение Limits.MAX_MODEL_LENGTH)
    @Test
    void fillRejectsTooLongModel() throws IOException {
        Path file = tempDir.resolve("long_model.txt");
        String longModel = "A".repeat(Limits.MAX_MODEL_LENGTH + 1);

        assertThrows(IllegalArgumentException.class, () -> new FileFiller().fill(file));
    }
}