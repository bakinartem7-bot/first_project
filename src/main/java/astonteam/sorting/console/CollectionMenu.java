package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;

/**
 * Консольный сценарий показа текущей коллекции автомобилей.
 */
public final class CollectionMenu {

    private CollectionMenu() {
    }

    /**
     * Выводит в консоль содержимое текущей коллекции.
     *
     * @param cars рабочая коллекция автомобилей
     */
    public static void showCollection(CarArrayList cars) {
        if (cars.size() == 0) {
            System.out.println("Коллекция пуста.");
            return;
        }

        System.out.println("Текущая коллекция (элементов: " + cars.size() + "):");
        for (Car car : cars) {
            System.out.println(car);
        }
    }
}
