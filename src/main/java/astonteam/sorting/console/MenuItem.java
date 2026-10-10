package astonteam.sorting.console;

import astonteam.sorting.common.Limits;
import astonteam.sorting.common.TextSanitizer;

public record MenuItem(int id, String caption) {

    public MenuItem {
        if (id <= 0 || id > Limits.MAX_MENU_ITEM_ID) {
            throw new IllegalArgumentException("Номер пункта вне диапазона 1.." + Limits.MAX_MENU_ITEM_ID + ": " + id);
        }
        caption = TextSanitizer.requireText(caption, "caption", Limits.MAX_CAPTION_LENGTH);
    }
}
