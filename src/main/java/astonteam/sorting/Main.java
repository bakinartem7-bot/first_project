package astonteam.sorting;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.AttemptsExceededException;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.console.Menu;
import astonteam.sorting.console.MenuItem;
import astonteam.sorting.console.SearchMenu;
import astonteam.sorting.console.SortingMenu;
import astonteam.sorting.fill.FileFiller;
import astonteam.sorting.fill.ManualFiller;
import astonteam.sorting.fill.RandomFiller;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.validation.CarValidator;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CarArrayList cars = new CarArrayList();
        CarValidator validator = new CarValidator();
        ResultSaver saver = new ResultSaver();

        RandomFiller randomFiller = new RandomFiller();
        try (ConsoleInput input = new ConsoleInput()) {
            Menu menu = new Menu("Главное меню", List.of(
                    new MenuItem(1, "Заполнить массив вручную"),
                    new MenuItem(2, "Заполнить массив случайно"),
                    new MenuItem(3, "Заполнить массив из файла"),
                    new MenuItem(4, "Сортировать коллекцию"),
                    new MenuItem(5, "Поиск автомобилей"),
                    new MenuItem(6, "Сохранить коллекцию в файл"),
                    new MenuItem(7, "Выход")
            ));
            boolean running = true;
            while (running) {
                try {
                    switch (menu.ask(input).id()) {
                        case 1 -> {
                            int size = input.readInt("Количество авто: ", 1, Limits.MAX_COLLECTION_SIZE);
                            cars = new ManualFiller(input, validator).fill(size);
                            System.out.println("Автомобилей введено: " + cars.size());
                        }
                        case 2 -> {
                            int size = input.readInt("Количество авто: ", 1, Limits.MAX_COLLECTION_SIZE);
                            cars = randomFiller.fill(size);
                            System.out.println("Автомобилей сгенерировано: " + cars.size());
                        }
                        case 3 -> {
                            String fileName = input.readNonBlank("Введите путь к файлу (например, cars.txt): ");
                            Path path = Path.of(fileName);
                            cars = new FileFiller().fill(path);
                            System.out.println("Автомобилей загружено: " + cars.size());
                        }
                        case 4 -> SortingMenu.sortCars(cars, input);
                        case 5 -> SearchMenu.searchCars(cars, input);
                        case 6 -> saver.saveCollection("cars.txt", cars);
                        case 7 -> running = false;
                        default -> System.out.println("Пункт в работе.");
                    }
                } catch (AttemptsExceededException | IllegalArgumentException | UncheckedIOException e) {
                    System.out.println("Ошибка: " + e.getMessage() + " Возврат в меню.");
                }
            }
        } catch (EndOfInputException e) {
            System.out.println("Ввод прерван, выход.");
        }
        System.out.println("Работа завершена.");
    }
}
