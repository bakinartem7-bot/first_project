package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

    private static final MenuItem FIRST = new MenuItem(1, "Первый");
    private static final MenuItem SECOND = new MenuItem(2, "Второй");

    @Test
    void constructorRejectsEmptyItems() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("Меню", List.of()));
    }

    @Test
    void constructorRejectsNullItem() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("Меню", Arrays.asList(FIRST, null)));
    }

    @Test
    void constructorRejectsDuplicateIds() {
        assertThrows(IllegalArgumentException.class,
                () -> new Menu("Меню", List.of(new MenuItem(1, "a"), new MenuItem(1, "b"))));
    }

    @Test
    void constructorRejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("   ", List.of(FIRST)));
    }

    @Test
    void askReturnsMatchingItem() {
        TestSupport.Outcome outcome = TestSupport.run("2\n", () -> {
            ConsoleInput input = new ConsoleInput();
            Menu menu = new Menu("Меню", List.of(FIRST, SECOND));
            assertEquals(2, menu.ask(input).id());
            input.close();
        });
        assertNull(outcome.error());
        assertTrue(outcome.output().contains("=== Меню ==="));
    }

    @Test
    void askReportsUnknownThenReturnsValidItem() {
        TestSupport.Outcome outcome = TestSupport.run("9\n1\n", () -> {
            ConsoleInput input = new ConsoleInput();
            Menu menu = new Menu("Меню", List.of(FIRST, SECOND));
            assertEquals(1, menu.ask(input).id());
            input.close();
        });
        assertTrue(outcome.output().contains("Пункта меню с номером 9 нет"));
    }
}
