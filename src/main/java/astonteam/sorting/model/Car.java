package astonteam.sorting.model;

import astonteam.sorting.validation.CarValidator;
import astonteam.sorting.validation.ValidationResult;

import java.util.Objects;

/**
 * Неизменяемая модель автомобиля с мощностью, моделью и годом выпуска.
 */
public final class Car {

    /** Мощность двигателя. */
    private final int power;
    /** Название модели. */
    private final String model;
    /** Год выпуска. */
    private final int year;

    /**
     * Создаёт автомобиль на основе настроенного билдера.
     *
     * @param builder билдер с заполненными полями
     */
    private Car(Builder builder) {
        this.power = builder.power;
        this.model = builder.model;
        this.year = builder.year;
    }

    /**
     * Возвращает мощность двигателя.
     *
     * @return мощность
     */
    public int getPower() {
        return power;
    }

    /**
     * Возвращает название модели.
     *
     * @return модель
     */
    public String getModel() {
        return model;
    }

    /**
     * Возвращает год выпуска.
     *
     * @return год
     */
    public int getYear() {
        return year;
    }

    /**
     * Создаёт новый билдер автомобиля.
     *
     * @return новый {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Сравнивает автомобиль с другим объектом по всем полям.
     *
     * @param o объект для сравнения
     * @return {@code true}, если объекты равны
     */
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

    /**
     * Возвращает хеш-код автомобиля.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(power, model, year);
    }

    /**
     * Возвращает читаемое строковое представление автомобиля.
     *
     * @return строка вида «Мощность: 100; Модель: Audi; Год: 2002»
     */
    @Override
    public String toString() {
        return "Мощность: " + power + "; Модель: " + model + "; Год: " + year;
    }

    /**
     * Билдер для пошагового создания автомобиля.
     */
    public static final class Builder {

        /**
         * Создаёт пустой построитель автомобиля.
         */
        public Builder() {
        }

        /** Мощность двигателя. */
        private int power;
        /** Название модели. */
        private String model;
        /** Год выпуска. */
        private int year;

        /**
         * Задаёт мощность двигателя.
         *
         * @param power мощность
         * @return этот билдер
         */
        public Builder power(int power) {
            this.power = power;
            return this;
        }

        /**
         * Задаёт название модели.
         *
         * @param model модель
         * @return этот билдер
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * Задаёт год выпуска.
         *
         * @param year год
         * @return этот билдер
         */
        public Builder year(int year) {
            this.year = year;
            return this;
        }

        /**
         * Проверяет поля и создаёт автомобиль.
         *
         * @return созданный {@link Car}
         * @throws NullPointerException     если модель не задана
         * @throws IllegalArgumentException если данные не проходят валидацию
         */
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
