package astonteam.sorting.sort;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для алгоритма {@link InsertionSort}: проверка сортировки
 * по разным полям, крайних случаев и обработки ошибок.
 */
class InsertionSortTest {
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
     * Проверяет, что после сортировки по мощности коллекция упорядочена
     * по неубыванию поля {@code power}.
     */
    @Test
    void whenSortingByPowerThenCollectionIsSorted() {
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getPower() <= cars.get(i).getPower(),
                    "Коллекция не отсортирована по мощности на позиции " + i
            );
        }
    }

    /**
     * Проверяет, что после сортировки по году коллекция упорядочена
     * по неубыванию поля {@code year}.
     */
    @Test
    void whenSortingByYearThenCollectionIsSorted() {
        new InsertionSort("year").sort(cars, CarComparators.BY_YEAR);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getYear() <= cars.get(i).getYear(),
                    "Коллекция не отсортирована по году на позиции " + i
            );
        }
    }


    /**
     * Проверяет, что после сортировки по модели коллекция упорядочена
     * лексикографически по полю {@code model}.
     */
    @Test
    void whenSortingByModelThenCollectionIsSorted() {
        new InsertionSort("model").sort(cars, CarComparators.BY_MODEL);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getModel().compareTo(cars.get(i).getModel()) <= 0,
                    "Коллекция не отсортирована по модели на позиции " + i
            );
        }
    }

    /**
     * Проверяет, что сортировка пустой коллекции не выбрасывает исключений
     * и оставляет её пустой.
     */
    @Test
    void whenSortingEmptyCollectionThenNoException() {
        CarArrayList emptyCars = new CarArrayList();
        new InsertionSort("power").sort(emptyCars, CarComparators.BY_POWER);
        assertEquals(0, emptyCars.size(),
                "Пустая коллекция должна оставаться пустой после сортировки");
        new InsertionSort("model").sort(emptyCars, CarComparators.BY_MODEL);
        assertEquals(0, emptyCars.size(),
                "Пустая коллекция должна оставаться пустой после сортировки");
        new InsertionSort("year").sort(emptyCars, CarComparators.BY_YEAR);
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
        new InsertionSort("power").sort(singleCar, CarComparators.BY_POWER);
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
     * Проверяет, что повторная сортировка уже отсортированной коллекции
     * не ломает порядок элементов.
     */
    @Test
    void whenSortingAlreadySortedCollectionThenNoException() {
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        new InsertionSort("power").sort(cars, CarComparators.BY_POWER);
        for (int i = 1; i < cars.size(); i++) {
            assertTrue(
                    cars.get(i - 1).getPower() <= cars.get(i).getPower(),
                    "Коллекция не отсортирована по мощности на позиции " + i
            );
        }
    }

    /**
     * Проверяет корректность сортировки при наличии дубликатов
     * по мощности в коллекции.
     */
    @Test
    void whenCollectionHasDuplicatesThenSortedByPowerCorrectly() {
        CarArrayList samePowerCars = new CarArrayList();
        samePowerCars.add(car(150, "Model50", 2025));
        samePowerCars.add(car(100, "Model30", 2024));
        samePowerCars.add(car(150, "Model10", 2023));
        new InsertionSort("power").sort(samePowerCars, CarComparators.BY_POWER);
        assertEquals("Model30", samePowerCars.get(0).getModel());
        assertEquals("Model50", samePowerCars.get(1).getModel());
        assertEquals("Model10", samePowerCars.get(2).getModel());
    }

    /**
     * Проверяет корректность сортировки при наличии дубликатов
     * по модели в коллекции.
     */
    @Test
    void whenCollectionHasDuplicatesThenSortedByModelCorrectly() {
        CarArrayList sameModelCars = new CarArrayList();
        sameModelCars.add(car(200, "Model50", 2025));
        sameModelCars.add(car(150, "Model10", 2024));
        sameModelCars.add(car(100, "Model50", 2023));
        new InsertionSort("model").sort(sameModelCars, CarComparators.BY_MODEL);
        assertEquals(150, sameModelCars.get(0).getPower());
        assertEquals(200, sameModelCars.get(1).getPower());
        assertEquals(100, sameModelCars.get(2).getPower());
    }

    /**
     * Проверяет корректность сортировки при наличии дубликатов
     * по году в коллекции.
     */
    @Test
    void whenCollectionHasDuplicatesThenSortedByYearCorrectly() {
        CarArrayList sameYearCars = new CarArrayList();
        sameYearCars.add(car(250, "Model50", 2025));
        sameYearCars.add(car(150, "Model30", 2020));
        sameYearCars.add(car(100, "Model10", 2025));
        new InsertionSort("year").sort(sameYearCars, CarComparators.BY_YEAR);
        assertEquals("Model30", sameYearCars.get(0).getModel());
        assertEquals("Model50", sameYearCars.get(1).getModel());
        assertEquals("Model10", sameYearCars.get(2).getModel());
    }

    /**
     * Проверяет, что обратный порядок элементов после сортировки
     * становится неубывающим по мощности.
     */
    @Test
    void whenSortingReverseOrderedCollectionByPowerThenItBecomesSorted() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(300, "C", 2022));
        testCars.add(car(200, "B", 2021));
        testCars.add(car(100, "A", 2020));
        new InsertionSort("power").sort(testCars, CarComparators.BY_POWER);
        assertEquals(100, testCars.get(0).getPower());
        assertEquals(200, testCars.get(1).getPower());
        assertEquals(300, testCars.get(2).getPower());
    }

    /**
     * Проверяет, что обратный порядок элементов после сортировки
     * становится неубывающим по модели.
     */
    @Test
    void whenSortingReverseOrderedCollectionByModelThenItBecomesSorted() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(300, "C", 2022));
        testCars.add(car(200, "B", 2021));
        testCars.add(car(100, "A", 2020));
        new InsertionSort("model").sort(testCars, CarComparators.BY_MODEL);
        assertEquals("A", testCars.get(0).getModel());
        assertEquals("B", testCars.get(1).getModel());
        assertEquals("C", testCars.get(2).getModel());
    }

    /**
     * Проверяет, что обратный порядок элементов после сортировки
     * становится неубывающим по году.
     */
    @Test
    void whenSortingReverseOrderedCollectionByYearThenItBecomesSorted() {
        CarArrayList testCars = new CarArrayList();
        testCars.add(car(300, "C", 2022));
        testCars.add(car(200, "B", 2021));
        testCars.add(car(100, "A", 2020));
        new InsertionSort("year").sort(testCars, CarComparators.BY_YEAR);
        assertEquals(2020, testCars.get(0).getYear());
        assertEquals(2021, testCars.get(1).getYear());
        assertEquals(2022, testCars.get(2).getYear());
    }

    /**
     * Проверяет стабильность сортировки: автомобили с равной мощностью
     * сохраняют исходный относительный порядок.
     */
    @Test
    void whenEqualPowerThenOriginalOrderIsPreserved() {
        Car first = car(150, "First", 2020);
        Car second = car(150, "Second", 2021);
        CarArrayList testCars = new CarArrayList();
        testCars.add(first);
        testCars.add(second);
        new InsertionSort("power").sort(testCars, CarComparators.BY_POWER);
        assertEquals("First", testCars.get(0).getModel());
        assertEquals("Second", testCars.get(1).getModel());
    }

    /**
     * Проверяет стабильность сортировки: автомобили с равной моделью
     * сохраняют исходный относительный порядок.
     */
    @Test
    void whenEqualModelThenOriginalOrderIsPreserved() {
        Car first = car(150, "Model", 2020);
        Car second = car(100, "Model", 2021);
        CarArrayList testCars = new CarArrayList();
        testCars.add(first);
        testCars.add(second);
        new InsertionSort("model").sort(testCars, CarComparators.BY_MODEL);
        assertEquals(150, testCars.get(0).getPower());
        assertEquals(100, testCars.get(1).getPower());
    }

    /**
     * Проверяет стабильность сортировки: автомобили с равным годом
     * сохраняют исходный относительный порядок.
     */
    @Test
    void whenEqualYearThenOriginalOrderIsPreserved() {
        Car first = car(250, "First", 2021);
        Car second = car(150, "Second", 2021);
        CarArrayList testCars = new CarArrayList();
        testCars.add(first);
        testCars.add(second);
        new InsertionSort("year").sort(testCars, CarComparators.BY_YEAR);
        assertEquals("First", testCars.get(0).getModel());
        assertEquals("Second", testCars.get(1).getModel());
    }

    /**
     * Проверяет, что сортировка с {@code null}-компаратором выбрасывает
     * {@link NullPointerException} с сообщением «comparator».
     */
    @Test
    void whenNullComparatorThenThrowsException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new InsertionSort("power").sort(cars, null)
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
                () -> new InsertionSort("power")
                        .sort(null, CarComparators.BY_POWER)
        );
        assertEquals("cars", exception.getMessage());
    }
}