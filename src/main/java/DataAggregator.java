import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class DataAggregator {

    private double fetchPrice(String name) {
        simulateService();
        return 899.99;
    }

    private String fetchDescription(String name) {
        simulateService();
        return "desc";
    }

    private double fetchRating(String name) {
        simulateService();
        return 4.2;
    }

    public ProductInfo aggregateProductInfo(String name) {

        CompletableFuture<Double> futurePrice = CompletableFuture
                .supplyAsync(() -> fetchPrice(name))
                .exceptionally(e -> 0.0);

        CompletableFuture<String> futureDesc = CompletableFuture
                .supplyAsync(() -> fetchDescription(name))
                .exceptionally(e -> "Нет данных");

        CompletableFuture<Double> futureRating = CompletableFuture
                .supplyAsync(() -> fetchRating(name))
                .exceptionally(e -> 0.0);

        CompletableFuture.allOf(futureDesc, futurePrice, futureRating).join();

        return new ProductInfo(name, futurePrice.join(), futureDesc.join(), futureRating.join());

    }

    private void simulateService() {
        int rnd = randomNumber(0, 100);
        int pause = randomNumber(1000, 3001);

        try {
            sleep(pause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        if (rnd < 20) {
            throw new RuntimeException("Some Error");
        }

    }

    private int randomNumber(int head, int tail) {
        return ThreadLocalRandom.current().nextInt(head, tail);
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            ProductInfo info = new DataAggregator().aggregateProductInfo("Ноутбук");

            System.out.println(info);
            System.out.println("Time - " + (System.currentTimeMillis() - start) / 1000D + " c");
            System.out.println();
        }

    }

}
