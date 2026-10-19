package astonteam.sorting;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.console.AttemptsExceededException;
import astonteam.sorting.console.CollectionMenu;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.CountMenu;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.console.FillMenu;
import astonteam.sorting.console.HistoryMenu;
import astonteam.sorting.console.Menu;
import astonteam.sorting.console.MenuItem;
import astonteam.sorting.console.SearchMenu;
import astonteam.sorting.console.SortingMenu;
import astonteam.sorting.fill.RandomFiller;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.search.CarCounter;
import astonteam.sorting.validation.CarValidator;

import java.io.UncheckedIOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CarArrayList cars = new CarArrayList();
        CarValidator validator = new CarValidator();
        ResultSaver saver = new ResultSaver();
        CarCounter counter = new CarCounter();

        RandomFiller randomFiller = new RandomFiller();
        try (ConsoleInput input = new ConsoleInput()) {
            Menu menu = new Menu("Главное меню", List.of(
                    new MenuItem(1, "Заполнить коллекцию"),
                    new MenuItem(2, "Показать текущую коллекцию"),
                    new MenuItem(3, "Сортировать коллекцию"),
                    new MenuItem(4, "Поиск автомобилей"),
                    new MenuItem(5, "Подсчитать вхождения автомобиля (многопоточно)"),
                    new MenuItem(6, "Показать историю операций"),
                    new MenuItem(7, "Сохранить коллекцию в файл (перезапись)"),
                    new MenuItem(8, "Выход")
            ));
            boolean running = true;
            while (running) {
                try {
                    switch (menu.ask(input).id()) {
                        case 1 -> {
                            cars = FillMenu.fillCars(cars, input, validator, randomFiller);
                        }
                        // Передаем saver в методы
                        case 2 -> CollectionMenu.showCollection(cars);
                        case 3 -> SortingMenu.sortCars(cars, input, saver);
                        case 4 -> SearchMenu.searchCars(cars, input, saver);
                        case 5 -> CountMenu.countCars(cars, input, counter);
                        case 6 -> HistoryMenu.showHistory(saver);
                        case 7 -> saver.saveCollection("cars.txt", cars);
                        case 8 -> running = false;
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
