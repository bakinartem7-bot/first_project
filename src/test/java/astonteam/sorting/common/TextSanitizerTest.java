package astonteam.sorting.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextSanitizerTest {

    @Test
    void normalizeReturnsEmptyForNull() {
        assertEquals("", TextSanitizer.normalize(null));
    }

    @Test
    void normalizeTrimsEdges() {
        assertEquals("abc", TextSanitizer.normalize("   abc   "));
    }

    @Test
    void normalizeReplacesNonBreakingSpaces() {
        assertEquals("a b c", TextSanitizer.normalize("a\u00A0b\u2007c"));
    }

    @Test
    void normalizeRemovesZeroWidthAndBom() {
        assertEquals("ab", TextSanitizer.normalize("a\u200B\u200C\u200D\uFEFFb"));
    }

    @Test
    void normalizeRemovesControlChars() {
        assertEquals("ab", TextSanitizer.normalize("a\tb\n"));
    }

    @Test
    void hasControlCharsDetects() {
        assertTrue(TextSanitizer.hasControlChars("a\tb"));
        assertTrue(TextSanitizer.hasControlChars("a\nb"));
        assertFalse(TextSanitizer.hasControlChars("plain"));
        assertFalse(TextSanitizer.hasControlChars(null));
    }

    @Test
    void requireTextReturnsNormalized() {
        assertEquals("value", TextSanitizer.requireText("  value ", "field", 10));
    }

    @Test
    void requireTextRejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> TextSanitizer.requireText("   ", "field", 10));
    }

    @Test
    void requireTextRejectsTooLong() {
        assertThrows(IllegalArgumentException.class, () -> TextSanitizer.requireText("abcdef", "field", 5));
    }

    @Test
    void privateConstructorIsNotUsableDirectly() throws Exception {
        Constructor<TextSanitizer> constructor = TextSanitizer.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }
}
