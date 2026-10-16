package astonteam.sorting.common;

/**
 * Утилиты нормализации и проверки текстовых значений.
 */
public final class TextSanitizer {

    /**
     * Приватный конструктор для запрета создания экземпляров.
     */
    private TextSanitizer() {
    }

    /**
     * Нормализует строку: заменяет спецпробелы, убирает управляющие символы и обрезает края.
     *
     * @param value исходное значение
     * @return нормализованная строка, либо пустая строка для {@code null}
     */
    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == '\u00A0' || ch == '\u2007' || ch == '\u202F') {
                builder.append(' ');
            } else if (ch != '\u200B' && ch != '\u200C' && ch != '\u200D' && ch != '\uFEFF'
                    && !Character.isISOControl(ch)) {
                builder.append(ch);
            }
        }
        return builder.toString().trim();
    }

    /**
     * Проверяет наличие управляющих символов в строке.
     *
     * @param value проверяемое значение
     * @return {@code true}, если строка содержит управляющие символы
     */
    public static boolean hasControlChars(String value) {
        return value != null && value.chars().anyMatch(Character::isISOControl);
    }

    /**
     * Нормализует текст и проверяет его на пустоту и длину.
     *
     * @param value     исходное значение
     * @param fieldName имя поля для сообщения об ошибке
     * @param maxLength максимально допустимая длина
     * @return нормализованный текст
     * @throws IllegalArgumentException если текст пуст или длиннее {@code maxLength}
     */
    public static String requireText(String value, String fieldName, int maxLength) {
        String normalized = normalize(value);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не должен быть пустым");
        }
        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " длиннее " + maxLength + " символов");
        }
        return normalized;
    }
}
