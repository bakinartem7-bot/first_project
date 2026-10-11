package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.util.Comparator;

public interface SortStrategy {

    /**
     * Сортирует коллекцию автомобилей на месте.
     *
     * @param cars коллекция автомобилей
     * @param comparator правило сравнения
     * @return SortingStatistics - статистика выполнения сортировки
     * @throws NullPointerException если cars или comparator равен null
     */
    SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator);
}