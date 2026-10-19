package astonteam.sorting.fill;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.model.Car;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Заполняет коллекцию автомобилями со случайными характеристиками.
 */
public final class RandomFiller {

    /**
     * Создаёт заполнитель случайными данными.
     */
    public RandomFiller() {
    }

    /**
     * Создаёт коллекцию из случайно сгенерированных автомобилей.
     *
     * @param size количество автомобилей
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

        CarArrayList cars = new CarArrayList();

        IntStream.range(0, size)
                .mapToObj(i -> generateRandomCar())
                .forEach(cars::add);

        return cars;
    }

    /**
     * Генерирует один автомобиль со случайными мощностью, моделью и годом.
     *
     * @return случайный автомобиль
     */
    private Car generateRandomCar() {
        int power = ThreadLocalRandom.current().nextInt(Limits.MIN_POWER, Limits.MAX_POWER + 1);

        int year = ThreadLocalRandom.current().nextInt(Limits.MIN_YEAR, Limits.MAX_YEAR + 1);

        String model = "Model-" + ThreadLocalRandom.current().nextInt(1000, 10000);

        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }
}
