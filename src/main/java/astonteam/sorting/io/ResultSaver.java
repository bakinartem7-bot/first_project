package astonteam.sorting.io;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ResultSaver {

    private static final Pattern SAFE_FILE_NAME =
            Pattern.compile("[A-Za-z0-9_.-]{1," + Limits.MAX_FILE_NAME_LENGTH + "}");
    private static final DateTimeFormatter FILE_STAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter DATE_STAMP = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final String BORDER = "=".repeat(72);
    private static final String REPORT_TITLE = "Отчёт о сортировке";
    private static final String TABLE_HEADER = "%-4s %-10s %-24s %s";
    private static final String TABLE_ROW = "%-4d %-10d %-24s %d";

    private final Path baseDirectory;

    public ResultSaver() {
        this(Path.of("results"));
    }

    public ResultSaver(Path directory) {
        Objects.requireNonNull(directory, "directory");
        this.baseDirectory = directory.toAbsolutePath().normalize();
    }

    public Path saveReport(Collection<Car> source, Collection<Car> sorted, SortingStatistics statistics) {
        Objects.requireNonNull(statistics, "statistics");
        List<Car> sourceCars = checkedCopy(source, "исходная коллекция");
        List<Car> sortedCars = checkedCopy(sorted, "отсортированная коллекция");

        List<String> lines = new ArrayList<>();
        lines.add(BORDER);
        lines.add(REPORT_TITLE);
        lines.add("Дата: " + LocalDateTime.now().format(DATE_STAMP));
        lines.add(BORDER);
        lines.addAll(carTable("Исходная коллекция", sourceCars));
        lines.addAll(carTable("Отсортированная коллекция", sortedCars));
        lines.addAll(statisticsBlock(statistics));

        Path file = resolveSafe("sorting_" + LocalDateTime.now().format(FILE_STAMP) + ".txt");
        write(file, lines);
        return file;
    }

    public Path saveCollection(String fileName, Collection<Car> cars) {
        Objects.requireNonNull(cars, "cars");
        List<Car> checked = checkedCopy(cars, "коллекция");
        Path file = resolveSafe(fileName);
        List<String> lines = checked.stream()
                .map(CarLineFormat::format)
                .collect(Collectors.toList());
        write(file, lines);
        return file;
    }

    private Path resolveSafe(String fileName) {
        String name = TextSanitizer.normalize(fileName);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не должно быть пустым");
        }
        if (!SAFE_FILE_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException("Недопустимое имя файла: \"" + fileName + "\"");
        }
        Path resolved = baseDirectory.resolve(name).normalize();
        if (!resolved.startsWith(baseDirectory)) {
            throw new IllegalArgumentException("Путь выходит за пределы каталога результатов: " + resolved);
        }
        return resolved;
    }

    private List<Car> checkedCopy(Collection<Car> cars, String name) {
        Objects.requireNonNull(cars, name);
        List<Car> copy = new ArrayList<>(cars.size());
        for (Car car : cars) {
            if (car == null) {
                throw new IllegalArgumentException("В " + name + " найден null-элемент");
            }
            requireValidCar(car, name);
            copy.add(car);
        }
        return copy;
    }

    private void requireValidCar(Car car, String name) {
        String model = car.getModel();
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("В " + name + " автомобиль без модели");
        }
        if (TextSanitizer.hasControlChars(model)) {
            throw new IllegalArgumentException("В " + name + " модель содержит управляющие символы");
        }
    }

    private List<String> carTable(String title, Collection<Car> cars) {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("-- " + title + " --");
        lines.add("Элементов: " + cars.size());
        lines.add(String.format(Locale.ROOT, TABLE_HEADER, "№", "Мощность", "Модель", "Год"));
        int index = 1;
        for (Car car : cars) {
            lines.add(String.format(Locale.ROOT, TABLE_ROW,
                    index++, car.getPower(), car.getModel(), car.getYear()));
        }
        return lines;
    }

    private List<String> statisticsBlock(SortingStatistics statistics) {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("-- Результаты подсчёта --");
        lines.add("Алгоритм:         " + statistics.algorithmName());
        lines.add("Поле сортировки:  " + statistics.sortField());
        lines.add("Элементов:        " + statistics.size());
        lines.add("Сравнений:        " + statistics.comparisons());
        lines.add("Перестановок:     " + statistics.swaps());
        lines.add(String.format(Locale.ROOT, "Время выполнения: %.3f мс", statistics.durationMillis()));
        return lines;
    }

    private void write(Path file, List<String> lines) {
        Path temp = file.resolveSibling(file.getFileName() + ".tmp");
        try {
            Files.createDirectories(baseDirectory);
            Files.write(temp, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            moveAtomically(temp, file);
            System.out.println("Файл сохранён: " + file);
        } catch (IOException e) {
            deleteQuietly(temp);
            throw new UncheckedIOException("Не удалось записать файл " + file, e);
        }
    }

    private void moveAtomically(Path temp, Path file) throws IOException {
        try {
            Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void deleteQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Не удалось удалить временный файл: " + path);
        }
    }
}
