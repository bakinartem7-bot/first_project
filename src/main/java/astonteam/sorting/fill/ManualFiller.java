package astonteam.sorting.fill;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.CarInputReader;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;

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

    private final CarInputReader carInputReader;

    /**
     * Создаёт заполнитель.
     *
     * @param input     источник консольного ввода
     * @param validator валидатор полей автомобиля
     */
    public ManualFiller(ConsoleInput input, CarValidator validator) {
        this.carInputReader = new CarInputReader(input, validator);
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
        System.out.printf("Автомобиль %d:%n", index);
        return carInputReader.readCar(
                POWER_LABEL,
                MODEL_LABEL,
                YEAR_LABEL
        );
    }
}
