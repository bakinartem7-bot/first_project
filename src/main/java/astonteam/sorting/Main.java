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

/**
 * Точка входа консольного приложения: главное меню и маршрутизация
 * по подменю (заполнение, показ, сортировка, поиск, подсчёт, история, сохранение).
 *
 * <p>Все ошибки ввода и файловых операций перехватываются здесь же,
 * чтобы после сбоя пользователь всегда возвращался в главное меню,
 * а коллекция и файлы результатов оставались неповреждёнными.</p>
 */
public class Main {

    /**
     * Приватный конструктор для запрета создания экземпляров класса с точкой входа.
     */
    private Main() {
    }

    /**
     * Имя файла коллекции в каталоге результатов (перезаписывается целиком).
     */
    private static final String COLLECTION_FILE = "cars.txt";

    /**
     * Запускает приложение: выводит главное меню и обрабатывает выбранные
     * пользователем пункты, пока не будет выбран выход или не закончится ввод.
     *
     * @param args аргументы командной строки (не используются)
     */
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
                        case 1 -> cars = FillMenu.fillCars(cars, input, validator, randomFiller);
                        case 2 -> CollectionMenu.showCollection(cars);
                        case 3 -> SortingMenu.sortCars(cars, input, saver);
                        case 4 -> SearchMenu.searchCars(cars, input, saver);
                        case 5 -> CountMenu.countCars(cars, input, counter);
                        case 6 -> HistoryMenu.showHistory(saver);
                        case 7 -> saveCollection(cars, input, saver);
                        case 8 -> {
                            offerSaveBeforeExit(cars, input, saver);
                            running = false;
                        }
                        default -> System.out.println("Пункт в работе.");
                    }
                } catch (AttemptsExceededException | IllegalArgumentException | UncheckedIOException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    System.out.println("Возврат в меню.");
                }
            }
        } catch (EndOfInputException e) {
            System.out.println("Ввод прерван, выход.");
        }
        System.out.println("Работа завершена.");
    }

    /**
     * Сохраняет коллекцию в файл результатов после подтверждения перезаписи.
     * Пустая коллекция не сохраняется: это почти всегда случайное действие,
     * которое уничтожило бы уже сохранённые данные.
     *
     * @param cars   коллекция автомобилей
     * @param input  источник ввода для подтверждения
     * @param saver  сохранитель файлов результатов
     */
    private static void saveCollection(CarArrayList cars, ConsoleInput input, ResultSaver saver) {
        if (cars.size() == 0) {
            System.out.println("Коллекция пуста - сохранение отменено, файл не изменён.");
            return;
        }
        if (input.readYesNo("Перезаписать файл " + COLLECTION_FILE)) {
            saver.saveCollection(COLLECTION_FILE, cars);
        } else {
            System.out.println("Сохранение отменено.");
        }
    }

    /**
     * Перед завершением работы предлагает сохранить непустую коллекцию.
     * Пустая коллекция не сохраняется и вопрос не задаётся:
     * сохранять нечего, а вопрос сбивает с толку.
     *
     * @param cars   рабочая коллекция автомобилей
     * @param input  источник ввода для подтверждения
     * @param saver  сохранитель файлов результатов
     */
    private static void offerSaveBeforeExit(CarArrayList cars, ConsoleInput input, ResultSaver saver) {
        if (cars.size() == 0) {
            return;
        }
        try {
            if (input.readYesNo("Сохранить коллекцию перед выходом")) {
                saver.saveCollection(COLLECTION_FILE, cars);
            }
        } catch (UncheckedIOException e) {
            System.out.println("Не удалось сохранить коллекцию: " + e.getMessage());
        }
    }
}
