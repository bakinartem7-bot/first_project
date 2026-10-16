package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import astonteam.sorting.search.CarSearcher;

import java.util.List;

public class SearchMenu {
    public static void searchCars(CarArrayList cars, ConsoleInput input) {
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
                int power = input.readInt("Введите мощность: ");
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
                int power = input.readInt("Введите мощность: ");
                String model = input.readNonBlank("Введите модель: ");
                int year = input.readInt("Введите год выпуска: ");

                Car target = Car.builder()
                        .power(power)
                        .model(model)
                        .year(year)
                        .build();

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
    }

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
