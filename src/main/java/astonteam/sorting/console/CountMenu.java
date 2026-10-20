package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.search.CarCounter;

/**
 * Консольный сценарий многопоточного подсчёта вхождений автомобиля.
 */
public final class CountMenu {

    /**
     * Приватный конструктор для запрета создания экземпляров утилитного класса.
     */
    private CountMenu() {
    }

    /**
     * Запрашивает автомобиль и выводит в консоль число его вхождений в коллекцию.
     *
     * @param cars    рабочая коллекция автомобилей
     * @param input   источник консольного ввода
     * @param counter многопоточный счётчик вхождений
     */
    public static void countCars(CarArrayList cars, ConsoleInput input, CarCounter counter) {
        if (cars.size() == 0) {
            System.out.println("Сначала заполните коллекцию автомобилями.");
            return;
        }

        System.out.println("Введите автомобиль для подсчёта вхождений:");
        CarInputReader carInputReader = new CarInputReader(input);
        Car target = carInputReader.readCar();

        int found = counter.countOccurrences(cars, target);
        System.out.println("Количество вхождений в коллекцию: " + found);
    }
}
