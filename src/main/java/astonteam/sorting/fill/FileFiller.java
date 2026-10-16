package astonteam.sorting.fill;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.common.Limits;
import astonteam.sorting.io.CarLineFormat;
import astonteam.sorting.model.Car;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;


public final class FileFiller {
    public CarArrayList fill(Path path) {
        Objects.requireNonNull(path, "path");

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Файл не существует: " + path);
        }
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Путь не является файлом: " + path);
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("Нет прав на чтение файла: " + path);
        }

        CarArrayList cars = new CarArrayList();

        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.filter(line -> !line.isBlank())
                    .forEach(line -> {
                        if (cars.size() >= Limits.MAX_COLLECTION_SIZE) {
                            throw new IllegalArgumentException("Превышен максимальный размер коллекции: " + Limits.MAX_COLLECTION_SIZE);
                        }
                        try {
                            Car car = CarLineFormat.parse(line);
                            cars.add(car);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Ошибка в строке \"" + line + "\": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка при чтении файла: " + path, e);
        }

        return cars;
    }
}
