package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.sort.CarComparators;
import astonteam.sorting.sort.InsertionSort;
import astonteam.sorting.sort.MergeSort;
import astonteam.sorting.sort.SortStrategy;
import astonteam.sorting.stats.SortingStatistics;

import java.util.Comparator;
import java.util.List;

public class SortingMenu {
    public static void sortCars(CarArrayList cars, ConsoleInput input) {
        if (cars.size() == 0) {
            System.out.println("Сначала заполните коллекцию автомобилями.");
            return;
        }

        // ---  ---
        Menu algorithmMenu = new Menu("Выберите алгоритм сортировки", List.of(
                new MenuItem(1, "Сортировка вставками"),
                new MenuItem(2, "Сортировка слиянием")
        ));
        int algorithmId = algorithmMenu.ask(input).id();

        Menu fieldMenu = new Menu("Выберите поле сортировки", List.of(
                new MenuItem(1, "Мощность"),
                new MenuItem(2, "Модель"),
                new MenuItem(3, "Год выпуска")
        ));
        int fieldId = fieldMenu.ask(input).id();

        String sortField;
        Comparator<Car> comparator;

        switch (fieldId) {
            case 1 -> {
                sortField = "power";
                comparator = CarComparators.BY_POWER;
            }
            case 2 -> {
                sortField = "model";
                comparator = CarComparators.BY_MODEL;
            }
            case 3 -> {
                sortField = "year";
                comparator = CarComparators.BY_YEAR;
            }
            default -> throw new IllegalArgumentException("Неизвестное поле сортировки: " + fieldId);
        }

        SortStrategy strategy = switch (algorithmId) {
            case 1 -> new InsertionSort(sortField);
            case 2 -> new MergeSort(sortField);
            default -> throw new IllegalArgumentException("Неизвестный алгоритм сортировки: " + algorithmId);
        };

        SortingStatistics statistics = strategy.sort(cars, comparator);

        // ---  ---
        System.out.println("Сортировка завершена.");
        System.out.println("Алгоритм: " + statistics.algorithmName());
        System.out.println("Поле: " + statistics.sortField());
        System.out.println("Сравнений: " + statistics.comparisons());
        System.out.println("Перемещений: " + statistics.swaps());
        System.out.printf("Время: %.3f мс%n", statistics.durationMillis());
        System.out.println("Количество элементов: " + statistics.size());
        System.out.println("--------------------------------------------------");
        System.out.println("Отсортированная коллекция:");
        for (Car car : cars) {
            System.out.println(car);
        }
        // -------------------------------------------------------------
    } // 
}