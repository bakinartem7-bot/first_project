package astonteam.sorting.console;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Menu {

    private static final String SEPARATOR = "-".repeat(46);

    private final String title;
    private final List<MenuItem> items;

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

    public void print() {
        System.out.println();
        System.out.println("=== " + title + " ===");
        for (MenuItem item : items) {
            System.out.printf(" %d) %s%n", item.id(), item.caption());
        }
        System.out.println(SEPARATOR);
    }
}
