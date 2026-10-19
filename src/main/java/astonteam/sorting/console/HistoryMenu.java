package astonteam.sorting.console;

import astonteam.sorting.io.ResultSaver;

import java.util.List;

/**
 * Консольный сценарий просмотра истории операций из файла history.txt.
 */
public final class HistoryMenu {

    /** Имя файла с историей операций. */
    private static final String HISTORY_FILE = "history.txt";

    private HistoryMenu() {
    }

    /**
     * Выводит в консоль содержимое истории операций.
     *
     * @param saver сохранитель, из каталога результатов которого читается история
     */
    public static void showHistory(ResultSaver saver) {
        List<String> lines = saver.readLines(HISTORY_FILE);

        System.out.println("История операций (history.txt):");
        if (lines.isEmpty()) {
            System.out.println("История пуста.");
            return;
        }
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
