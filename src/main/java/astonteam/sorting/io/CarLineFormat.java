package astonteam.sorting.io;

import astonteam.sorting.common.TextSanitizer;
import astonteam.sorting.model.Car;

import java.util.Objects;

/**
 * Преобразует автомобиль в строку и обратно в формате «мощность;модель;год».
 */
public final class CarLineFormat {

    /** Разделитель полей в строке. */
    public static final String DELIMITER = ";";
    /** Шаблон-подсказка формата строки. */
    public static final String PATTERN = "мощность" + DELIMITER + "модель" + DELIMITER + "год";

    /**
     * Утилитный конструктор, не предназначен для вызова.
     */
    private CarLineFormat() {
    }

    /**
     * Формирует строку представления автомобиля.
     *
     * @param car автомобиль для форматирования
     * @return строка в формате «мощность;модель;год»
     * @throws IllegalArgumentException если модель пуста или содержит недопустимые символы
     */
    public static String format(Car car) {
        Objects.requireNonNull(car, "car");
        String model = car.getModel();
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Модель автомобиля не должна быть пустой");
        }
        if (model.contains(DELIMITER) || TextSanitizer.hasControlChars(model)) {
            throw new IllegalArgumentException("Модель содержит недопустимые символы: \"" + model + "\"");
        }
        return car.getPower() + DELIMITER + model + DELIMITER + car.getYear();
    }


    /**
     * Разбирает строку и создаёт автомобиль.
     *
     * @param line строка в формате «мощность;модель;год»
     * @return созданный автомобиль
     * @throws IllegalArgumentException если строка пуста или содержит некорректные данные
     */
    public static  Car parse(String line) {
        Objects.requireNonNull(line, "line");
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Строка не должна быть пустой");
        }
        String[] parts = trimmed.split(DELIMITER, -1);

        if (parts.length > 3) {
            throw new IllegalArgumentException("Модель содержит недопустимый символ-разделитель '" + DELIMITER + "'");
        }
        if (parts.length < 3) {
            throw new IllegalArgumentException("Недостаточно данных. Ожидается 3 поля, разделенные '" + DELIMITER + "'");
        }

        int power;
        int year;

        try {
            power = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Мощность должна быть целым числом: '" + parts[0] + "'");
        }

        String model = parts[1].trim();
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Модель не должна быть пустой");
        }

        try {
            year = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Год должен быть целым числом: '" + parts[2] + "'");
        }

        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();

    }
}
