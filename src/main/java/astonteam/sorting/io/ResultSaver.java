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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Сохраняет результаты сортировки и коллекции автомобилей в текстовые файлы.
 */
public final class ResultSaver {
    /** Шаблон допустимого имени файла. */
    private static final Pattern SAFE_FILE_NAME =
            Pattern.compile("[A-Za-z0-9_.-]{1," + Limits.MAX_FILE_NAME_LENGTH + "}");
    /** Формат метки времени в имени файла. */
    private static final DateTimeFormatter FILE_STAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    /** Формат даты в отчёте. */
    private static final DateTimeFormatter DATE_STAMP = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    /** Разделительная линия отчёта. */
    private static final String BORDER = "=".repeat(72);
    /** Заголовок отчёта. */
    private static final String REPORT_TITLE = "Отчёт о сортировке";
    /** Формат строки заголовка таблицы. */
    private static final String TABLE_HEADER = "%-4s %-10s %-24s %s";
    /** Формат строки данных таблицы. */
    private static final String TABLE_ROW = "%-4d %-10d %-24s %d";

    /** Базовый каталог для сохранения файлов. */
    private final Path baseDirectory;

    /**
     * Создаёт сохранитель с каталогом результатов по умолчанию.
     */
    public ResultSaver() {
        this(Path.of("results"));
    }

    /**
     * Создаёт сохранитель с указанным каталогом.
     *
     * @param directory базовый каталог для сохранения файлов
     */
    public ResultSaver(Path directory) {
        Objects.requireNonNull(directory, "directory");
        this.baseDirectory = directory.toAbsolutePath().normalize();
    }

    /**
     * Сохраняет отчёт о сортировке с исходной и отсортированной коллекциями.
     *
     * @param source     исходная коллекция автомобилей
     * @param sorted     отсортированная коллекция автомобилей
     * @param statistics статистика сортировки
     * @return путь к сохранённому файлу
     */
    public Path saveReport(Iterable<Car> source, Iterable<Car> sorted, SortingStatistics statistics) {
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
        write(file, lines, false);
        return file;
    }

    /**
     * Сохраняет коллекцию автомобилей в файл с указанным именем (ПЕРЕЗАПИСЬ).
     */
    public Path saveCollection(String fileName, Iterable<Car> cars) {
        Objects.requireNonNull(cars, "cars");
        List<Car> checked = checkedCopy(cars, "коллекция");
        Path file = resolveSafe(fileName);
        List<String> lines = checked.stream()
                .map(CarLineFormat::format)
                .collect(Collectors.toList());
        write(file, lines, false);
        return file;
    }

    /**
     * ДОПИСЫВАЕТ коллекцию автомобилей в конец файла (APPEND).
     * Добавляет человекочитаемую шапку с датой и названием действия.
     *
     * @param fileName имя файла
     * @param cars коллекция автомобилей
     * @param actionName название действия (например, "Результат сортировки" или "Результат поиска")
     * @return путь к файлу
     */
    public Path appendCollection(String fileName, Iterable<Car> cars, String actionName) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(actionName, "actionName");
        List<Car> checked = checkedCopy(cars, "коллекция");
        Path file = resolveSafe(fileName);

        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("--- " + actionName + " | Дата: " + LocalDateTime.now().format(DATE_STAMP) + " ---");

        if (checked.isEmpty()) {
            lines.add("(пустая коллекция)");
        } else {
            for (int i = 0; i < checked.size(); i++) {
                lines.add((i + 1) + ". " + CarLineFormat.format(checked.get(i)));
            }
        }
        lines.add("--------------------------------------------------");

        write(file, lines, true);
        return file;
    }

    /**
     * Читает текстовый файл результатов построчно.
     *
     * @param fileName имя файла в каталоге результатов
     * @return строки файла; пустой список, если файл не существует
     * @throws UncheckedIOException при ошибке чтения файла
     */
    public List<String> readLines(String fileName) {
        Path file = resolveSafe(fileName);
        if (!Files.exists(file)) {
            return List.of();
        }
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать файл " + file, e);
        }
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

    private List<Car> checkedCopy(Iterable<Car> cars, String name) {
        Objects.requireNonNull(cars, name);
        List<Car> copy = new ArrayList<>();
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

    private List<String> carTable(String title, List<Car> cars) {
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

    /**
     * Записывает строки в файл.
     * @param append если true - дописывает в конец (APPEND), иначе перезаписывает.
     */
    private void write(Path file, List<String> lines, boolean append) {
        try {
            Files.createDirectories(baseDirectory);
            if (append) {
                Files.write(file, lines, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Данные успешно дописаны в файл: " + file);
            } else {
                Path temp = file.resolveSibling(file.getFileName() + ".tmp");
                Files.write(temp, lines, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                moveAtomically(temp, file);
                System.out.println("Файл сохранён: " + file);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось записать файл " + file, e);
        }
    }

    /**
     * Перемещает временный файл на место целевого, по возможности атомарно.
     *
     * @param temp временный файл
     * @param file целевой файл
     * @throws IOException при ошибке перемещения файла
     */
    private void moveAtomically(Path temp, Path file) throws IOException {
        try {
            Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
