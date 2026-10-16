package astonteam.sorting;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void fillAndExit() {
        TestSupport.Outcome outcome =
                TestSupport.run("1\n1\n150\nToyota\n2015\n7\n", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей введено: 1"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    @Test
    void invalidSizeIsRejectedThenAcceptsValid() {
        TestSupport.Outcome outcome =
                TestSupport.run("1\n0\n1\n150\nToyota\n2015\n7\n", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Автомобилей введено: 1"));
    }

    @Test
    void attemptsExceededReturnsToMenu() {
        TestSupport.Outcome outcome =
                TestSupport.run("9\n9\n9\n9\n9\n7\n", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Возврат в меню"));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    @Test
    void endOfInputFinishesGracefully() {
        TestSupport.Outcome outcome = TestSupport.run("", () -> Main.main(new String[0]));
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    @Test
    void saveOptionWritesFileAndReturnsToMenu() throws IOException {
        Path resultsDirectory = Path.of("results");
        Path carsFile = resultsDirectory.resolve("cars.txt");
        byte[] previous = Files.exists(carsFile) ? Files.readAllBytes(carsFile) : null;
        try {
            TestSupport.Outcome outcome =
                    TestSupport.run("1\n1\n150\nToyota\n2015\n6\n7\n",
                            () -> Main.main(new String[0])
                    );
            assertNull(outcome.error());
            assertTrue(outcome.output().contains("Файл сохранён"));
            assertTrue(Files.exists(carsFile));
        } finally {
            if (previous == null) {
                Files.deleteIfExists(carsFile);
                deleteIfEmpty(resultsDirectory);
            } else {
                Files.write(carsFile, previous);
            }
        }
        if (previous == null) {
            assertFalse(Files.exists(carsFile));
        }
    }

    @Test
    void searchOptionFindsCarAndReturnsToMainMenu() {
        TestSupport.Outcome outcome = TestSupport.run(
                "1\n1\n150\nToyota\n2015\n5\n1\n150\n7\n",
                () -> Main.main(new String[0])
        );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains("Найденные автомобили:"));
        assertTrue(outcome.output().contains(
                "Car{power=150, model='Toyota', year=2015}"
        ));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    @Test
    void searchOptionOnEmptyCollectionReturnsToMainMenu() {
        TestSupport.Outcome outcome = TestSupport.run(
                "5\n7\n",
                () -> Main.main(new String[0])
        );

        assertNull(outcome.error());
        assertTrue(outcome.output().contains(
                "Сначала заполните коллекцию автомобилями."
        ));
        assertTrue(outcome.output().contains("Работа завершена."));
    }

    private static void deleteIfEmpty(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return;
        }
        try (Stream<Path> entries = Files.list(directory)) {
            if (entries.findAny().isEmpty()) {
                Files.delete(directory);
            }
        }
    }
}
