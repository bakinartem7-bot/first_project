package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import astonteam.sorting.search.CarSearcher;
import astonteam.sorting.common.Limits;

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
                int power = readPower(input);
                input.markSuccess();
                foundCars = searcher.findByPower(cars, power);
            }
            case 2 -> {
                String model = input.readNonBlank("Введите модель: ");
                foundCars = searcher.findByModel(cars, model);
            }
            case 3 -> {
                int year = input.readInt("Введите год выпуска: ");
                foundCars = searcher.findByYear(cars, year);
            }
            case 4 -> {
                int power = readPower(input);
                String model = input.readNonBlank("Введите модель: ");
                int year = input.readInt("Введите год выпуска: ");

                Car target = Car.builder()
                        .power(power)
                        .model(model)
                        .year(year)
                        .build();
                input.markSuccess();
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

        // Задача 15: Предлагаем дописать результаты поиска в файл
        if (foundCars.size() > 0) {
            if (input.readYesNo("Дописать найденные автомобили в файл history.txt?")) {
                saver.appendCollection("history.txt", foundCars, "Результат поиска");
            }
        }
    }

    /**
     * Запрашивает мощность автомобиля и повторяет ввод, пока значение
     * не попадёт в допустимый диапазон.
     *
     * @param input источник консольного ввода
     * @return корректная мощность автомобиля
     * @throws AttemptsExceededException если достигнут предел неудачных попыток
     * @throws EndOfInputException если поток ввода завершён
     */
    private static int readPower(ConsoleInput input) {
        while (true) {
            int power = input.readInt("Введите мощность: ");

            if (power < 0) {
                input.reportError("Мощность не может быть отрицательной.");
                continue;
            }

            if (power < Limits.MIN_POWER || power > Limits.MAX_POWER) {
                input.reportError(
                        "Мощность должна быть в диапазоне от "
                                + Limits.MIN_POWER + " до " + Limits.MAX_POWER + "."
                );
                continue;
            }

            return power;
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
