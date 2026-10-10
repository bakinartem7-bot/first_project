package astonteam.sorting.stats;

import astonteam.sorting.common.Limits;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortingStatisticsTest {

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

    @Test
    void durationMillisConvertsCorrectly() {
        SortingStatistics statistics =
                new SortingStatistics("Bubble", "power", 1, 0, 0, Duration.ofMillis(1500));
        assertEquals(1500.0, statistics.durationMillis(), 1e-9);
    }

    @Test
    void rejectsNegativeCounters() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", -1, 0, 0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", 0, -1, 0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", 0, 0, -1, Duration.ZERO));
    }

    @Test
    void rejectsNegativeDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power", 0, 0, 0, Duration.ofMillis(-1)));
    }

    @Test
    void rejectsTooBigSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "power",
                        Limits.MAX_COLLECTION_SIZE + 1, 0, 0, Duration.ZERO));
    }

    @Test
    void rejectsBlankTextFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("   ", "power", 0, 0, 0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new SortingStatistics("Bubble", "  ", 0, 0, 0, Duration.ZERO));
    }

    @Test
    void rejectsNullDuration() {
        assertThrows(NullPointerException.class,
                () -> new SortingStatistics("Bubble", "power", 0, 0, 0, null));
    }
}
