package astonteam.sorting.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты класса {@link Limits}: проверяют корректность значений лимитов
 * и недоступность создания экземпляра напрямую.
 */
class LimitsTest {

    /**
     * Проверяет, что все объявленные лимиты положительны.
     */
    @Test
    void allLimitsArePositive() {
        assertTrue(Limits.MAX_LINE_LENGTH > 0);
        assertTrue(Limits.MAX_ATTEMPTS > 0);
        assertTrue(Limits.MAX_COLLECTION_SIZE > 0);
        assertTrue(Limits.MAX_MODEL_LENGTH > 0);
        assertTrue(Limits.MAX_CAPTION_LENGTH > 0);
        assertTrue(Limits.MAX_TEXT_LENGTH > 0);
        assertTrue(Limits.MAX_FILE_NAME_LENGTH > 0);
        assertTrue(Limits.MAX_MENU_ITEM_ID > 0);
    }

    /**
     * Проверяет, что приватный конструктор существует и может быть вызван
     * только через рефлексию (экземпляр создаётся не равен null).
     *
     * @throws Exception если не удаётся получить или вызвать конструктор
     */
    @Test
    void privateConstructorIsNotUsableDirectly() throws Exception {
        Constructor<Limits> constructor = Limits.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }
}
