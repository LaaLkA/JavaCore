import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        BlockingQueue queue = new BlockingQueue(2);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        Future<Object> consumer = pool.submit(queue::dequeue);
        Thread.sleep(500);
        System.out.println("Потребитель ждёт: " + !consumer.isDone());

        queue.enqueue("hello");
        System.out.println("Получено: " + consumer.get(2, TimeUnit.SECONDS));

        Set<Object> received = ConcurrentHashMap.newKeySet();
        for (int i = 0; i < 2; i++) {
            int start = i * 1000;
            pool.submit(() -> {
                for (int number = start; number < start + 1000; number++) {
                    queue.enqueue(number);
                }
                return null;
            });
            pool.submit(() -> {
                for (int count = 0; count < 1000; count++) {
                    received.add(queue.dequeue());
                }
                return null;
            });
        }

        pool.shutdown();
        System.out.println("Все задачи завершились: " + pool.awaitTermination(5, TimeUnit.SECONDS));
        System.out.println("Получено разных чисел: " + received.size() + " из 2000");
        System.out.println("Осталось в очереди: " + queue.size());

        pool.shutdownNow();
    }
}
