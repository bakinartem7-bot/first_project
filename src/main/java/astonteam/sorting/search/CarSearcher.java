package astonteam.sorting.search;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;

import java.util.Objects;

public final class CarSearcher {

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