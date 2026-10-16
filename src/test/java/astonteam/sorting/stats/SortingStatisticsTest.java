package astonteam.sorting.stats;

import astonteam.sorting.common.Limits;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты статистики сортировки {@link SortingStatistics}: проверяют хранение полей,
 * пересчёт длительности и валидацию входных значений.
 */
class SortingStatisticsTest {

    /**
     * Проверяет, что все поля сохраняются и доступны корректно.
     */
    @Test
    void storesAllFields() {
        SortingStatistics statistics =
                new SortingStatistics("Bubble", "power", 3, 10, 4, Duration.ofMillis(2));
        assertEquals("Bubble", statistics.algorithmName());
        assertEquals("power", statistics.sortField());
        assertEquals(3, statistics.size());
        assertEquals(10, statistics.comparisons());
        assertEquals(4, statistics.swaps());
    }

    /**
     * Проверяет, что длительность корректно пересчитывается в миллисекунды.
     */
    @Test
    void durationMillisConvertsCorrectly() {
        SortingStatistics statistics =
                new SortingStatistics("Bubble", "power", 1, 0, 0, Duration.ofMillis(1500));
        assertEquals(1500.0, statistics.durationMillis(), 1e-9);
    }

    /**
     * Проверяет, что отрицательные значения счётчиков (размер, сравнения, обмены) отклоняются.
     */
    @Test
    void rejectsNegativeCounters() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", -1, 0, 0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", 0, -1, 0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", 0, 0, -1, Duration.ZERO));
    }

    /**
     * Проверяет, что отрицательная длительность отклоняется.
     */
    @Test
    void rejectsNegativeDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", 0, 0, 0, Duration.ofMillis(-1)));
    }

    /**
     * Проверяет, что размер, превышающий {@link Limits#MAX_COLLECTION_SIZE}, отклоняется.
     */
    @Test
    void rejectsTooBigSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power",
                        Limits.MAX_COLLECTION_SIZE + 1, 0, 0, Duration.ZERO));
    }

    /**
     * Проверяет, что пустые или состоящие из пробелов текстовые поля отклоняются.
     */
    @Test
    void rejectsBlankTextFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("   ", "power", 0, 0, 0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "  ", 0, 0, 0, Duration.ZERO));
    }

    /**
     * Проверяет, что null вместо длительности выбрасывает NullPointerException.
     */
    @Test
    void rejectsNullDuration() {
        assertThrows(NullPointerException.class,
                () -> new SortingStatistics("Bubble", "power", 0, 0, 0, null));
    }
}
