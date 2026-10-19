package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация сортировки вставками для коллекции автомобилей.
 */
public class InsertionSort implements SortStrategy {
    /**
     * Название поля, по которому выполняется сортировка (для статистики).
     */
    private final String sortField;

    /**
     * Создаёт стратегию сортировки вставками для указанного поля.
     *
     * @param sortField название сортируемого поля
     * @throws NullPointerException если {@code sortField} равен {@code null}
     */
    public InsertionSort(String sortField) {
        this.sortField = Objects.requireNonNull(sortField);
    }

    /**
     * Сортирует коллекцию методом вставки, подсчитывая сравнения и перемещения.
     *
     * @param cars      сортируемая коллекция
     * @param comparator компаратор, определяющий порядок сортировки
     * @return статистика сортировки
     * @throws NullPointerException если {@code cars} или {@code comparator} равны {@code null}
     */
    @Override
    public SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(comparator, "comparator");
        String algorithmName = "InsertionSort";
        int size = cars.size();
        Counters counters = new Counters();

        long beginTime = System.nanoTime();
        for (int i = 1; i < cars.size(); i++) {
            Car key = cars.get(i);
            int j = i - 1;
            while (j >= 0) {
                counters.comparisons++;
                if (comparator.compare(cars.get(j), key) <= 0) {
                    break;
                }
                cars.set(j + 1, cars.get(j));
                counters.swaps++;
                j--;
            }
            if (cars.get(j + 1) != key) {
                counters.swaps++;
            }
            cars.set(j + 1, key);
        }
        Duration duration = Duration.ofNanos(
                System.nanoTime() - beginTime
        );
        return new SortingStatistics(algorithmName, sortField, size, counters.comparisons, counters.swaps, duration);
    }

    /**
     * Счётчики операций, накапливаемые во время сортировки.
     */
    private static final class Counters {
        /**
         * Число сравнений элементов.
         */
        private long comparisons;

        /** Счётчик сдвигов и обменов за последний вызов сортировки. */
        private long swaps;
    }
}