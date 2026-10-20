package astonteam.sorting.console;

import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;
import astonteam.sorting.validation.ValidationResult;

import java.util.Objects;

/**
 * Читает и проверяет поля автомобиля из консоли.
 *
 * <p>При вводе одного критерия счётчик ошибок сбрасывается после
 * корректного значения. При вводе целого автомобиля счётчик
 * сбрасывается только после корректного ввода всех полей.</p>
 */
public final class CarInputReader {

    private static final String POWER_PROMPT = "Введите мощность: ";
    private static final String MODEL_PROMPT = "Введите модель: ";
    private static final String YEAR_PROMPT = "Введите год выпуска: ";

    private final ConsoleInput input;
    private final CarValidator validator;

    /**
     * Создаёт средство чтения полей автомобиля.
     *
     * @param input источник консольного ввода
     * @throws NullPointerException если {@code input} равен {@code null}
     */
    public CarInputReader(ConsoleInput input) {
        this(input, new CarValidator());
    }

    /**
     * Создаёт средство чтения с заданным валидатором.
     *
     * @param input     источник консольного ввода
     * @param validator валидатор полей автомобиля
     * @throws NullPointerException если один из аргументов равен {@code null}
     */
    public CarInputReader(ConsoleInput input, CarValidator validator) {
        this.input = Objects.requireNonNull(input, "input");
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    /**
     * Запрашивает мощность как самостоятельный критерий.
     *
     * @return корректная мощность
     */
    public int readPower() {
        int power = readPowerValue(POWER_PROMPT);
        input.markSuccess();
        return power;
    }

    /**
     * Запрашивает модель как самостоятельный критерий.
     *
     * @return корректная модель
     */
    public String readModel() {
        String model = readModelValue(MODEL_PROMPT);
        input.markSuccess();
        return model;
    }

    /**
     * Запрашивает год как самостоятельный критерий.
     *
     * @return корректный год выпуска
     */
    public int readYear() {
        int year = readYearValue(YEAR_PROMPT);
        input.markSuccess();
        return year;
    }

    /**
     * Запрашивает все поля и создаёт автомобиль.
     *
     * <p>Счётчик ошибок сбрасывается только после корректного
     * ввода всего автомобиля.</p>
     *
     * @return созданный автомобиль
     */
    public Car readCar() {
        return readCar(POWER_PROMPT, MODEL_PROMPT, YEAR_PROMPT);
    }

    /**
     * Запрашивает автомобиль с заданными текстами приглашений.
     *
     * @param powerPrompt приглашение для мощности
     * @param modelPrompt приглашение для модели
     * @param yearPrompt  приглашение для года
     * @return созданный автомобиль
     */
    public Car readCar(
            String powerPrompt,
            String modelPrompt,
            String yearPrompt
    ) {
        int power = readPowerValue(powerPrompt);
        String model = readModelValue(modelPrompt);
        int year = readYearValue(yearPrompt);
        Car car = Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
        input.markSuccess();
        return car;
    }

    /**
     * Запрашивает мощность до получения корректного значения.
     *
     * <p>Метод не сбрасывает счётчик ошибок: это выполняется после
     * успешного ввода отдельного критерия или всего автомобиля.</p>
     *
     * @param prompt приглашение к вводу мощности
     * @return корректная мощность автомобиля
     * @throws AttemptsExceededException если достигнут предел ошибок ввода
     * @throws EndOfInputException если поток ввода завершён
     */
    private int readPowerValue(String prompt) {
        while (true) {
            int power = input.readInt(prompt);
            ValidationResult result = validator.validatePower(power);
            if (result.isValid()) {
                return power;
            }
            input.reportError(result.message());
        }
    }

    /**
     * Запрашивает название модели до получения корректного значения.
     *
     * <p>Метод не сбрасывает счётчик ошибок: это выполняется после
     * успешного ввода отдельного критерия или всего автомобиля.</p>
     *
     * @param prompt приглашение к вводу модели
     * @return корректное название модели
     * @throws AttemptsExceededException если достигнут предел ошибок ввода
     * @throws EndOfInputException если поток ввода завершён
     */
    private String readModelValue(String prompt) {
        while (true) {
            String model = input.readLine(prompt);
            ValidationResult result = validator.validateModel(model);
            if (result.isValid()) {
                return model;
            }
            input.reportError(result.message());
        }
    }

    /**
     * Запрашивает год выпуска до получения корректного значения.
     *
     * <p>Метод не сбрасывает счётчик ошибок: это выполняется после
     * успешного ввода отдельного критерия или всего автомобиля.</p>
     *
     * @param prompt приглашение к вводу года выпуска
     * @return корректный год выпуска
     * @throws AttemptsExceededException если достигнут предел ошибок ввода
     * @throws EndOfInputException если поток ввода завершён
     */
    private int readYearValue(String prompt) {
        while (true) {
            int year = input.readInt(prompt);
            ValidationResult result = validator.validateYear(year);
            if (result.isValid()) {
                return year;
            }
            input.reportError(result.message());
        }
    }
}