package astonteam.sorting;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.AttemptsExceededException;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.console.FillMenu;
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

/**
 * Главный класс приложения: запускает консольное меню работы
 * с коллекцией автомобилей (заполнение, сортировка, поиск, сохранение).
 */
public class Main {

    /**
     * Точка входа: выводит главное меню и обрабатывает команды пользователя
     * до выбора пункта «Выход» или завершения ввода.
     *
     * @param args аргументы командной строки (не используются)
     */
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
                    new MenuItem(4, "Сохранить коллекцию в файл"),
                    new MenuItem(5, "Выход")
            ));
            boolean running = true;
            while (running) {
                try {
                    switch (menu.ask(input).id()) {
                        case 1 -> {
                            cars = FillMenu.fillCars(cars, input, validator, randomFiller);
                        }
                        case 2 -> SortingMenu.sortCars(cars, input);
                        case 3 -> SearchMenu.searchCars(cars, input);
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
