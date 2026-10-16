package astonteam.sorting.sort;

import astonteam.sorting.model.Car;

import java.util.Comparator;

/**
 * Набор готовых компараторов для сортировки автомобилей по отдельным полям.
 */
public final class CarComparators {
    /**
     * Утилитный класс — инстанцирование запрещено.
     */
    private CarComparators() {
    }

    /**
     * Компаратор сортировки по мощности автомобиля.
     */
    public static final Comparator<Car> BY_POWER = Comparator.comparingInt(Car::getPower);
    /**
     * Компаратор сортировки по модели автомобиля.
     */
    public static final Comparator<Car> BY_MODEL = Comparator.comparing(Car::getModel);
    /**
     * Компаратор сортировки по году выпуска автомобиля.
     */
    public static final Comparator<Car> BY_YEAR = Comparator.comparingInt(Car::getYear);
}
