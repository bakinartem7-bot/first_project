package astonteam.sorting;

import astonteam.sorting.collection.CarList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.AttemptsExceededException;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.console.Menu;
import astonteam.sorting.console.MenuItem;
import astonteam.sorting.fill.ManualFiller;
import astonteam.sorting.io.ResultSaver;
import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;

import java.io.UncheckedIOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Car> cars = new CarList();
        CarValidator validator = new CarValidator();
        ResultSaver saver = new ResultSaver();

        try (ConsoleInput input = new ConsoleInput()) {
            Menu menu = new Menu("Главное меню", List.of(
                    new MenuItem(1, "Заполнить массив вручную"),
                    new MenuItem(2, "Сохранить коллекцию в файл"),
                    new MenuItem(3, "Выход")));
            boolean running = true;
            while (running) {
                try {
                    switch (menu.ask(input).id()) {
                        case 1 -> {
                            int size = input.readInt("Количество авто: ", 1, Limits.MAX_COLLECTION_SIZE);
                            cars = new ManualFiller(input, validator).fill(size);
                            System.out.println("Автомобилей введено: " + cars.size());
                        }
                        case 2 -> saver.saveCollection("cars.txt", cars);
                        case 3 -> running = false;
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
