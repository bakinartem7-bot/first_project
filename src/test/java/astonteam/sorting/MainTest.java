package astonteam.sorting;

import astonteam.sorting.io.ResultSaver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Интеграционные тесты точки входа {@link Main}: эмулируют ввод пользователя
 * и проверяют реакции программы на разные сценарии работы меню.
 */
class MainTest {

    /** Временный каталог: все файлы тестов пишутся сюда, а не в настоящий {@code results/}. */
    @TempDir
    Path tempDir;

    /**
     * Перенаправляет каталог результатов во временный каталог,
     * чтобы тесты не затрагивали реальные данные.
     */
    @BeforeEach
    void useTemporaryResultsDirectory() {
        System.setProperty(ResultSaver.RESULTS_DIRECTORY_PROPERTY, tempDir.toString());
    }

    /**
     * Возвращает систему в исходное состояние после теста.
     */
    @AfterEach
    void resetResultsDirectory() {
        System.clearProperty(ResultSaver.RESULTS_DIRECTORY_PROPERTY);
    }

    /**
     * Проверяет сценарий заполнения коллекции одним автомобилем и корректного выхода,
     * а также сообщения о количестве введённых автомобилей и завершении работы.
     */
    @Test
    void fillAndExit() {
        TestSupport.Outcome outcome =
                TestSupport.run("1\n1\n1\n150\nToyota\n2015\n8\nнет\n", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей введено: 1"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Проверяет, что недопустимый размер коллекции отклоняется,
     * после чего программа принимает корректное значение и завершает ввод.
     */
    @Test
    void invalidSizeIsRejectedThenAcceptsValid() {
        TestSupport.Outcome outcome =
                TestSupport.run("1\n1\n0\n1\n150\nToyota\n2015\n8\nнет\n", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей введено: 1"));
    }

    /**
     * Проверяет, что при превышении числа попыток ввода происходит возврат в меню
     * и последующий корректный выход из программы.
     */
    @Test
    void attemptsExceededReturnsToMenu() {
        TestSupport.Outcome outcome =
                TestSupport.run("9\n9\n9\n9\n9\n8\n", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Возврат в меню"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Проверяет корректное завершение программы при достижении конца входного потока
     * (пустой ввод), без возникновения исключений.
     */
    @Test
    void endOfInputFinishesGracefully() {
        TestSupport.Outcome outcome = TestSupport.run("", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Проверяет, что пункт сохранения после подтверждения записывает файл
     * с автомобилями и возвращает в меню.
     */
    @Test
    void saveOptionWritesFileAndReturnsToMenu() {
        Path carsFile = tempDir.resolve("cars.txt");

        TestSupport.Outcome outcome =
                TestSupport.run("1\n1\n1\n150\nToyota\n2015\n7\nда\n8\nнет\n",
                        () -> Main.main(new String[0])
                );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Файл сохранён"));
        assertTrue(Files.exists(carsFile));
    }

    /**
     * Проверяет, что сохранение отклоняется без подтверждения: после ответа
     * «нет» файл не создаётся, а программа продолжает работу.
     */
    @Test
    void saveOptionDeclinedDoesNotWriteFile() {
        Path carsFile = tempDir.resolve("cars.txt");

        TestSupport.Outcome outcome =
                TestSupport.run("1\n1\n1\n150\nToyota\n2015\n7\nнет\n8\nнет\n",
                        () -> Main.main(new String[0])
                );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сохранение отменено."));
        assertFalse(Files.exists(carsFile));
    }

    /**
     * Проверяет, что поиск находит ранее добавленный автомобиль,
     * выводит его и возвращает в главное меню.
     */
    @Test
    void searchOptionFindsCarAndReturnsToMainMenu() {
        TestSupport.Outcome outcome = TestSupport.run(
                "1\n1\n1\n150\nToyota\n2015\n4\n1\n150\n8\nнет\n",
                () -> Main.main(new String[0])
        );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Найденные автомобили:"));
        assertTrue(outcome.output().contains(
                "Мощность: 150; Модель: Toyota; Год: 2015"
        ));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Проверяет, что поиск по пустой коллекции сообщает о необходимости
     * сначала заполнить коллекцию и возвращает в главное меню.
     */
    @Test
    void searchOptionOnEmptyCollectionReturnsToMainMenu() {
        TestSupport.Outcome outcome = TestSupport.run(
                "4\n8\n",
                () -> Main.main(new String[0])
        );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains(
                "Сначала заполните коллекцию автомобилями."
        ));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Проверяет, что при выходе с непустой коллекцией программа предлагает
     * сохранить её и после ответа «да» записывает файл.
     *
     * @throws Exception при ошибке чтения записного файла
     */
    @Test
    void exitWithSaveWritesFile() throws Exception {
        Path carsFile = tempDir.resolve("cars.txt");

        TestSupport.Outcome outcome = TestSupport.run(
                "1\n1\n1\n150\nToyota\n2015\n8\nда\n",
                () -> Main.main(new String[0])
        );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Сохранить коллекцию перед выходом"));
        assertTrue(outcome.output().contains("Файл сохранён"));
        assertTrue(Files.exists(carsFile));
        assertTrue(Files.readString(carsFile).contains("150;Toyota;2015"));
    }

    /**
     * Проверяет, что при выходе отказ от сохранения оставляет файл незатронутым.
     */
    @Test
    void exitWithSaveDeclinedDoesNotWriteFile() {
        Path carsFile = tempDir.resolve("cars.txt");

        TestSupport.Outcome outcome = TestSupport.run(
                "1\n1\n1\n150\nToyota\n2015\n8\nнет\n",
                () -> Main.main(new String[0])
        );

        assertNull(outcome.error());
        assertFalse(Files.exists(carsFile));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    /**
     * Проверяет, что при пустой коллекции перед выходом вопрос о сохранении не задаётся.
     */
    @Test
    void exitOnEmptyCollectionDoesNotAskToSave() {
        Path carsFile = tempDir.resolve("cars.txt");

        TestSupport.Outcome outcome = TestSupport.run("8\n", () -> Main.main(new String[0]));

        assertNull(outcome.error());
        assertFalse(outcome.output().contains("Сохранить коллекцию перед выходом"));
        assertTrue(outcome.output().contains("Работа завершена."));
        assertFalse(Files.exists(carsFile));
    }
}
