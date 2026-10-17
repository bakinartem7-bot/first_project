package astonteam.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CarCounter {

    public static int countOccurrences(List<Car> cars, Car target) throws InterruptedException, ExecutionException {
        if (cars == null || cars.isEmpty() || target == null) {
            return 0;
        }

        int size = cars.size();
        // Берём столько потоков, сколько ядер у процессора (или 1, если вдруг 0)
        int numThreads = Math.max(1, Runtime.getRuntime().availableProcessors());
        int chunkSize = (size + numThreads - 1) / numThreads;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        try {
            List<Future<Integer>> futures = new ArrayList<>();

            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize;
                if (start >= size) {
                    break;
                }
                int end = Math.min(start + chunkSize, size);

                Callable<Integer> task = new CarCountTask(cars, target, start, end);
                futures.add(executor.submit(task));
            }

            int totalCount = 0;
            for (Future<Integer> future : futures) {
                totalCount += future.get();
            }

            return totalCount;
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }
    }
}