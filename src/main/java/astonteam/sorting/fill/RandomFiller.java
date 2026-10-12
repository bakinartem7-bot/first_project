package astonteam.sorting.fill;

import  astonteam.sorting.collection.CarArrayList;
import  astonteam.sorting.common.Limits;
import  astonteam.sorting.model.Car;
import  astonteam.sorting.validation.CarValidator;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


public final class RandomFiller {

    private final CarValidator validator;

    public RandomFiller(CarValidator validator) {
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    public CarArrayList fill(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер массива должен быть больше нуля: " + size);
        }
        if (size > Limits.MAX_COLLECTION_SIZE) {
            throw  new IllegalArgumentException("Максимальный размер массива: " + Limits.MAX_COLLECTION_SIZE);
        }

        CarArrayList cars = new CarArrayList();

        IntStream.range(0, size)
                .mapToObj(i -> generateRandomCar())
                .forEach(cars::add);

        return cars;

    }

    private Car generateRandomCar () {
        int power = ThreadLocalRandom.current().nextInt(1, 2001);

        int year = ThreadLocalRandom.current().nextInt(1886, 2027);

        String model = "Model-" + ThreadLocalRandom.current().nextInt(1000, 10000);

        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }



}
