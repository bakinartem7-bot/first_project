package astonteam.sorting.search;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;

import java.util.Objects;

/**
 * Утилита поиска автомобилей в коллекции по различным полям.
 */
public final class CarSearcher {

    /**
     * Находит все автомобили с указанной мощностью.
     *
     * @param cars   коллекция автомобилей для поиска
     * @param power  искомая мощность
     * @return коллекция автомобилей, мощность которых совпадает с заданной
     * @throws NullPointerException если {@code cars} равен {@code null}
     */
    public CarArrayList findByPower(CarArrayList cars, int power) {
        Objects.requireNonNull(cars, "cars");

        CarArrayList foundCars = new CarArrayList();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getPower() == power) {
                foundCars.add(car);
            }
        }

        return foundCars;
    }

    /**
     * Находит все автомобили с указанной моделью (без учёта регистра, с обрезкой пробелов).
     *
     * @param cars  коллекция автомобилей для поиска
     * @param model искомая модель
     * @return коллекция автомобилей с совпадающей моделью
     * @throws NullPointerException если {@code cars} или {@code model} равны {@code null}
     */
    public CarArrayList findByModel(CarArrayList cars, String model) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(model, "model");

        String searchedModel = model.trim();
        CarArrayList foundCars = new CarArrayList();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getModel().equalsIgnoreCase(searchedModel)) {
                foundCars.add(car);
            }
        }

        return foundCars;
    }

    /**
     * Находит все автомобили указанного года выпуска.
     *
     * @param cars  коллекция автомобилей для поиска
     * @param year  искомый год выпуска
     * @return коллекция автомобилей, год выпуска которых совпадает с заданным
     * @throws NullPointerException если {@code cars} равен {@code null}
     */
    public CarArrayList findByYear(CarArrayList cars, int year) {
        Objects.requireNonNull(cars, "cars");

        CarArrayList foundCars = new CarArrayList();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getYear() == year) {
                foundCars.add(car);
            }
        }

        return foundCars;
    }

    /**
     * Находит все автомобили, полностью совпадающие с целевым по мощности, году и модели.
     *
     * @param cars   коллекция автомобилей для поиска
     * @param target целевой автомобиль, поля которого используются для сравнения
     * @return коллекция найденных совпадений
     * @throws NullPointerException если {@code cars} или {@code target} равны {@code null}
     */
    public CarArrayList findWholeCar(CarArrayList cars, Car target) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(target, "target");

        CarArrayList foundCars = new CarArrayList();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);

            if (car.getPower() == target.getPower()
                    && car.getYear() == target.getYear()
                    && car.getModel().equalsIgnoreCase(target.getModel())) {
                foundCars.add(car);
            }
        }

        return foundCars;
    }
}