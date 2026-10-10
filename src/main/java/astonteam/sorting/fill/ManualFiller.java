package astonteam.sorting.fill;

import astonteam.sorting.collection.CarList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;
import astonteam.sorting.validation.ValidationResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ManualFiller {

    private static final String POWER_LABEL = "  Мощность (л.с.): ";
    private static final String MODEL_LABEL = "  Модель: ";
    private static final String YEAR_LABEL = "  Год производства: ";

    private final ConsoleInput input;
    private final CarValidator validator;

    public ManualFiller(ConsoleInput input, CarValidator validator) {
        this.input = Objects.requireNonNull(input, "input");
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    public List<Car> fill(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер массива должен быть больше нуля: " + size);
        }
        if (size > Limits.MAX_COLLECTION_SIZE) {
            throw new IllegalArgumentException("Максимальный размер массива: " + Limits.MAX_COLLECTION_SIZE);
        }
        System.out.printf("Введите %d автомобилей, заполняя поля по одному:%n", size);
        return IntStream.rangeClosed(1, size)
                .mapToObj(this::readCar)
                .collect(Collectors.toCollection(CarList::new));
    }

    private Car readCar(int index) {
        while (true) {
            System.out.printf("Автомобиль %d:%n", index);
            int power = readIntField(POWER_LABEL, "мощность");
            String model = readModelField(MODEL_LABEL);
            int year = readIntField(YEAR_LABEL, "год производства");
            Car car = validateAndBuild(power, model, year);
            if (car != null) {
                input.markSuccess();
                return car;
            }
            System.out.println("Повторите ввод автомобиля.");
        }
    }

    private int readIntField(String label, String fieldName) {
        while (true) {
            String line = input.readLine(label);
            if (line.isEmpty()) {
                input.reportError("Поле \"" + fieldName + "\" не должно быть пустым.");
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                input.reportError("Поле \"" + fieldName + "\" должно быть целым числом, получено \"" + line + "\".");
            }
        }
    }

    private String readModelField(String label) {
        while (true) {
            String line = input.readLine(label);
            if (line.isEmpty()) {
                input.reportError("Модель не должна быть пустой.");
                continue;
            }
            if (line.length() > Limits.MAX_MODEL_LENGTH) {
                input.reportError("Модель длиннее " + Limits.MAX_MODEL_LENGTH + " символов.");
                continue;
            }
            return line;
        }
    }

    private Car validateAndBuild(int power, String model, int year) {
        ValidationResult result;
        try {
            result = validator.validate(power, model, year);
        } catch (RuntimeException e) {
            input.reportError("Ошибка валидации: " + e.getMessage());
            return null;
        }
        if (result == null) {
            input.reportError("Валидатор вернул пустой результат.");
            return null;
        }
        if (!result.isValid()) {
            input.reportError("Данные отклонены: " + result.message());
            return null;
        }
        try {
            return Car.builder()
                    .power(power)
                    .model(model)
                    .year(year)
                    .build();
        } catch (RuntimeException e) {
            input.reportError("Не удалось создать автомобиль: " + e.getMessage());
            return null;
        }
    }
}
