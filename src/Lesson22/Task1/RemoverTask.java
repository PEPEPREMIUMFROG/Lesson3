package Lesson22.Task1;

public class RemoverTask implements Runnable {
    private final ThreadSafeList<String> list;
    private final String removerName;
    private final int iterations;

    public RemoverTask(ThreadSafeList<String> list, String removerName, int iterations) {
        this.list = list;
        this.removerName = removerName;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " (" + removerName + ") started removing.");
        try {
            for (int i = 0; i < iterations; i++) {
                try {
                    String item = list.remove(0);
                    System.out.println(name + " (" + removerName + ") removed: " + item);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(name + " (" + removerName + ") found nothing to remove.");
                }
                Thread.sleep(400);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " (" + removerName + ") was interrupted.");
        }
        System.out.println(name + " (" + removerName + ") finished.");
    }
}