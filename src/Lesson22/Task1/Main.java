package Lesson22.Task1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadSafeList<String> list = new ThreadSafeList<>();
        ExecutorService executor = Executors.newFixedThreadPool(6);

        System.out.println("\n1. Launching readers and remover...");
        executor.submit(new ReaderTask(list, "Reader-Alpha", 4));
        executor.submit(new ReaderTask(list, "Reader-Beta", 4));
        executor.submit(new RemoverTask(list, "Remover-One", 2));

        System.out.println("\n2. Waiting 1 second...");
        Thread.sleep(1000);

        System.out.println("\n3. Launching adders...");
        executor.submit(new AdderTask(list, "ItemOne", 3));

        System.out.println("\n4. Waiting 5 seconds for tasks to interact...");
        Thread.sleep(5000);

        System.out.println("\n5. Launching another remover and reader...");
        executor.submit(new RemoverTask(list, "Remover-Two", 2));
        executor.submit(new AdderTask(list, "ItemTwo", 2));

        System.out.println("\n6. Waiting 4 more seconds...");
        Thread.sleep(4000);
        System.out.println("\n\nShutting down...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(8, TimeUnit.SECONDS)) {
                System.err.println("Forcing shutdown!");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.out.println("\nFinal list: " + list.toString());
    }
}