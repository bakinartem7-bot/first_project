package astonteam.sorting.search;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для {@link CarSearcher}: поиск автомобилей по отдельным полям и по целому объекту.
 */
class CarSearcherTest {

    /**
     * Коллекция автомобилей, наполняемая перед каждым тестом.
     */
    private CarArrayList cars;
    /**
     * Тестируемый поисковик автомобилей.
     */
    private CarSearcher searcher;

    /**
     * Создаёт тестовый автомобиль с указанными параметрами.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year год выпуска автомобиля
     * @return построенный объект {@link Car}
     */
    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    /**
     * Подготавливает перед каждым тестом поисковик и коллекцию машин.
     */
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

    /**
     * Проверяет, что поиск по мощности возвращает все совпадения.
     */
    @Test
    void whenSearchingByPowerReturnsAllMatches() {
        CarArrayList foundCars = searcher.findByPower(cars, 100);
        assertEquals(2, foundCars.size());
        assertEquals(100, foundCars.get(0).getPower());
        assertEquals(100, foundCars.get(1).getPower());
    }

    /**
     * Проверяет, что поиск по модели игнорирует регистр и возвращает все совпадения.
     */
    @Test
    void whenSearchingByModelIgnoresCaseAndReturnsAllMatches() {
        CarArrayList foundCars = searcher.findByModel(cars, "TOYOTA");

        assertEquals(2, foundCars.size());
        assertEquals("Toyota", foundCars.get(0).getModel());
        assertEquals("toyota", foundCars.get(1).getModel());
    }

    /**
     * Проверяет, что поиск по году возвращает все совпадения.
     */
    @Test
    void whenSearchingByYearReturnsAllMatches() {
        CarArrayList foundCars = searcher.findByYear(cars, 2022);
        assertEquals(2, foundCars.size());
        assertEquals(2022, foundCars.get(0).getYear());
        assertEquals(2022, foundCars.get(1).getYear());
    }

    /**
     * Проверяет, что при поиске по целому автомобилю должны совпасть все поля.
     */
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

    /**
     * Проверяет, что поиск несуществующего автомобиля возвращает пустую коллекцию.
     */
    @Test
    void whenSearchingForNonExistingCarThenReturnsEmptyCollection() {
        Car target = car(999, "TOYOTA", 2021);

        CarArrayList foundCars = searcher.findWholeCar(cars, target);
        assertEquals(0, foundCars.size());
    }

    /**
     * Проверяет, что поиск по пустой коллекции возвращает пустую коллекцию.
     */
    @Test
    void whenSearchingEmptyCollectionThenReturnsEmptyCollection() {
        CarArrayList emptyCars = new CarArrayList();

        CarArrayList foundCars = searcher.findByPower(emptyCars, 100);

        assertEquals(0, foundCars.size());
    }
}