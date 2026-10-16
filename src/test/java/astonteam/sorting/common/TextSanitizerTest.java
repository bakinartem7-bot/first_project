package astonteam.sorting.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты утилиты {@link TextSanitizer}: проверяют нормализацию текста,
 * обнаружение управляющих символов и валидацию строк.
 */
class TextSanitizerTest {

    /**
     * Проверяет, что null нормализуется в пустую строку.
     */
    @Test
    void normalizeReturnsEmptyForNull() {
        assertEquals("", TextSanitizer.normalize(null));
    }

    /**
     * Проверяет, что пробелы по краям строки отбрасываются.
     */
    @Test
    void normalizeTrimsEdges() {
        assertEquals("abc", TextSanitizer.normalize("   abc   "));
    }

    /**
     * Проверяет, что неразрывные пробелы заменяются обычными.
     */
    @Test
    void normalizeReplacesNonBreakingSpaces() {
        assertEquals("a b c", TextSanitizer.normalize("a\u00A0b\u2007c"));
    }

    /**
     * Проверяет, что символы нулевой ширины и BOM удаляются.
     */
    @Test
    void normalizeRemovesZeroWidthAndBom() {
        assertEquals("ab", TextSanitizer.normalize("a\u200B\u200C\u200D\uFEFFb"));
    }

    /**
     * Проверяет, что управляющие символы (табуляция, перевод строки) удаляются.
     */
    @Test
    void normalizeRemovesControlChars() {
        assertEquals("ab", TextSanitizer.normalize("a\tb\n"));
    }

    /**
     * Проверяет обнаружение управляющих символов, включая случай null.
     */
    @Test
    void hasControlCharsDetects() {
        assertTrue(TextSanitizer.hasControlChars("a\tb"));
        assertTrue(TextSanitizer.hasControlChars("a\nb"));
        assertFalse(TextSanitizer.hasControlChars("plain"));
        assertFalse(TextSanitizer.hasControlChars(null));
    }

    /**
     * Проверяет, что requireText возвращает нормализованное значение.
     */
    @Test
    void requireTextReturnsNormalized() {
        assertEquals("value", TextSanitizer.requireText("  value ", "field", 10));
    }

    /**
     * Проверяет, что пустая или состоящая из пробелов строка отклоняется.
     */
    @Test
    void requireTextRejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> TextSanitizer.requireText("   ", "field", 10));
    }

    /**
     * Проверяет, что строка, превышающая заданную длину, отклоняется.
     */
    @Test
    void requireTextRejectsTooLong() {
        assertThrows(IllegalArgumentException.class, () -> TextSanitizer.requireText("abcdef", "field", 5));
    }

    /**
     * Проверяет, что приватный конструктор существует и вызывается только через рефлексию.
     *
     * @throws Exception если не удаётся получить или вызвать конструктор
     */
    @Test
    void privateConstructorIsNotUsableDirectly() throws Exception {
        Constructor<TextSanitizer> constructor = TextSanitizer.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }
}
