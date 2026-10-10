package astonteam.sorting.io;

import astonteam.sorting.common.TextSanitizer;
import astonteam.sorting.model.Car;

import java.util.Objects;

public final class CarLineFormat {

    public static final String DELIMITER = ";";
    public static final String PATTERN = "мощность" + DELIMITER + "модель" + DELIMITER + "год";

    private CarLineFormat() {
    }

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
}
