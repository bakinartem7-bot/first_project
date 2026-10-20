package astonteam.sorting.fill;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;
import astonteam.sorting.validation.ValidationResult;
import astonteam.sorting.console.AttemptsExceededException;
import astonteam.sorting.console.EndOfInputException;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Заполняет коллекцию автомобилей данными, вводимыми пользователем с консоли.
 */
public final class ManualFiller {

    /** Подпись поля запроса мощности. */
    private static final String POWER_LABEL = "  Мощность (л.с.): ";
    /** Подпись поля запроса модели. */
    private static final String MODEL_LABEL = "  Модель: ";
    /** Подпись поля запроса года производства. */
    private static final String YEAR_LABEL = "  Год производства: ";

    /** Источник ввода из консоли. */
    private final ConsoleInput input;
    /** Валидатор введённых данных автомобиля. */
    private final CarValidator validator;

    /**
     * Создаёт заполнитель.
     *
     * @param input    источник консольного ввода
     * @param validator валидатор автомобилей
     */
    public ManualFiller(ConsoleInput input, CarValidator validator) {
        this.input = Objects.requireNonNull(input, "input");
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    /**
     * Запрашивает у пользователя указанное число автомобилей
     * и заполняет коллекцию через последовательный stream.
     *
     * @param size количество автомобилей для ввода
     * @return заполненная коллекция автомобилей
     * @throws IllegalArgumentException если размер не положителен или превышает предел
     */
    public CarArrayList fill(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер массива должен быть больше нуля: " + size);
        }
        if (size > Limits.MAX_COLLECTION_SIZE) {
            throw new IllegalArgumentException("Максимальный размер массива: " + Limits.MAX_COLLECTION_SIZE);
        }
        System.out.printf("Введите %d автомобилей, заполняя поля по одному:%n", size);
        CarArrayList cars = new CarArrayList();
        IntStream.rangeClosed(1, size)
                .mapToObj(this::readCar)
                .forEach(cars::add);
        return cars;
    }

    /**
     * Считывает один автомобиль, повторяя ввод до успешной валидации.
     *
     * @param index порядковый номер автомобиля для отображения
     * @return созданный автомобиль
     */
    private Car readCar(int index) {
        while (true) {
            System.out.printf("Автомобиль %d:%n", index);
            int power = readPowerField();
            String model = readModelField(MODEL_LABEL);
            int year = readYearField();
            Car car = validateAndBuild(power, model, year);
            if (car != null) {
                input.markSuccess();
                return car;
            }
            System.out.println("Повторите ввод автомобиля.");
        }
    }

    /**
     * Считывает целочисленное поле, повторяя ввод при ошибке.
     *
     * @param label     подпись для приглашения ввода
     * @param fieldName имя поля для сообщений об ошибке
     * @return введённое целое число
     */
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

    /**
     * Запрашивает мощность и повторяет ввод до получения значения
     * из допустимого диапазона.
     *
     * @return корректная мощность автомобиля
     * @throws AttemptsExceededException если достигнут предел ошибок
     * @throws EndOfInputException если поток ввода завершён
     */
    private int readPowerField() {
        while (true) {
            int power = readIntField(POWER_LABEL, "мощность");

            if (power < 0) {
                input.reportError("Мощность не может быть отрицательной.");
                continue;
            }

            if (power < Limits.MIN_POWER || power > Limits.MAX_POWER) {
                input.reportError(
                        "Мощность должна быть в диапазоне от "
                                + Limits.MIN_POWER + " до " + Limits.MAX_POWER + "."
                );
                continue;
            }

            return power;
        }
    }

    /**
     * Считывает модель, проверяя её на пустоту и длину.
     *
     * @param label подпись для приглашения ввода
     * @return введённая модель
     */
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

    /**
     * Запрашивает год выпуска и повторяет ввод до получения значения
     * из допустимого диапазона.
     *
     * @return корректный год выпуска автомобиля
     * @throws AttemptsExceededException если достигнут предел ошибок
     * @throws EndOfInputException если поток ввода завершён
     */
    private int readYearField() {
        while (true) {
            int year = readIntField(YEAR_LABEL, "год производства");

            if (year < Limits.MIN_YEAR || year > Limits.MAX_YEAR) {
                input.reportError(
                        "Год должен быть в диапазоне от "
                                + Limits.MIN_YEAR + " до " + Limits.MAX_YEAR + "."
                );
                continue;
            }

            return year;
        }
    }

    /**
     * Проверяет данные и создаёт автомобиль, сообщая об ошибках.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year  год производства
     * @return созданный автомобиль или null при ошибке валидации
     */
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
