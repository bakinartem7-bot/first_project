package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

public class MergeSort implements SortStrategy {

    private final String sortField;

    private long comparisons;
    private long swaps;

    public MergeSort(String sortField) {
        this.sortField = Objects.requireNonNull(sortField, "sortField");
    }

    @Override
    public SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(comparator, "comparator");
        comparisons = 0;
        swaps = 0;
        long startTime = System.nanoTime();

        CarArrayList temp = new CarArrayList();
        for (Car car : cars) {
            temp.add(car);
        }

        mergeSort(cars, temp, 0, cars.size(), comparator);
        Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
        return new SortingStatistics(
                "MergeSort",
                sortField,
                cars.size(),
                comparisons,
                swaps,
                duration);
    }

    private void mergeSort(
            CarArrayList cars,
            CarArrayList temp,
            int left,
            int right,
            Comparator<Car> comparator
    ) {
        if (right - left <= 1) {
            return;
        }
        int middle = left + (right - left) / 2;

        mergeSort(cars, temp, left, middle, comparator);
        mergeSort(cars, temp, middle, right, comparator);
        merge(cars, temp, left, middle, right, comparator);
    }

    private void merge(
            CarArrayList cars,
            CarArrayList temp,
            int left,
            int middle,
            int right,
            Comparator<Car> comparator
    ) {
        int leftIndex = left;
        int rightIndex = middle;
        int tempIndex = left;

        while (leftIndex < middle && rightIndex < right) {
            comparisons++;
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
            cars.set(i, temp.get(i));
            swaps++;
        }
    }
}