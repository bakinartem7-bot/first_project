package astonteam.sorting.console;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.fill.FileFiller;
import astonteam.sorting.fill.ManualFiller;
import astonteam.sorting.fill.RandomFiller;
import astonteam.sorting.validation.CarValidator;

import java.nio.file.Path;
import java.util.List;

/**
 * Консольное меню для выбора способа заполнения коллекции автомобилей.
 */
public class FillMenu {

    /**
     * Позволяет пользователю выбрать способ заполнения: вручную, случайно или из файла.
     * Возвращает новую заполненную коллекцию либо исходную, если выбран возврат в меню.
     *
     * @param currentCars текущая коллекция автомобилей
     * @param input источник пользовательского ввода
     * @param validator валидатор для ручного ввода
     * @param randomFiller генератор случайных автомобилей
     * @return заполненная коллекция автомобилей
     */
    public static CarArrayList fillCars(
            CarArrayList currentCars,
            ConsoleInput input,
            CarValidator validator,
            RandomFiller randomFiller) {

        Menu menu = new Menu("Выберите способ заполнения", List.of(
                new MenuItem(1, "Заполнить вручную"),
                new MenuItem(2, "Заполнить случайно"),
                new MenuItem(3, "Заполнить из файла"),
                new MenuItem(4, "Назад в главное меню")
        ));

        int choice = menu.ask(input).id();

        switch (choice) {
            case 1 -> {
                int size = input.readInt("Количество авто: ", 1, Limits.MAX_COLLECTION_SIZE);
                CarArrayList newCars = new ManualFiller(input, validator).fill(size);
                System.out.println("Автомобилей введено: " + newCars.size());
                return newCars;
            }
            case 2 -> {
                int size = input.readInt("Количество авто: ", 1, Limits.MAX_COLLECTION_SIZE);
                CarArrayList newCars = randomFiller.fill(size);
                System.out.println("Автомобилей сгенерировано: " + newCars.size());
                return newCars;
            }
            case 3 -> {
                String fileName = input.readNonBlank("Введите путь к файлу (например, cars.txt): ");
                Path path = Path.of(fileName);
                CarArrayList newCars = new FileFiller().fill(path);
                System.out.println("Автомобилей загружено: " + newCars.size());
                return newCars;
            }
            case 4 -> {
                // Возвращаем текущую коллекцию без изменений
                return currentCars;
            }
            default -> throw new IllegalArgumentException("Неизвестный способ заполнения: " + choice);
        }
    }
}




