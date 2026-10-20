package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import astonteam.sorting.search.CarSearcher;

import java.util.List;

/**
 * Консольное меню поиска автомобилей по различным полям коллекции.
 */
public class SearchMenu {

    /**
     * Приватный конструктор для запрета создания экземпляров утилитного класса.
     */
    private SearchMenu() {
    }

    /**
     * Запрашивает критерий поиска, выводит найденные автомобили и при желании сохраняет результат.
     *
     * @param cars  рабочая коллекция автомобилей
     * @param input источник консольного ввода
     * @param saver сервис записи результата в файл
     */
    public static void searchCars(CarArrayList cars, ConsoleInput input, ResultSaver saver) {
        if (cars.size() == 0) {
            System.out.println("Сначала заполните коллекцию автомобилями.");
            return;
        }
        CarInputReader carInputReader = new CarInputReader(input);
        Menu searchMenu = new Menu("Выберите вариант поиска", List.of(
                new MenuItem(1, "Поиск по мощности"),
                new MenuItem(2, "Поиск по модели"),
                new MenuItem(3, "Поиск по году"),
                new MenuItem(4, "Поиск по всем данным"),
                new MenuItem(5, "Назад")
        ));
        int searchId = searchMenu.ask(input).id();

        CarSearcher searcher = new CarSearcher();
        CarArrayList foundCars;

        switch (searchId) {
            case 1 -> {
                int power = carInputReader.readPower();
                foundCars = searcher.findByPower(cars, power);
            }
            case 2 -> {
                String model = carInputReader.readModel();
                foundCars = searcher.findByModel(cars, model);
            }
            case 3 -> {
                int year = carInputReader.readYear();
                foundCars = searcher.findByYear(cars, year);
            }
            case 4 -> {
                System.out.println("Введите данные автомобиля:");
                Car target = carInputReader.readCar();
                foundCars = searcher.findWholeCar(cars, target);
            }
            case 5 -> {
                return;
            }
            default -> throw new IllegalArgumentException(
                    "Неизвестный вариант поиска: " + searchId
            );
        }
        printResults(foundCars);

        if (foundCars.size() > 0) {
            if (input.readYesNo("Дописать найденные автомобили в файл history.txt?")) {
                saver.appendCollection("history.txt", foundCars, "Результат поиска");
            }
        }
    }

    /**
     * Выводит найденные автомобили или сообщение об отсутствии совпадений.
     *
     * @param foundCars коллекция найденных автомобилей
     */
    private static void printResults(CarArrayList foundCars) {
        if (foundCars.size() == 0) {
            System.out.println("Совпадений не найдено.");
            return;
        }
        System.out.println("Найденные автомобили:");
        for (Car car : foundCars) {
            System.out.println(car);
        }
    }
}
