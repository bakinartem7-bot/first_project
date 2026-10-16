package astonteam.sorting.console;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для {@link MenuItem}: хранение и валидация идентификатора и заголовка пункта меню.
 */
class MenuItemTest {

    /**
     * Проверяет, что пункт меню сохраняет идентификатор и заголовок.
     */
    @Test
    void storesIdAndCaption() {
        MenuItem item = new MenuItem(3, "Заголовок");
        assertEquals(3, item.id());
        assertEquals("Заголовок", item.caption());
    }

    /**
     * Проверяет, что заголовок пункта меню обрезается по краям.
     */
    @Test
    void trimsCaption() {
        assertEquals("Заголовок", new MenuItem(1, "  Заголовок  ").caption());
    }

    /**
     * Проверяет, что неположительный идентификатор приводит к IllegalArgumentException.
     */
    @Test
    void rejectsNonPositiveId() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(0, "a"));
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(-1, "a"));
    }

    /**
     * Проверяет, что слишком большой идентификатор приводит к IllegalArgumentException.
     */
    @Test
    void rejectsTooBigId() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(1000, "a"));
    }

    /**
     * Проверяет, что пустой заголовок приводит к IllegalArgumentException.
     */
    @Test
    void rejectsBlankCaption() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(1, "   "));
    }
}
