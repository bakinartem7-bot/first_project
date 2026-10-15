package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.stats.SortingStatistics;

import java.util.Comparator;

public interface SortStrategy {

    SortingStatistics sort(CarArrayList cars, Comparator<Car> comparator);
}