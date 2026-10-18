package astonteam.sorting.search;

import astonteam.sorting.collection.CarArrayList;
import astonteam.sorting.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Многопоточный утилитарный класс для подсчёта вхождений автомобиля в коллекцию.
 */
public final class CarCounter {

    /**
     * Подсчитывает количество вхождений заданного автомобиля в коллекцию,
     * разбивая работу на части и выполняя их в пуле потоков.
     *
     * @param cars   коллекция автомобилей
     * @param target искомый автомобиль
     * @return количество вхождений
     * @throws NullPointerException если {@code cars} или {@code target} равны {@code null}
     */
    public int countOccurrences(CarArrayList cars, Car target) {
        Objects.requireNonNull(cars, "cars");
        Objects.requireNonNull(target, "target");

        int size = cars.size();
        if (size == 0) {
            return 0;
        }

        // Определяем количество потоков (не больше, чем элементов в коллекции)
        int threadCount = Math.min(size, Math.max(1, Runtime.getRuntime().availableProcessors()));
        int chunkSize = size / threadCount;
        int remainder = size % threadCount;

        List<Callable<Integer>> tasks = new ArrayList<>();
        int start = 0;

        // Создаем задачи, разбивая индексы на непересекающиеся диапазоны
        for (int i = 0; i < threadCount; i++) {
            int currentChunkSize = chunkSize + (i < remainder ? 1 : 0);
            int end = start + currentChunkSize;
            int finalStart = start;
            int finalEnd = end;

            tasks.add(() -> {
                int localCount = 0;
                for (int j = finalStart; j < finalEnd; j++) {
                    if (cars.get(j).equals(target)) {
                        localCount++;
                    }
                }
                return localCount;
            });
            start = end;
        }

        int totalCount = 0;

        // Java 21 поддерживает try-with-resources для ExecutorService
        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            // invokeAll запускает все задачи и ждет их завершения
            List<Future<Integer>> futures = executor.invokeAll(tasks);

            // Корректно суммируем результаты
            for (Future<Integer> future : futures) {
                totalCount += future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Выполнение подсчета было прервано", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Ошибка при выполнении задачи подсчета", e.getCause());
        }

        return totalCount;
    }
}
