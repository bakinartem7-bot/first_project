package astonteam.sorting.search;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarSearcherTest {

    private CarArrayList cars;
    private CarSearcher searcher;

    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    @BeforeEach
    void setUp() {
        cars = new CarArrayList();
        searcher = new CarSearcher();

        cars.add(car(100, "Bmw", 2021));
        cars.add(car(150, "Toyota", 2021));
        cars.add(car(2000, "toyota", 1999));
        cars.add(car(400, "Niva", 1986));
        cars.add(car(120, "Tesla", 2022));
        cars.add(car(100, "Bmw", 2022));
    }

    @Test
    void whenSearchingByPowerReturnsAllMatches() {
        CarArrayList foundCars = searcher.findByPower(cars, 100);
        assertEquals(2, foundCars.size());
        assertEquals(100, foundCars.get(0).getPower());
        assertEquals(100, foundCars.get(1).getPower());
    }

    @Test
    void whenSearchingByModelIgnoresCaseAndReturnsAllMatches() {
        CarArrayList foundCars = searcher.findByModel(cars, "TOYOTA");

        assertEquals(2, foundCars.size());
        assertEquals("Toyota", foundCars.get(0).getModel());
        assertEquals("toyota", foundCars.get(1).getModel());
    }

    @Test
    void whenSearchingByYearReturnsAllMatches() {
        CarArrayList foundCars = searcher.findByYear(cars, 2022);
        assertEquals(2, foundCars.size());
        assertEquals(2022, foundCars.get(0).getYear());
        assertEquals(2022, foundCars.get(1).getYear());
    }

    @Test
    void whenSearchingByWholeCarThenAllFieldsMustMatch() {
        Car target = car(150, "TOYOTA", 2021);
        cars.add(car(100, "TOYOTA", 2021));
        cars.add(car(150, "HONDA", 2021));
        cars.add(car(150, "TOYOTA", 2020));

        CarArrayList foundCars = searcher.findWholeCar(cars, target);

        assertEquals(1, foundCars.size());
        assertEquals(150, foundCars.get(0).getPower());
        assertEquals("Toyota", foundCars.get(0).getModel());
        assertEquals(2021, foundCars.get(0).getYear());
    }

    @Test
    void whenSearchingForNonExistingCarThenReturnsEmptyCollection() {
        Car target = car(999, "TOYOTA", 2021);

        CarArrayList foundCars = searcher.findWholeCar(cars, target);
        assertEquals(0, foundCars.size());
    }

    @Test
    void whenSearchingEmptyCollectionThenReturnsEmptyCollection() {
        CarArrayList emptyCars = new CarArrayList();

        CarArrayList foundCars = searcher.findByPower(emptyCars, 100);

        assertEquals(0, foundCars.size());
    }
}