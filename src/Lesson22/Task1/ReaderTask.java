package Lesson22.Task1;

public class ReaderTask implements Runnable {
    private final ThreadSafeList<String> list;
    private final String readerName;
    private final int iterations;

    public ReaderTask(ThreadSafeList<String> list, String readerName, int iterations) {
        this.list = list;
        this.readerName = readerName;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " (" + readerName + ") started reading.");
        try {
            for (int i = 0; i < iterations; i++) {
                try {
                    String item = list.get(0);
                    System.out.println(name + " (" + readerName + ") read: " + item);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(name + " (" + readerName + ") found list empty or item missing.");
                }
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " (" + readerName + ") was interrupted.");
        }
        System.out.println(name + " (" + readerName + ") finished.");
    }
}