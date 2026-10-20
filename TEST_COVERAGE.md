# Тестовое покрытие (JUnit 5)

Запуск: `gradlew test` (Windows) или `./gradlew test`. Итог: **29 тестовых классов / 270 тестов / 0 падений / 0 ошибок / 0 пропусков**.

## Тестовые классы и число тестов

| Область | Класс | Тестов | Что проверяется |
|---|---|--------|---|
| Валидация | `validation.CarValidatorTest` | 13     | границы мощности/года, длина модели, пустая/null модель, символ `;`, управляющие символы, текст ошибок |
| Валидация | `validation.ValidationResultTest` | 3      | фабрики успешного/неуспешного результата, `isValid` |
| Модель | `model.CarTest` | 11     | builder, `equals`/`hashCode`, читаемый `toString`, невалидные параметры |
| Коллекция | `collection.CarArrayListTest` | 13     | add/get/remove, границы индексов, итерация, `size`, null-аргументы |
| Формат строки | `io.CarLineFormatTest` | 15     | round-trip, UTF-8/кириллица, неверный разделитель/число полей/значения |
| Хранение файлов | `io.ResultSaverTest` | 16     | запись и атомарная перезапись, APPEND без потери строк, безопасные имена, отчёт, `readLines` |
| Заполнение | `fill.ManualFillerTest` | 10     | ввод вручную, повтор при ошибке, выход по лимиту попыток |
| Заполнение | `fill.FileFillerTest` | 8      | чтение UTF-8/кириллицы, неверный разделитель, длинная модель, null/отсутствие файла |
| Заполнение | `fill.RandomFillerTest` | 4      | размер выборки, валидность сгенерированных машин |
| Сортировки | `sort.InsertionSortTest` | 17     | порядок по полям, статистика обменов |
| Сортировки | `sort.MergeSortTest` | 21     | порядок по полям, статистика обменов |
| Сортировки | `sort.EvenPowerInsertionSortTest` | 9      | сортировка только чётной мощности (доп. задание) |
| Сортировки | `sort.EvenPowerMergeSortTest` | 9      | сортировка только чётной мощности (доп. задание) |
| Сортировки | `sort.CarComparatorsTest` | 6      | компараторы по мощности/модели/году |
| Поиск | `search.CarSearcherTest` | 9      | 4 способа поиска, регистр, отсутствие совпадений |
| Поиск | `search.CarCounterTest` | 6      | многопоточный подсчёт (Stream/parallel), границы, null-аргументы |
| Статистика | `stats.SortingStatisticsTest` | 7      | агрегация времени/обменов |
| Общие | `common.TextSanitizerTest` | 10     | нормализация/опасные управляющие символы |
| Общие | `common.LimitsTest` | 2      | константы лимитов |
| Меню | `console.MenuTest` | 6      | регистрация пунктов, валидация, выход |
| Меню | `console.MenuItemTest` | 5      | корректность пункта меню |
| Ввод | `console.ConsoleInputTest` | 12     | построчный/целочисленный ввод, границы, `readYesNo` |
| Ввод | `console.ConsoleExceptionsTest` | 2      | исключения `AttemptsExceededException`/`EndOfInputException` |
| Ввод | `console.CarInputReaderTest` | 9 | ввод и валидация полей автомобиля, повтор ошибочного поля, управление счётчиком попыток |
| Меню | `console.SortingMenuTest` | 8      | диалог сортировки, дописывание истории (`@TempDir`) |
| Меню | `console.SearchMenuTest` | 9      | диалог поиска, дописывание истории (`@TempDir`) |
| Меню | `console.CountMenuTest` | 7 | многопоточный подсчёт через меню, отсутствие совпадений и валидация искомого автомобиля |
| Интеграция | `MainTest` | 8      | главное меню: заполнение/сохранение/отказ от перезаписи/выход (`@TempDir`) |
| Интеграция | `MenuScenarioTest` | 15     | полный сквозной сценарий: заполнение → сортировка → поиск → подсчёт → показ коллекции → **показ истории** → сохранение → выход |

## Покрытие функции «просмотр истории»

- `HistoryMenu` (показ `results/history.txt`) и `ResultSaver.readLines` проверяются в `MenuScenarioTest`:
  - `historyOptionAfterAppendShowsHistory` — после дописывания сортировки/поиска пункт «Показать историю» выводит записи;
  - `historyOptionWithoutHistoryReportsEmpty` — при пустом/отсутствующем файле выводится сообщение о пустой истории.
- Дописывание APPEND проверяется в `io.ResultSaverTest` и `console.SortingMenuTest`/`console.SearchMenuTest`.

## Ключевые гарантии целостности данных (покрыты тестами)

- запись файлов **атомарна** (temp + `ATOMIC_MOVE`), остаточный `.tmp` удаляется в `finally`;
- в тестах каталог результатов перенаправляется во **временный** (`@TempDir` + `astonteam.results.dir`), реальный `results/` не удаляется и не перезаписывается;
- модель с `;` и управляющие символы **отклоняются при вводе**, а не при сохранении;
- сохранение пустой коллекции **запрещено**, перезапись `cars.txt` требует подтверждения пользователя;
- дописывание `history.txt` (APPEND) **не теряет** уже записанные строки.

## Как посмотреть результаты прогона

- HTML-отчёт JUnit: `build/reports/tests/test/index.html` (после `gradlew test`);
- XML-результаты: `build/test-results/test/TEST-*.xml`;
- документация Javadoc: `gradlew javadoc` (собирается без предупреждений).
