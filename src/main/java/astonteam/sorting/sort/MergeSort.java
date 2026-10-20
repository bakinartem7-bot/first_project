package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация сортировки слиянием для коллекции автомобилей.
 */
public class MergeSort implements SortStrategy {

    /**
     * Название поля, по которому выполняется сортировка (для статистики).
     */
    private final String sortField;

    /**
     * Создаёт стратегию сортировки слиянием для указанного поля.
     *
     * @param sortField название сортируемого поля
     * @throws NullPointerException если {@code sortField} равен {@code null}
     */
    public MergeSort(String sortField) {
        this.sortField = Objects.requireNonNull(sortField, "sortField");
    }

    /**
     * Сортирует коллекцию методом слияния, подсчитывая сравнения и перемещения.
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
        long startTime = System.nanoTime();

        Counters counters = new Counters();

        CarArrayList temp = new CarArrayList();
        for (Car car : cars) {
            temp.add(car);
        }

        mergeSort(cars, temp, 0, cars.size(), comparator, counters);
        Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
        return new SortingStatistics(
                "MergeSort",
                sortField,
                cars.size(),
                counters.comparisons,
                counters.swaps,
                duration);
    }

    /**
     * Рекурсивно разбивает диапазон на половины и объединяет их.
     *
     * @param cars      сортируемая коллекция
     * @param temp      временная коллекция для слияния
     * @param left      индекс начала диапазона (включительно)
     * @param right     индекс конца диапазона (исключительно)
     * @param comparator компаратор, определяющий порядок сортировки
     * @param counters  счётчики операций сортировки
     */
    private void mergeSort(
            CarArrayList cars,
            CarArrayList temp,
            int left,
            int right,
            Comparator<Car> comparator,
            Counters counters
    ) {
        if (right - left <= 1) {
            return;
        }
        int middle = left + (right - left) / 2;

        mergeSort(cars, temp, left, middle, comparator, counters);
        mergeSort(cars, temp, middle, right, comparator, counters);
        merge(cars, temp, left, middle, right, comparator, counters);
    }

    /**
     * Сливает два отсортированных смежных диапазона в одну отсортированную последовательность.
     *
     * @param cars      сортируемая коллекция
     * @param temp      временная коллекция, в которой выполняется слияние
     * @param left      индекс начала левого диапазона (включительно)
     * @param middle    граница между левым и правым диапазонами
     * @param right     индекс конца правого диапазона (исключительно)
     * @param comparator компаратор, определяющий порядок сортировки
     * @param counters  счётчики операций сортировки
     */
    private void merge(
            CarArrayList cars,
            CarArrayList temp,
            int left,
            int middle,
            int right,
            Comparator<Car> comparator,
            Counters counters
    ) {
        int leftIndex = left;
        int rightIndex = middle;
        int tempIndex = left;

        while (leftIndex < middle && rightIndex < right) {
            counters.comparisons++;
            if (comparator.compare(cars.get(leftIndex), cars.get(rightIndex)) <= 0) {
                temp.set(tempIndex++, cars.get(leftIndex++));
            } else {
                temp.set(tempIndex++, cars.get(rightIndex++));
            }
        }

        while (leftIndex < middle) {
            temp.set(tempIndex++, cars.get(leftIndex++));
        }

        while (rightIndex < right) {
            temp.set(tempIndex++, cars.get(rightIndex++));
        }

        for (int i = left; i < right; i++) {
            Car mergedCar = temp.get(i);
            if (cars.get(i) != mergedCar) {
                counters.swaps++;
            }
            cars.set(i, mergedCar);
        }
    }

    /**
     * Счётчики операций, накапливаемые во время сортировки.
     */
    private static final class Counters {
        /**
         * Число сравнений элементов.
         */
        private long comparisons;

        /** Счётчик изменений позиций при слиянии. */
        private long swaps;
    }
}