package astonteam.sorting.io;

import astonteam.sorting.common.Limits;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link ResultSaver}: сохранение коллекции автомобилей и отчёта о сортировке в файлы.
 */
class ResultSaverTest {

    /**
     * Временный каталог, в который сохраняются файлы в ходе тестов.
     */
    @TempDir
    Path directory;

    /**
     * Создаёт тестовый автомобиль с указанными параметрами.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year год выпуска автомобиля
     * @return построенный объект {@link Car}
     */
    private static Car car(int power, String model, int year) {
        return Car.builder().power(power).model(model).year(year).build();
    }

    /**
     * Проверяет, что сохранение одной машины записывает UTF-8 строки возвращённым файлом.
     */
    @Test
    void saveCollectionWritesUtf8Lines() throws IOException {
        ResultSaver saver = new ResultSaver(directory);
        Path file = saver.saveCollection("cars.txt", List.of(car(150, "Toyota", 2015)));
        assertEquals(directory.resolve("cars.txt"), file);
        String content = Files.readString(file, StandardCharsets.UTF_8);
        assertEquals("150;Toyota;2015" + System.lineSeparator(), content);
    }

    /**
     * Проверяет, что несколько машин записываются в файл построчно в исходном порядке.
     */
    @Test
    void saveCollectionWritesMultipleLines() throws IOException {
        ResultSaver saver = new ResultSaver(directory);
        Path file = saver.saveCollection("cars.txt",
                List.of(car(150, "Toyota", 2015), car(90, "BMW", 2010)));
        String content = Files.readString(file, StandardCharsets.UTF_8);
        assertEquals("150;Toyota;2015" + System.lineSeparator()
                + "90;BMW;2010" + System.lineSeparator(), content);
    }

    /**
     * Проверяет, что сохранение коллекции с null-элементом приводит к IllegalArgumentException.
     */
    @Test
    void saveCollectionRejectsNullElement() {
        ResultSaver saver = new ResultSaver(directory);
        List<Car> cars = new ArrayList<>();
        cars.add(null);
        assertThrows(IllegalArgumentException.class, () -> saver.saveCollection("cars.txt", cars));
    }

    /**
     * Проверяет, что модель с управляющими символами отклоняется при сохранении.
     */
    @Test
    void saveCollectionRejectsControlCharsInModel() {
        ResultSaver saver = new ResultSaver(directory);
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection("cars.txt", List.of(car(150, "A\tB", 2015))));
    }

    /**
     * Проверяет, что при неудачном сохранении файл не создаётся.
     */
    @Test
    void failedSaveDoesNotCreateFile() {
        ResultSaver saver = new ResultSaver(directory);
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection("cars.txt", List.of(car(150, "A;B", 2015))));
        assertFalse(Files.exists(directory.resolve("cars.txt")));
    }

    /**
     * Проверяет, что при неудачном сохранении содержимое ранее записанного файла не меняется.
     */
    @Test
    void failedSaveKeepsPreviousFileContent() throws IOException {
        ResultSaver saver = new ResultSaver(directory);
        Path file = saver.saveCollection("cars.txt", List.of(car(150, "Toyota", 2015)));
        String before = Files.readString(file, StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection("cars.txt", List.of(car(150, "A;B", 2015))));
        assertEquals(before, Files.readString(file, StandardCharsets.UTF_8));
    }

    /**
     * Проверяет, что пустое имя файла отклоняется при сохранении.
     */
    @Test
    void saveCollectionRejectsBlankFileName() {
        ResultSaver saver = new ResultSaver(directory);
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection("   ", List.of(car(150, "Toyota", 2015))));
    }

    /**
     * Проверяет, что небезопасные имена файлов (с разделителями пути и выходом наружу) отклоняются.
     */
    @Test
    void saveCollectionRejectsUnsafeFileName() {
        ResultSaver saver = new ResultSaver(directory);
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection("bad/name.txt", List.of(car(150, "Toyota", 2015))));
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection("../escape.txt", List.of(car(150, "Toyota", 2015))));
    }

    /**
     * Проверяет, что имя файла длиннее допустимого лимита отклоняется.
     */
    @Test
    void saveCollectionRejectsTooLongFileName() {
        ResultSaver saver = new ResultSaver(directory);
        String name = "a".repeat(Limits.MAX_FILE_NAME_LENGTH + 1) + ".txt";
        assertThrows(IllegalArgumentException.class,
                () -> saver.saveCollection(name, List.of(car(150, "Toyota", 2015))));
    }

    /**
     * Проверяет, что отчёт сохраняется в файл и содержит сведения об алгоритме, машинах и заголовке.
     */
    @Test
    void saveReportWritesReportFile() throws IOException {
        ResultSaver saver = new ResultSaver(directory);
        SortingStatistics statistics =
                new SortingStatistics("Bubble", "power", 1, 0, 0, Duration.ofMillis(1));
        Path file = saver.saveReport(
                List.of(car(150, "Toyota", 2015)),
                List.of(car(150, "Toyota", 2015)),
                statistics);
        assertTrue(Files.exists(file));
        String content = Files.readString(file, StandardCharsets.UTF_8);
        assertTrue(content.contains("Bubble"));
        assertTrue(content.contains("Toyota"));
        assertTrue(content.contains("Отчёт о сортировке"));
    }

    /**
     * Проверяет, что сохранение отчёта с null-статистикой приводит к NullPointerException.
     */
    @Test
    void saveReportRejectsNullStatistics() {
        ResultSaver saver = new ResultSaver(directory);
        assertThrows(NullPointerException.class,
                () -> saver.saveReport(List.of(), List.of(), null));
    }

    /**
     * Проверяет, что метод appendCollection дописывает данные, не стирая предыдущие.
     */
    @Test
    void appendCollectionAddsToEndWithoutOverwriting() throws IOException {
        ResultSaver saver = new ResultSaver(directory);
        Path file = directory.resolve("history.txt");

        // Первая запись
        saver.appendCollection("history.txt", List.of(car(150, "Toyota", 2015)), "Первая запись");

        // Вторая запись (дописывание)
        saver.appendCollection("history.txt", List.of(car(90, "BMW", 2010)), "Вторая запись");

        String content = Files.readString(file, StandardCharsets.UTF_8);

        // Проверяем, что обе записи присутствуют в файле
        assertTrue(content.contains("Toyota"));
        assertTrue(content.contains("BMW"));
        assertTrue(content.contains("Первая запись"));
        assertTrue(content.contains("Вторая запись"));
    }
}
