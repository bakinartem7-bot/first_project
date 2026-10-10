package astonteam.sorting.io;

import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarLineFormatTest {

    private static Car car(int power, String model, int year) {
        return Car.builder().power(power).model(model).year(year).build();
    }

    @Test
    void formatProducesDelimitedLine() {
        assertEquals("150;Toyota;2015", CarLineFormat.format(car(150, "Toyota", 2015)));
    }

    @Test
    void patternConstantMatchesFields() {
        assertEquals("мощность;модель;год", CarLineFormat.PATTERN);
        assertEquals(";", CarLineFormat.DELIMITER);
    }

    @Test
    void formatRejectsNullCar() {
        assertThrows(NullPointerException.class, () -> CarLineFormat.format(null));
    }

    @Test
    void formatRejectsBlankModel() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.format(car(150, "   ", 2015)));
    }

    @Test
    void formatRejectsDelimiterInModel() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.format(car(150, "A;B", 2015)));
    }

    @Test
    void formatRejectsControlCharsInModel() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.format(car(150, "A\tB", 2015)));
    }

    @Test
    void privateConstructorIsNotUsableDirectly() throws Exception {
        Constructor<CarLineFormat> constructor = CarLineFormat.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }
}
