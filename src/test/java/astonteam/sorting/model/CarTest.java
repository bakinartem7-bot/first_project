package astonteam.sorting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarTest {

    @Test
    void buildsWithAllFields() {
        Car car = Car.builder().power(150).model("Toyota").year(2015).build();
        assertEquals(150, car.getPower());
        assertEquals("Toyota", car.getModel());
        assertEquals(2015, car.getYear());
    }

    @Test
    void buildWithoutModelThrows() {
        assertThrows(NullPointerException.class, () -> Car.builder().power(1).year(2000).build());
    }

    @Test
    void toStringContainsFields() {
        Car car = Car.builder().power(99).model("Focus").year(2009).build();
        String text = car.toString();
        assertTrue(text.contains("99"));
        assertTrue(text.contains("Focus"));
        assertTrue(text.contains("2009"));
    }
}
