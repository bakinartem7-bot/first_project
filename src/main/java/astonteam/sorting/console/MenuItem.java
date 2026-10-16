package astonteam.sorting.console;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

/**
 * Пункт консольного меню: номер и подписываемый ему текст.
 *
 * @param id      номер пункта в диапазоне 1..{@link Limits#MAX_MENU_ITEM_ID}
 * @param caption текст пункта меню
 */
public record MenuItem(int id, String caption) {

    /**
     * Валидирует поля пункта меню.
     *
     * @throws IllegalArgumentException если номер вне допустимого диапазона или подпись пуста
     */
    public MenuItem {
        if (id <= 0 || id > Limits.MAX_MENU_ITEM_ID) {
            throw new IllegalArgumentException("Номер пункта вне диапазона 1.." + Limits.MAX_MENU_ITEM_ID + ": " + id);
        }
        caption = TextSanitizer.requireText(caption, "caption", Limits.MAX_CAPTION_LENGTH);
    }
}
