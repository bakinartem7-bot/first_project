package astonteam.sorting.console;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Консольное меню: выводит заголовок со списком пунктов и запрашивает у пользователя выбор одного из них.
 */
public final class Menu {

    /** Разделитель, отображаемый под списком пунктов меню. */
    private static final String SEPARATOR = "-".repeat(46);

    /** Заголовок меню. */
    private final String title;

    /** Неизменяемый список пунктов меню. */
    private final List<MenuItem> items;

    /**
     * Создаёт меню с заданным заголовком и пунктами.
     *
     * @param title заголовок меню
     * @param items список пунктов меню; не должен быть пустым и содержать null и дубликаты номеров
     * @throws IllegalArgumentException если заголовок пуст, список пуст, содержит null либо дублирующиеся номера
     * @throws NullPointerException      если {@code items} равен null
     */
    public Menu(String title, List<MenuItem> items) {
        this.title = TextSanitizer.requireText(title, "title", Limits.MAX_CAPTION_LENGTH);
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Меню должно содержать хотя бы один пункт");
        }
        Set<Integer> ids = new HashSet<>();
        for (MenuItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException("Пункт меню не может быть null");
            }
            if (!ids.add(item.id())) {
                throw new IllegalArgumentException("Дублирующийся номер пункта: " + item.id());
            }
        }
        this.items = List.copyOf(items);
    }

    /**
     * Выводит меню и повторяет запрос выбора, пока пользователь не укажет существующий номер пункта.
     *
     * @param input источник пользовательского ввода
     * @return выбранный пункт меню
     * @throws AttemptsExceededException если превышено допустимое число ошибок ввода
     * @throws EndOfInputException       если поток ввода завершён
     * @throws NullPointerException      если {@code input} равен null
     */
    public MenuItem ask(ConsoleInput input) {
        Objects.requireNonNull(input, "input");
        print();
        while (true) {
            int choice = input.readInt("Ваш выбор: ");
            for (MenuItem item : items) {
                if (item.id() == choice) {
                    input.markSuccess();
                    return item;
                }
            }
            input.reportError("Пункта меню с номером " + choice + " нет.");
        }
    }

    /** Выводит заголовок и пронумерованный список пунктов меню в консоль. */
    public void print() {
        System.out.println();
        System.out.println("=== " + title + " ===");
        for (MenuItem item : items) {
            System.out.printf(" %d) %s%n", item.id(), item.caption());
        }
        System.out.println(SEPARATOR);
    }
}
