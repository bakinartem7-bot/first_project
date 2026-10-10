package astonteam.sorting.console;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuItemTest {

    @Test
    void storesIdAndCaption() {
        MenuItem item = new MenuItem(3, "Заголовок");
        assertEquals(3, item.id());
        assertEquals("Заголовок", item.caption());
    }

    @Test
    void trimsCaption() {
        assertEquals("Заголовок", new MenuItem(1, "  Заголовок  ").caption());
    }

    @Test
    void rejectsNonPositiveId() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(0, "a"));
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(-1, "a"));
    }

    @Test
    void rejectsTooBigId() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(1000, "a"));
    }

    @Test
    void rejectsBlankCaption() {
        assertThrows(IllegalArgumentException.class, () -> new MenuItem(1, "   "));
    }
}
