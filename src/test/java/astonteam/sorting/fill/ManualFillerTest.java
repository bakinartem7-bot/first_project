package astonteam.sorting.fill;

import astonteam.sorting.TestSupport;
import astonteam.sorting.common.Limits;
import astonteam.sorting.console.ConsoleInput;
import astonteam.sorting.console.EndOfInputException;
import astonteam.sorting.model.Car;
import astonteam.sorting.validation.CarValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ManualFillerTest {

    private final CarValidator validator = new CarValidator();

    @Test
    void fillReadsRequestedNumberOfCars() {
        TestSupport.Outcome outcome = TestSupport.run(
                "150\nToyota\n2015\n90\nBMW\n2010\n", () -> {
                    ConsoleInput input = new ConsoleInput();
                    List<Car> cars = new ManualFiller(input, validator).fill(2);
                    assertEquals(2, cars.size());
                    assertEquals("Toyota", cars.get(0).getModel());
                    assertEquals(150, cars.get(0).getPower());
                    assertEquals("BMW", cars.get(1).getModel());
                    assertEquals(2010, cars.get(1).getYear());
                    input.close();
                });
        assertNull(outcome.error());
    }

    @Test
    void fillRejectsNonPositiveSize() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            ManualFiller filler = new ManualFiller(input, validator);
            assertThrows(IllegalArgumentException.class, () -> filler.fill(0));
            assertThrows(IllegalArgumentException.class, () -> filler.fill(-1));
            input.close();
        });
    }

    @Test
    void fillRejectsTooBigSize() {
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            ManualFiller filler = new ManualFiller(input, validator);
            assertThrows(IllegalArgumentException.class,
                    () -> filler.fill(Limits.MAX_COLLECTION_SIZE + 1));
            input.close();
        });
    }

    @Test
    void fillRetriesAfterNonNumericField() {
        TestSupport.run("abc\n150\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            List<Car> cars = new ManualFiller(input, validator).fill(1);
            assertEquals(1, cars.size());
            assertEquals(150, cars.get(0).getPower());
            input.close();
        });
    }

    @Test
    void fillRetriesAfterValidationFailure() {
        TestSupport.run("0\nToyota\n2015\n150\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            List<Car> cars = new ManualFiller(input, validator).fill(1);
            assertEquals(1, cars.size());
            assertEquals(150, cars.get(0).getPower());
            input.close();
        });
    }

    @Test
    void fillRetriesAfterEmptyModel() {
        TestSupport.run("\nToyota\n150\nToyota\n2015\n", () -> {
            ConsoleInput input = new ConsoleInput();
            List<Car> cars = new ManualFiller(input, validator).fill(1);
            assertEquals("Toyota", cars.get(0).getModel());
            input.close();
        });
    }

    @Test
    void fillFailsOnEndOfInput() {
        TestSupport.run("150\n", () -> {
            ConsoleInput input = new ConsoleInput();
            ManualFiller filler = new ManualFiller(input, validator);
            assertThrows(EndOfInputException.class, () -> filler.fill(1));
            input.close();
        });
    }

    @Test
    void constructorRejectsNullDependencies() {
        assertThrows(NullPointerException.class, () -> new ManualFiller(null, validator));
        TestSupport.run("", () -> {
            ConsoleInput input = new ConsoleInput();
            assertThrows(NullPointerException.class, () -> new ManualFiller(input, null));
            input.close();
        });
    }
}
