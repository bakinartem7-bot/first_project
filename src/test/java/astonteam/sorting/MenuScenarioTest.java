package astonteam.sorting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Сквозные (интеграционные) сценарии работы приложения через главное меню.
 *
 * <p>Каждый тест эмулирует полный диалог пользователя с программой через
 * {@link TestSupport#run(String, Runnable)} и {@link Main#main(String[])},
 * покрывая:
 * <ul>
 *     <li>положительные сценарии — все три источника заполнения (ручной,
 *     случайный, из файла), сортировку по трём полям (мощность, модель, год),
 *     запись результата в файл и корректный выход;</li>
 *     <li>отрицательные сценарии — неверный ввод (нечисловое значение, номер
 *     отсутствующего пункта меню, мощность вне диапазона, неожидаемый ответ
 *     да/нет, отсутствующий файл) не должен завершать программу.</li>
 * </ul>
 */
class MenuScenarioTest {

    /** Каталог результатов, используемый {@link astonteam.sorting.io.ResultSaver} по умолчанию. */
    private static final Path RESULTS = Path.of("results");
    /** Файл, в который пишет пункт меню «Сохранить коллекцию». */
    private static final Path CARS_FILE = RESULTS.resolve("cars.txt");
    /** Файл, в который дописываются результаты сортировки и поиска. */
    private static final Path HISTORY_FILE = RESULTS.resolve("history.txt");

    /** Временный каталог для входного файла сценария «заполнить из файла». */
    @TempDir
    Path tempDir;

    /**
     * Удаляет сгенерированные сценариями файлы, чтобы тесты не оставляли мусор,
     * и убирает каталог результатов, если он стал пустым.
     *
     * @throws IOException при ошибке работы с файловой системой
     */
    @AfterEach
    void cleanupGeneratedFiles() throws IOException {
        Files.deleteIfExists(CARS_FILE);
        Files.deleteIfExists(HISTORY_FILE);
        if (Files.isDirectory(RESULTS)) {
            try (var entries = Files.list(RESULTS)) {
                if (entries.findAny().isEmpty()) {
                    Files.delete(RESULTS);
                }
            }
        }
    }

    /**
     * Запускает {@link Main} с заданным пользовательским вводом.
     *
     * @param input строки, подаваемые программе в стандартный ввод
     * @return результат выполнения: перехваченный вывод и возможное исключение
     */
    private static TestSupport.Outcome runMain(String input) {
        return TestSupport.run(input, () -> Main.main(new String[0]));
    }

    /**
     * Соединяет строки ввода переводом строки, добавляя завершающий перевод.
     *
     * @param lines отдельные строки ввода
     * @return готовый текст стандартного ввода
     */
    private static String lines(String... lines) {
        return String.join("\n", lines) + "\n";
    }

    /**
     * Позитивный сценарий: ручное заполнение → сортировка по мощности →
     * сохранение в файл → выход.
     */
    @Test
    void manualSourceSortByPowerWriteAndExit() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "1", "2",
                "150", "Toyota", "2015",
                "100", "Audi", "2018",
                "2", "1", "1", "нет",
                "4",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей введено: 2"));
        assertTrue(outcome.output().contains("Сортировка завершена."));
        assertTrue(outcome.output().contains("Алгоритм: InsertionSort"));
        assertTrue(outcome.output().contains("Файл сохранён"));
        assertTrue(outcome.output().contains("Работа завершена."));
        assertTrue(Files.exists(CARS_FILE));
        assertTrue(
                outcome.output().indexOf("power=100") < outcome.output().indexOf("power=150"),
                "автомобили должны быть отсортированы по мощности по возрастанию"
        );
    }

    /**
     * Позитивный сценарий: случайное заполнение → сортировка по модели →
     * дописывание в history.txt → выход.
     */
    @Test
    void randomSourceSortByModelAppendAndExit() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "2", "3",
                "2", "2", "2", "да",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей сгенерировано: 3"));
        assertTrue(outcome.output().contains("Алгоритм: MergeSort"));
        assertTrue(outcome.output().contains("Данные успешно дописаны в файл"));
        assertTrue(outcome.output().contains("Работа завершена."));
        assertTrue(Files.exists(HISTORY_FILE));
    }

    /**
     * Позитивный сценарий: заполнение из файла → сортировка по году → выход.
     *
     * @throws IOException при подготовке входного файла
     */
    @Test
    void fileSourceSortByYearAndExit() throws IOException {
        Path inputFile = tempDir.resolve("input.txt");
        Files.writeString(inputFile, "150;Toyota;2015\n100;Audi;2018\n", StandardCharsets.UTF_8);

        TestSupport.Outcome outcome = runMain(lines(
                "1", "3", inputFile.toString(),
                "2", "1", "3", "нет",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей загружено: 2"));
        assertTrue(outcome.output().contains("Алгоритм: InsertionSort"));
        assertTrue(outcome.output().contains("Работа завершена."));
        assertTrue(
                outcome.output().indexOf("year=2015") < outcome.output().indexOf("year=2018"),
                "автомобили должны быть отсортированы по году по возрастанию"
        );
    }

    /**
     * Отрицательный сценарий: несуществующий файл не завершает программу —
     * выводится ошибка и происходит возврат в меню, затем корректный выход.
     */
    @Test
    void nonexistentFileReportsErrorAndReturnsToMenu() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "3", "no_such_file_12345.txt",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Ошибка:"));
        assertTrue(outcome.output().contains("Файл не существует"));
        assertTrue(outcome.output().contains("Возврат в меню"));
        assertTrue(outcome.output().contains("Работа завершена."));
        assertFalse(outcome.output().contains("Автомобилей загружено"));
    }

    /**
     * Отрицательный сценарий: нечисловой ввод и номер отсутствующего пункта
     * меню не завершают программу — после ошибок она продолжает работу и
     * корректно выходит.
     */
    @Test
    void invalidMenuNumberDoesNotExitProgram() {
        TestSupport.Outcome outcome = runMain(lines("abc", "7", "5"));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("не является целым числом"));
        assertTrue(outcome.output().contains("Пункта меню с номером 7 нет."));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Отрицательный сценарий: мощность вне диапазона отклоняется, после чего
     * принимается корректный автомобиль и программа завершается нормально.
     */
    @Test
    void invalidManualPowerRejectedThenValid() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "1", "1",
                "9999", "Toyota", "2015",
                "150", "Toyota", "2015",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("мощность вне диапазона"));
        assertTrue(outcome.output().contains("Автомобилей введено: 1"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Отрицательный сценарий: неожидаемый ответ на вопрос да/нет повторяет
     * запрос, не завершая программу.
     */
    @Test
    void invalidYesNoThenContinues() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "1", "1",
                "150", "Toyota", "2015",
                "2", "1", "1",
                "maybe", "нет",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Ожидается ответ"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Отрицательный сценарий: ошибка одного сценария (загрузка из файла)
     * не мешает следующему (ручное заполнение) и корректному выходу.
     */
    @Test
    void errorInOneScenarioDoesNotStopOthers() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "3", "missing_file_999.txt",
                "1", "1", "1",
                "150", "Toyota", "2015",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Файл не существует"));
        assertTrue(outcome.output().contains("Автомобилей введено: 1"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Позитивный сценарий: многопоточный подсчёт вхождений автомобиля
     * выводит результат в консоль и возвращает в главное меню.
     */
    @Test
    void countOptionPrintsOccurrencesAndReturnsToMainMenu() {
        TestSupport.Outcome outcome = runMain(lines(
                "1", "1", "3",
                "150", "Toyota", "2015",
                "150", "Toyota", "2015",
                "100", "Audi", "2018",
                "5", "150", "Toyota", "2015",
                "6"
        ));

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей введено: 3"));
        assertTrue(outcome.output().contains("Количество вхождений в коллекцию: 2"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }
}
