package astonteam.sorting.fill;

import  astonteam.sorting.collection.CarArrayList;
import  astonteam.sorting.common.Limits;
import  astonteam.sorting.model.Car;
import  astonteam.sorting.validation.CarValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class RandomFillerTest {
    private RandomFiller randomFiller;
    private CarValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CarValidator();
        randomFiller = new RandomFiller(validator);
    }

    @Test
    void shouldGenerateCollectionWithExactSize() {
        int size = 10;
        CarArrayList result = randomFiller.fill(size);
        assertEquals(size, result.size(), "Размер коллекции должен соответствовать запрошенному");
    }

    @Test
    void shouldThrowExceptionWhenSizeIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> randomFiller.fill(0));
        assertThrows(IllegalArgumentException.class, () -> randomFiller.fill(-5));
    }

    @Test
    void shouldThrowExceptionWhenSizeExceedsLimit() {
        int tooBigSize = Limits.MAX_COLLECTION_SIZE + 1;
        assertThrows(IllegalArgumentException.class, () ->randomFiller.fill(tooBigSize));
    }

    @Test
    void  shouldGenerateOnlyValidCars() {
        int size = 100;
        CarArrayList result = randomFiller.fill(size);

        for (Car car : result) {
            assertNotNull(car, "Машина не должна быть null");
            assertNotNull(car.getModel(), "Модель не должна быть null");
            assertFalse(car.getModel().isBlank(), "Модель не должна быть пустой");

            assertTrue(car.getPower() >= 1 && car.getPower() <= 2000, "Мощность вне допустимого диапазона: " + car.getPower());
            assertTrue(car.getYear() >= 1886 && car.getYear() <= 2026, "Год вне допустимого диапазона: " + car.getYear());
        }
    }
}
