package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

public class MergeSort implements SortStrategy {

    private final String sortField;

    public MergeSort(String sortField) {
        this.sortField = Objects.requireNonNull(sortField, "sortField");
    }

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

    private static final class Counters {
        private long comparisons;

        // Число позиций, в которых после слияния изменился объект
        private long swaps;
    }
}