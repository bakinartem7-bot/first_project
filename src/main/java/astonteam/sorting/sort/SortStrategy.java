package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.util.Comparator;

/**
 * Стратегия сортировки коллекции автомобилей по заданному компаратору.
 */
public interface SortStrategy {

    /**
     * Сортирует коллекцию автомобилей на месте и собирает статистику выполнения.
     *
     * @param cars      сортируемая коллекция
     * @param comparator компаратор, определяющий порядок сортировки
     * @return статистика сортировки: алгоритм, поле, размер, сравнения, перемещения и время
     * @throws NullPointerException если {@code cars} или {@code comparator} равны {@code null}
     */
    SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator);
}