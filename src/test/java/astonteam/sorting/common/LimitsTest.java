package astonteam.sorting.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LimitsTest {

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

    @Test
    void privateConstructorIsNotUsableDirectly() throws Exception {
        Constructor<Limits> constructor = Limits.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }
}
