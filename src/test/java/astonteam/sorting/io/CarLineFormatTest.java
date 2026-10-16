package astonteam.sorting.io;

import astonteam.sorting.model.Car;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для {@link CarLineFormat}: форматирование автомобиля в строку и разбор строки обратно в объект.
 */
class CarLineFormatTest {

    /**
     * Создаёт тестовый автомобиль с указанными параметрами.
     *
     * @param power мощность автомобиля
     * @param model модель автомобиля
     * @param year год выпуска автомобиля
     * @return построенный объект {@link Car}
     */
    private static Car car(int power, String model, int year) {
        return Car.builder().power(power).model(model).year(year).build();
    }

    /**
     * Проверяет, что форматирование даёт строку с разделителями вида «мощность;модель;год».
     */
    @Test
    void formatProducesDelimitedLine() {
        assertEquals("150;Toyota;2015", CarLineFormat.format(car(150, "Toyota", 2015)));
    }

    /**
     * Проверяет значения констант PATTERN и DELIMITER.
     */
    @Test
    void patternConstantMatchesFields() {
        assertEquals("мощность;модель;год", CarLineFormat.PATTERN);
        assertEquals(";", CarLineFormat.DELIMITER);
    }

    /**
     * Проверяет, что форматирование null-автомобиля приводит к NullPointerException.
     */
    @Test
    void formatRejectsNullCar() {
        assertThrows(NullPointerException.class, () -> CarLineFormat.format(null));
    }

    /**
     * Проверяет, что модель, содержащая разделитель, отклоняется при форматировании.
     */
    @Test
    void formatRejectsDelimiterInModel() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.format(car(150, "A;B", 2015)));
    }

    /**
     * Проверяет, что модель с управляющими символами отклоняется при форматировании.
     */
    @Test
    void formatRejectsControlCharsInModel() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.format(car(150, "A\tB", 2015)));
    }

    /**
     * Проверяет, что приватный конструктор доступен через рефлексию и создаёт экземпляр.
     */
    @Test
    void privateConstructorIsNotUsableDirectly() throws Exception {
        Constructor<CarLineFormat> constructor = CarLineFormat.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }

    /**
     * Проверяет разбор корректной строки в автомобиль с правильными полями.
     */
    @Test
    void parseValidLine() {
        Car car = CarLineFormat.parse("150;Toyota;2015");
        assertEquals(150, car.getPower());
        assertEquals("Toyota", car.getModel());
        assertEquals(2015, car.getYear());
    }

    /**
     * Проверяет, что разбор null-строки приводит к NullPointerException.
     */
    @Test
    void parseRejectsNull() {
        assertThrows(NullPointerException.class, () -> CarLineFormat.parse(null));
    }

    /**
     * Проверяет, что пустая или состоящая из пробелов строка отклоняется при разборе.
     */
    @Test
    void parseRejectsEmptyOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse(""));
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("   "));
    }

    /**
     * Проверяет, что строка с меньшим, чем нужно, числом частей отклоняется.
     */
    @Test
    void parseRejectsTooFewParts() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("150;Toyota"));
    }

    /**
     * Проверяет, что строка с большим, чем нужно, числом частей отклоняется.
     */
    @Test
    void parseRejectsTooManyParts() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("150;Toyota;Corolla;2015"));
    }

    /**
     * Проверяет, что нечисловая мощность отклоняется при разборе.
     */
    @Test
    void parseRejectsInvalidPower() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("abc;Toyota;2015"));
    }

    /**
     * Проверяет, что нечисловой год отклоняется при разборе.
     */
    @Test
    void parseRejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("150;Toyota;year"));
    }

    /**
     * Проверяет, что пустая модель отклоняется при разборе.
     */
    @Test
    void parseRejectsEmptyModel() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("150;;2015"));
    }

    /**
     * Проверяет, что мощность вне допустимого диапазона отклоняется при разборе.
     */
    @Test
    void parseRejectsOutOfBoundsPower() {
        assertThrows(IllegalArgumentException.class, () -> CarLineFormat.parse("-10;Toyota;2015"));
    }



}
