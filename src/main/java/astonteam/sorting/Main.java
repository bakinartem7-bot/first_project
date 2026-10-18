package astonteam.sorting;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.console.AttemptsExceededException;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.console.FillMenu;
import astonteam.sorting.console.Menu;
import astonteam.sorting.console.MenuItem;
import astonteam.sorting.console.SearchMenu;
import astonteam.sorting.console.SortingMenu;
import astonteam.sorting.fill.RandomFiller;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.validation.CarValidator;

import java.io.UncheckedIOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CarArrayList cars = new CarArrayList();
        CarValidator validator = new CarValidator();
        ResultSaver saver = new ResultSaver();

        RandomFiller randomFiller = new RandomFiller();
        try (ConsoleInput input = new ConsoleInput()) {
            Menu menu = new Menu("Главное меню", List.of(
                    new MenuItem(1, "Заполнить коллекцию"),
                    new MenuItem(2, "Сортировать коллекцию"),
                    new MenuItem(3, "Поиск автомобилей"),
                    new MenuItem(4, "Сохранить коллекцию в файл (перезапись)"),
                    new MenuItem(5, "Выход")
            ));
            boolean running = true;
            while (running) {
                try {
                    switch (menu.ask(input).id()) {
                        case 1 -> {
                            cars = FillMenu.fillCars(cars, input, validator, randomFiller);
                        }
                        // Передаем saver в методы
                        case 2 -> SortingMenu.sortCars(cars, input, saver);
                        case 3 -> SearchMenu.searchCars(cars, input, saver);
                        case 4 -> saver.saveCollection("cars.txt", cars);
                        case 5 -> running = false;
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
