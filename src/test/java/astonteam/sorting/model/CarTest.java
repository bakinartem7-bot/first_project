package astonteam.sorting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
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
    void buildRejectsBlankModel() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(150).model("   ").year(2015).build());
    }

    @Test
    void buildRejectsNegativePower() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(-1).model("Toyota").year(2015).build());
    }

    @Test
    void buildRejectsPowerOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(2001).model("Toyota").year(2015).build());
    }

    @Test
    void buildRejectsYearOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(150).model("Toyota").year(1800).build());
    }

    @Test
    void toStringContainsFields() {
        Car car = Car.builder().power(99).model("Focus").year(2009).build();
        String text = car.toString();
        assertTrue(text.contains("99"));
        assertTrue(text.contains("Focus"));
        assertTrue(text.contains("2009"));
    }

    @Test
    void equalCarsHaveEqualHashCode() {
        Car first = Car.builder().power(150).model("Toyota").year(2015).build();
        Car second = Car.builder().power(150).model("Toyota").year(2015).build();
        assertNotSame(first, second);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void carsDifferingInAnyFieldAreNotEqual() {
        Car base = Car.builder().power(150).model("Toyota").year(2015).build();
        assertNotEquals(base, Car.builder().power(151).model("Toyota").year(2015).build());
        assertNotEquals(base, Car.builder().power(150).model("Honda").year(2015).build());
        assertNotEquals(base, Car.builder().power(150).model("Toyota").year(2016).build());
    }

    @Test
    void carIsNotEqualToNullOrOtherType() {
        Car car = Car.builder().power(150).model("Toyota").year(2015).build();
        assertEquals(car, car);
        assertNotEquals(car, null);
        assertNotEquals(car, "Toyota");
    }
}
