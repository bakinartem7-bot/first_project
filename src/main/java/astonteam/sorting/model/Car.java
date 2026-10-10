package astonteam.sorting.model;

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
            return new Car(this);
        }
    }
}
