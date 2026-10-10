package astonteam.sorting.common;

public final class TextSanitizer {

    private TextSanitizer() {
    }

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

    public static boolean hasControlChars(String value) {
        return value != null && value.chars().anyMatch(Character::isISOControl);
    }

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
