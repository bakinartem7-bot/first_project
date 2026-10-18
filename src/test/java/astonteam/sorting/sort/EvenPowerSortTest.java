package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Тесты для алгоритма {@link EvenPowerSort}: проверка сортировки только
 * автомобилей с чётной мощностью и сохранности остальных элементов.
 */
class EvenPowerSortTest {
    /**
     * Коллекция автомобилей, наполняемая в {@link #setUp()}.
     */
    private CarArrayList cars;

    /**
     * Создаёт автомобиль с указанными параметрами для использования в тестах.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year  год выпуска автомобиля
     * @return собранный экземпляр {@link Car}
     */
    private static Car car(int power, String model, int year) {
        return Car.builder()
                .power(power)
                .model(model)
                .year(year)
                .build();
    }

    /**
     * Подготавливает коллекцию из 100 автомобилей с возрастающей мощностью,
     * моделями «Model0»..«Model99» и годами 2000..2026.
     */
    @BeforeEach
    void setUp() {
        cars = new CarArrayList();
        for (int i = 0; i < 100; i++) {
            cars.add(
                    car(100 + i, "Model" + i, 2000 + (i % 27))
            );
        }
    }

    /**
     * Проверяет, что при сортировке автомобили с чётной мощностью
     * перемещаются на свои позиции, а с нечётной — остаются на своих местах.
     */
    @Test
    void whenSortingThenOddPowerCarsStayAtTheirPositions() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(300, "Even-300", 2022));
        testCars.add(car(101, "Odd-101", 2021));
        testCars.add(car(100, "Even-100", 2020));
        testCars.add(car(99, "Odd-99", 2019));
        testCars.add(car(200, "Even-200", 2018));
        Car odd101 = testCars.get(1);
        Car odd99 = testCars.get(3);
        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);
        assertSame(odd101, testCars.get(1));
        assertSame(odd99, testCars.get(3));
        assertEquals(100, testCars.get(0).getPower());
        assertEquals(101, testCars.get(1).getPower());
        assertEquals(200, testCars.get(2).getPower());
        assertEquals(99, testCars.get(3).getPower());
        assertEquals(300, testCars.get(4).getPower());
    }

    /**
     * Проверяет, что сортировка пустой коллекции не выбрасывает исключений
     * и оставляет её пустой.
     */
    @Test
    void whenSortingEmptyCollectionThenNoException() {
        CarArrayList emptyCars = new CarArrayList();
        new EvenPowerSort("power").sort(emptyCars, CarComparators.BY_POWER);
        assertEquals(0, emptyCars.size(),
                "Пустая коллекция должна оставаться пустой после сортировки");
    }

    /**
     * Проверяет, что сортировка коллекции из одного элемента не нарушает
     * ни сам элемент, ни размер коллекции.
     */
    @Test
    void whenSortingSingleElementCollectionThenNoException() {
        CarArrayList singleCar = new CarArrayList();
        singleCar.add(car(150, "Solo", 2020));
        new EvenPowerSort("power").sort(singleCar, CarComparators.BY_POWER);
        assertEquals("Solo", singleCar.get(0).getModel(),
                "Элемент коллекции должен оставаться неизменным после сортировки");
        assertEquals(2020, singleCar.get(0).getYear(),
                "Элемент коллекции должен оставаться неизменным после сортировки");
        assertEquals(150, singleCar.get(0).getPower(),
                "Элемент коллекции должен оставаться неизменным после сортировки");
        assertEquals(1, singleCar.size(),
                "Коллекция с одним элементом должна оставаться неизменной после сортировки");
    }

    /**
     * Проверяет, что на уже отсортированной коллекции чётные элементы
     * остаются упорядоченными, а нечётные — на исходных позициях.
     */
    @Test
    void whenSortingAlreadySortedCollectionThenEvenCarsStaySortedAndOddCarsStayInPlace() {
        Car[] originalCars = new Car[cars.size()];
        for (int i = 0; i < cars.size(); i++) {
            originalCars[i] = cars.get(i);
        }
        new EvenPowerSort("power").sort(cars, CarComparators.BY_POWER);
        int previousEvenPower = Integer.MIN_VALUE;
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getPower() % 2 == 0) {
                assertTrue(car.getPower() >= previousEvenPower);
                previousEvenPower = car.getPower();
            } else {
                assertSame(
                        originalCars[i],
                        car,
                        "Автомобиль с нечётной мощностью должен остаться на исходной позиции " + i
                );
            }
        }
    }

    /**
     * Проверяет, что обратный порядок элементов после сортировки
     * становится неубывающим по мощности.
     */
    @Test
    void whenSortingReverseOrderedCollectionThenItBecomesSorted() {
        CarArrayList testCars = new CarArrayList();

        testCars.add(car(300, "C", 2022));
        testCars.add(car(200, "B", 2021));
        testCars.add(car(100, "A", 2020));
        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);
        assertEquals(100, testCars.get(0).getPower());
        assertEquals(200, testCars.get(1).getPower());
        assertEquals(300, testCars.get(2).getPower());
    }

    /**
     * Проверяет стабильность сортировки: автомобили с равной чётной
     * мощностью сохраняют исходный относительный порядок.
     */
    @Test
    void whenEvenCarsHaveEqualPowerThenTheirOriginalOrderIsPreserved() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(200, "First", 2020));
        testCars.add(car(100, "Smaller", 2021));
        testCars.add(car(200, "Second", 2022));

        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);

        assertEquals("Smaller", testCars.get(0).getModel());
        assertEquals("First", testCars.get(1).getModel());
        assertEquals("Second", testCars.get(2).getModel());
    }

    /**
     * Проверяет, что при отсутствии автомобилей с чётной мощностью
     * коллекция остаётся полностью неизменной.
     */
    @Test
    void whenThereAreNoEvenPowerCarsThenCollectionStaysUnchanged() {
        CarArrayList testCars = new CarArrayList();
        Car first = car(101, "First", 2020);
        Car second = car(99, "Second", 2021);

        testCars.add(first);
        testCars.add(second);

        new EvenPowerSort("power").sort(testCars, CarComparators.BY_POWER);

        assertSame(first, testCars.get(0));
        assertSame(second, testCars.get(1));
    }

    /**
     * Проверяет, что сортировка с {@code null}-компаратором выбрасывает
     * {@link NullPointerException} с сообщением «comparator».
     */
    @Test
    void whenNullComparatorThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new EvenPowerSort("power").sort(cars, null)
        );
        assertEquals("comparator", exception.getMessage());
    }

    /**
     * Проверяет, что сортировка {@code null}-коллекции выбрасывает
     * {@link NullPointerException} с сообщением «cars».
     */
    @Test
    void whenNullCollectionThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new EvenPowerSort("power")
                        .sort(null, CarComparators.BY_POWER)
        );
        assertEquals("cars", exception.getMessage());
    }
}