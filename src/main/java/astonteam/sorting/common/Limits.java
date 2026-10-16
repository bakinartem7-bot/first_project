package astonteam.sorting.common;

/**
 * Набор констант с ограничениями размеров и диапазонов значений.
 */
public final class Limits {

    /** Максимальная длина строки ввода. */
    public static final int MAX_LINE_LENGTH = 256;
    /** Максимальное количество попыток ввода. */
    public static final int MAX_ATTEMPTS = 5;
    /** Максимальный размер коллекции. */
    public static final int MAX_COLLECTION_SIZE = 10_000;
    /** Максимальная длина названия модели. */
    public static final int MAX_MODEL_LENGTH = 50;
    /** Максимальная длина подписи. */
    public static final int MAX_CAPTION_LENGTH = 100;
    /** Максимальная длина текста. */
    public static final int MAX_TEXT_LENGTH = 50;
    /** Максимальная длина имени файла. */
    public static final int MAX_FILE_NAME_LENGTH = 64;
    /** Максимальный идентификатор пункта меню. */
    public static final int MAX_MENU_ITEM_ID = 999;

    /** Минимальная мощность двигателя. */
    public static final int MIN_POWER = 1;
    /** Максимальная мощность двигателя. */
    public static final int MAX_POWER = 2000;
    /** Минимальный год выпуска. */
    public static final int MIN_YEAR = 1886;
    /** Максимальный год выпуска. */
    public static final int MAX_YEAR = 2026;

    /**
     * Приватный конструктор для запрета создания экземпляров.
     */
    private Limits() {
    }
}
