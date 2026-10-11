package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

public class InsertionSort implements SortStrategy {
    private final String sortField;

    public InsertionSort(String sortField) {
        this.sortField = Objects.requireNonNull(sortField);
    }

    @Override
    public SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(comparator, "comparator");
        String algorithmName = "InsertionSort";
        int size = cars.size();
        long comparisons = 0;
        long swaps = 0;
        long beginTime = System.nanoTime();
        for (int i = 1; i < cars.size(); i++) {
            Car key = cars.get(i);
            int j = i - 1;
            while (j >= 0) {
                comparisons++;
                if (comparator.compare(cars.get(j), key) <= 0) {
                    break;
                }
                cars.set(j + 1, cars.get(j));
                swaps++;
                j--;
            }
            cars.set(j + 1, key);
        }
        Duration duration = Duration.ofNanos(
                System.nanoTime() - beginTime
        );
        return new SortingStatistics(algorithmName, sortField, size, comparisons, swaps, duration);
    }
}