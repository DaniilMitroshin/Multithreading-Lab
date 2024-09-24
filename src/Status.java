import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum Status {
    UNKNOWN,
    STOPPING,
    RUNNING,
    FATAL_ERROR;

    private static final List<Status> VALUES = Arrays.asList(Status.values());
    private static final int SIZE = VALUES.size() - 1;  // Исключаем первый элемент
    private static final Random RANDOM = new Random();

    public static Status getRandomStatus() {
        // Генерируем случайное число от 1 до SIZE включительно
        return VALUES.get(RANDOM.nextInt(SIZE) + 1);
    }
}