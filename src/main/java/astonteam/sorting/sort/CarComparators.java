package astonteam.sorting.sort;

import astonteam.sorting.model.Car;

import java.util.Comparator;

public final class CarComparators {
    private CarComparators() {
    }

    public static final Comparator<Car> BY_POWER = Comparator.comparingInt(Car::getPower);
    public static final Comparator<Car> BY_MODEL = Comparator.comparing(Car::getModel);
    public static final Comparator<Car> BY_YEAR = Comparator.comparingInt(Car::getYear);
}
