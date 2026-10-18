package astonteam.sorting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты модели {@link Car}: проверяют работу builder, валидацию полей,
 * методы equals/hashCode и строковое представление.
 */
class CarTest {

    /**
     * Проверяет, что builder корректно заполняет все поля автомобиля.
     */
    @Test
    void buildsWithAllFields() {
        Car car = Car.builder().power(150).model("Toyota").year(2015).build();
        assertEquals(150, car.getPower());
        assertEquals("Toyota", car.getModel());
        assertEquals(2015, car.getYear());
    }

    /**
     * Проверяет, что сборка без указания модели выбрасывает NullPointerException.
     */
    @Test
    void buildWithoutModelThrows() {
        assertThrows(NullPointerException.class, () -> Car.builder().power(1).year(2000).build());
    }

    /**
     * Проверяет, что модель, состоящая только из пробелов, отклоняется.
     */
    @Test
    void buildRejectsBlankModel() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(150).model("").year(2015).build()); //Проверка абсолютно пустой строки
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(150).model("   ").year(2015).build());
    }

    /**
     * Проверяет, что отрицательная мощность отклоняется валидацией.
     */
    @Test
    void buildRejectsNegativePower() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(-1).model("Toyota").year(2015).build());
    }

    /**
     * Проверяет, что мощность выше допустимого предела отклоняется.
     */
    @Test
    void buildRejectsPowerOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(0).model("Toyota").year(2015).build());
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(2001).model("Toyota").year(2015).build());
    }

    /**
     * Проверяет, что год выпуска вне допустимого диапазона отклоняется.
     */
    @Test
    void buildRejectsYearOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(150).model("Toyota").year(1885).build());
        assertThrows(IllegalArgumentException.class,
                () -> Car.builder().power(150).model("Toyota").year(2027).build());
    }

    /**
     * Проверяет, что toString содержит значения всех полей автомобиля.
     */
    @Test
    void toStringContainsFields() {
        Car car = Car.builder().power(99).model("Focus").year(2009).build();
        String text = car.toString();
        assertTrue(text.contains("99"));
        assertTrue(text.contains("Focus"));
        assertTrue(text.contains("2009"));
    }

    /**
     * Проверяет, что равные по значению, но разные экземпляры автомобилей
     * равны и имеют одинаковый hashCode.
     */
    @Test
    void equalCarsHaveEqualHashCode() {
        Car first = Car.builder().power(150).model("Toyota").year(2015).build();
        Car second = Car.builder().power(150).model("Toyota").year(2015).build();
        assertNotSame(first, second);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    /**
     * Проверяет, что автомобили, различающиеся хотя бы одним полем, не равны.
     */
    @Test
    void carsDifferingInAnyFieldAreNotEqual() {
        Car base = Car.builder().power(150).model("Toyota").year(2015).build();
        assertNotEquals(base, Car.builder().power(151).model("Toyota").year(2015).build());
        assertNotEquals(base, Car.builder().power(150).model("Honda").year(2015).build());
        assertNotEquals(base, Car.builder().power(150).model("Toyota").year(2016).build());
    }

    /**
     * Проверяет, что автомобиль равен самому себе, но не равен null и объекту другого типа.
     */
    @Test
    void carIsNotEqualToNullOrOtherType() {
        Car car = Car.builder().power(150).model("Toyota").year(2015).build();
        assertEquals(car, car);
        assertNotEquals(car, null);
        assertNotEquals(car, "Toyota");
    }
}
