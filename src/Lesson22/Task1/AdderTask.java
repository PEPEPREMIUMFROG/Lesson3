package Lesson22.Task1;

public class AdderTask implements Runnable {
    private final ThreadSafeList<String> list;
    private final String itemName;
    private final int iterations;

    public AdderTask(ThreadSafeList<String> list, String itemName, int iterations) {
        this.list = list;
        this.itemName = itemName;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " (" + itemName + ") started adding.");
        try {
            for (int i = 0; i < iterations; i++) {
                try {
                    String itemToAdd = itemName + (i + 1);
                    list.add(itemToAdd);
                    System.out.println(name + " (" + itemName + ") added: " + itemToAdd);
                } catch (Exception e) {
                    System.out.println(name + " (" + itemName + ") failed to add item: " + e.getMessage());
                }
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " (" + itemName + ") was interrupted.");
        }
        System.out.println(name + " (" + itemName + ") finished.");
    }
}