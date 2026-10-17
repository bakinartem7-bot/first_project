package astonteam.sorting;
//
import astonteam.sorting.model.Car;
import java.util.List;
import java.util.concurrent.Callable;

public class CarCountTask implements Callable<Integer> {
    private final List<Car> cars;
    private final Car target;
    private final int start;
    private final int end;

    public CarCountTask(List<Car> cars, Car target, int start, int end) {
        this.cars = cars;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() {
        int count = 0;
        for (int i = start; i < end; i++) {
            Car current = cars.get(i);
            // Важно: current != null на случай, если в коллекции есть пропуски
            if (current != null && current.equals(target)) {
                count++;
            }
        }
        return count;
    }
}
