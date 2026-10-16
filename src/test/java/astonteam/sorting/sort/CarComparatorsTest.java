package astonteam.sorting.sort;

import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для компараторов {@link CarComparators}: проверка порядка сравнения
 * по мощности, модели и году, а также обработки {@code null}.
 */
class CarComparatorsTest {

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
     * Проверяет, что {@code BY_POWER} сравнивает автомобили только по мощности:
     * меньшая мощность — меньший порядок, равная мощность — равенство.
     */
    @Test
    void byPowerComparesOnlyPower() {
        Car weaker = car(100, "Toyota", 2020);
        Car stronger = car(200, "Audi", 2010);
        Car samePower = car(100, "BMW", 1990);
        assertTrue(CarComparators.BY_POWER.compare(weaker, stronger) < 0);
        assertTrue(CarComparators.BY_POWER.compare(stronger, weaker) > 0);
        assertEquals(0, CarComparators.BY_POWER.compare(weaker, samePower));
    }

    /**
     * Проверяет, что {@code BY_MODEL} сравнивает автомобили только по модели
     * (лексикографически), игнорируя мощность и год.
     */
    @Test
    void byModelComparesOnlyModel() {
        Car first = car(150, "Audi", 2020);
        Car second = car(200, "BMW", 2010);
        Car third = car(100, "Audi", 1990);
        assertTrue(CarComparators.BY_MODEL.compare(first, second) < 0);
        assertTrue(CarComparators.BY_MODEL.compare(second, first) > 0);
        assertEquals(0, CarComparators.BY_MODEL.compare(first, third));
    }

    /**
     * Проверяет, что {@code BY_YEAR} сравнивает автомобили только по году
     * выпуска, игнорируя мощность и модель.
     */
    @Test
    void byYearComparesOnlyYear() {
        Car older = car(150, "Toyota", 2000);
        Car newer = car(200, "Audi", 2020);
        Car sameYear = car(100, "BMW", 2000);
        assertTrue(CarComparators.BY_YEAR.compare(older, newer) < 0);
        assertTrue(CarComparators.BY_YEAR.compare(newer, older) > 0);
        assertEquals(0, CarComparators.BY_YEAR.compare(older, sameYear));
    }

    /**
     * Проверяет, что {@code BY_POWER} выбрасывает {@link NullPointerException}
     * при сравнении с {@code null}.
     */
    @Test
    void byPowerRejectsNull() {
        assertThrows(
                NullPointerException.class,
                () -> CarComparators.BY_POWER.compare(null, car(100, "A", 2020))
        );
    }

    /**
     * Проверяет, что {@code BY_MODEL} выбрасывает {@link NullPointerException}
     * при сравнении с {@code null}.
     */
    @Test
    void byModelRejectsNull() {
        assertThrows(
                NullPointerException.class,
                () -> CarComparators.BY_MODEL.compare(null, car(100, "A", 2020))
        );
    }

    /**
     * Проверяет, что {@code BY_YEAR} выбрасывает {@link NullPointerException}
     * при сравнении с {@code null}.
     */
    @Test
    void byYearRejectsNull() {
        assertThrows(
                NullPointerException.class,
                () -> CarComparators.BY_YEAR.compare(null, car(100, "A", 2020))
        );
    }
}