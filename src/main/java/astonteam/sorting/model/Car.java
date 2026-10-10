package astonteam.sorting.model;

import astonteam.sorting.validation.CarValidator;
import astonteam.sorting.validation.ValidationResult;

import java.util.Objects;

public final class Car {

    private final int power;
    private final String model;
    private final int year;

    private Car(Builder builder) {
        this.power = builder.power;
        this.model = builder.model;
        this.year = builder.year;
    }

    public int getPower() {
        return power;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car other)) {
            return false;
        }
        return power == other.power
                && year == other.year
                && Objects.equals(model, other.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(power, model, year);
    }

    @Override
    public String toString() {
        return "Car{power=" + power + ", model='" + model + "', year=" + year + '}';
    }

    public static final class Builder {

        private int power;
        private String model;
        private int year;

        public Builder power(int power) {
            this.power = power;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Car build() {
            Objects.requireNonNull(model, "model");
            ValidationResult result = new CarValidator().validate(power, model, year);
            if (!result.isValid()) {
                throw new IllegalArgumentException(result.message());
            }
            return new Car(this);
        }
    }
}
