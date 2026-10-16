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
public class EvenPowerSort implements SortStrategy {
    /**
     * Название поля, по которому выполняется сортировка (для статистики).
     */
    private final String sortField;

    /**
     * Создаёт стратегию сортировки чётных по мощности автомобилей для указанного поля.
     *
     * @param sortField название сортируемого поля
     * @throws NullPointerException если {@code sortField} равен {@code null}
     */
    public EvenPowerSort(String sortField) {
        this.sortField = Objects.requireNonNull(sortField, "sortField");
    }

    /**
     * Отбирает автомобили с чётной мощностью, сортирует их вставками
     * и возвращает на исходные позиции, подсчитывая статистику.
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
        String algorithmName = "EvenPowerInsertionSort";
        int size = cars.size();
        Counters counters = new Counters();
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

        for (int i = 1; i < evenPowerCars.size(); i++) {
            Car key = evenPowerCars.get(i);
            int j = i - 1;
            while (j >= 0) {
                counters.comparisons++;
                if (comparator.compare(evenPowerCars.get(j), key) <= 0) {
                    break;
                }
                setAndCount(evenPowerCars, j + 1, evenPowerCars.get(j), counters);
                j--;
            }
            setAndCount(evenPowerCars, j + 1, key, counters);
        }
        for (int i = 0; i < evenCount; i++) {
            int originalIndex = evenPowerCarPositions[i];
            setAndCount(cars, originalIndex, evenPowerCars.get(i), counters);
        }

        Duration duration = Duration.ofNanos(
                System.nanoTime() - beginTime
        );
        return new SortingStatistics(algorithmName, sortField, size, counters.comparisons, counters.swaps, duration);
    }

    /**
     * Записывает автомобиль в заданную позицию, увеличивая счётчик
     * перемещений, если ссылка в этой позиции изменилась.
     *
     * @param cars     коллекция, в которую выполняется запись
     * @param index    индекс целевой позиции
     * @param car      записываемый автомобиль
     * @param counters счётчики операций сортировки
     */
    private static void setAndCount(CarArrayList cars, int index, Car car, Counters counters) {
        if (cars.get(index) != car) {
            counters.swaps++;
        }
        cars.set(index, car);
    }

    /**
     * Счётчики операций, накапливаемые во время сортировки.
     */
    private static final class Counters {
        /**
         * Число сравнений элементов.
         */
        private long comparisons;

        // Число записей, изменивших ссылку в коллекциях во время сортировки.
        private long swaps;
    }
}
