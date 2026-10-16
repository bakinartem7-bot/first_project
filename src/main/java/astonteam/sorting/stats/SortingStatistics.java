package astonteam.sorting.stats;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

import java.time.Duration;
import java.util.Objects;

/**
 * Статистика выполнения сортировки.
 *
 * @param algorithmName название алгоритма сортировки
 * @param sortField     поле, по которому выполнялась сортировка
 * @param size          количество элементов
 * @param comparisons   количество сравнений
 * @param swaps         количество перестановок
 * @param duration      длительность выполнения
 */
public record SortingStatistics(
        String algorithmName,
        String sortField,
        int size,
        long comparisons,
        long swaps,
        Duration duration) {

    /**
     * Компактный конструктор, выполняющий нормализацию и проверку полей.
     *
     * @throws NullPointerException     если {@code duration} равен {@code null}
     * @throws IllegalArgumentException если данные не проходят проверки
     */
    public SortingStatistics {
        algorithmName = TextSanitizer.requireText(algorithmName, "algorithmName", Limits.MAX_TEXT_LENGTH);
        sortField = TextSanitizer.requireText(sortField, "sortField", Limits.MAX_TEXT_LENGTH);
        Objects.requireNonNull(duration, "duration");
        if (size < 0 || comparisons < 0 || swaps < 0) {
            throw new IllegalArgumentException("Счётчики не могут быть отрицательными");
        }
        if (size > Limits.MAX_COLLECTION_SIZE) {
            throw new IllegalArgumentException("Размер превышает допустимый: " + size);
        }
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Длительность не может быть отрицательной");
        }
    }

    /**
     * Возвращает длительность в миллисекундах.
     *
     * @return длительность в миллисекундах
     */
    public double durationMillis() {
        return duration.toNanos() / 1_000_000.0;
    }
}
