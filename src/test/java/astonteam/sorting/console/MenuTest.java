package astonteam.sorting.console;

import astonteam.sorting.TestSupport;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link Menu}: создание меню, валидация пунктов и выбор пункта пользователем.
 */
class MenuTest {

    /**
     * Первый пункт меню, используемый в тестах.
     */
    private static final MenuItem FIRST = new MenuItem(1, "Первый");
    /**
     * Второй пункт меню, используемый в тестах.
     */
    private static final MenuItem SECOND = new MenuItem(2, "Второй");

    /**
     * Проверяет, что пустой список пунктов приводит к IllegalArgumentException.
     */
    @Test
    void constructorRejectsEmptyItems() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("Меню", List.of()));
    }

    /**
     * Проверяет, что null среди пунктов меню приводит к IllegalArgumentException.
     */
    @Test
    void constructorRejectsNullItem() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("Меню", Arrays.asList(FIRST, null)));
    }

    /**
     * Проверяет, что дублирующиеся идентификаторы пунктов приводят к IllegalArgumentException.
     */
    @Test
    void constructorRejectsDuplicateIds() {
        assertThrows(IllegalArgumentException.class,
                () -> new Menu("Меню", List.of(new MenuItem(1, "a"), new MenuItem(1, "b"))));
    }

    /**
     * Проверяет, что пустой заголовок меню приводит к IllegalArgumentException.
     */
    @Test
    void constructorRejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("   ", List.of(FIRST)));
    }

    /**
     * Проверяет, что ввод существующего номера возвращает соответствующий пункт меню.
     */
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

    /**
     * Проверяет, что при неизвестном номере выводится сообщение и принимается следующий корректный ввод.
     */
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
