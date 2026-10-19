package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация сортировки вставками, применяющейся только к автомобилям
 * с чётной мощностью (остальные позиции сохраняются).
 */
public class EvenPowerInsertionSort implements SortStrategy {
    /**
     * Создаёт стратегию сортировки чётных по мощности автомобилей для указанного поля.
     */
    public EvenPowerInsertionSort() {
    }

    /**
     * Отбирает автомобили с чётной мощностью, сортирует их вставками
     * и возвращает на исходные позиции, подсчитывая статистику.
     *
     * @param cars       сортируемая коллекция
     * @param comparator компаратор, определяющий порядок сортировки
     * @return статистика сортировки
     * @throws NullPointerException если {@code cars} или {@code comparator} равны {@code null}
     */
    @Override
    public SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(comparator, "comparator");
        String algorithmName = "EvenPowerInsertionSort";
        int size = cars.size();
        long beginTime = System.nanoTime();

        CarArrayList evenPowerCars = new CarArrayList();
        int[] evenPowerCarPositions = new int[cars.size()];
        int evenCount = 0;

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getPower() % 2 == 0) {
                evenPowerCarPositions[evenCount] = i;
                evenPowerCars.add(car);
                evenCount++;
            }
        }

        // Количество элементов, изменивших позиции при возврате в исходную коллекцию
        long restoredMoves = 0;

        SortingStatistics insertionStatistics =
                new InsertionSort("power").sort(evenPowerCars, comparator);

        for (int i = 0; i < evenCount; i++) {
            int originalIndex = evenPowerCarPositions[i];
            Car sortedCar = evenPowerCars.get(i);

            if (cars.get(originalIndex) != sortedCar) {
                restoredMoves++;
            }
            cars.set(originalIndex, sortedCar);
        }

        Duration duration = Duration.ofNanos(System.nanoTime() - beginTime);

        return new SortingStatistics(
                algorithmName,
                "power",
                size,
                insertionStatistics.comparisons(),
                insertionStatistics.swaps() + restoredMoves,
                duration
        );
    }
}
