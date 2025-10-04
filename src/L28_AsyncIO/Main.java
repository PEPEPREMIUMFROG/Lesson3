package L28_AsyncIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String[][] clientConfigs = {
                {"msg from client 1", "1350"},
                {"msg from client 2", "1500"},
                {"msg from client 3", "1750"}
        };
        List<Thread> threads = new ArrayList<>();
        List<AsyncClient> clients = new ArrayList<>();
        for (String[] config : clientConfigs) {
            String message = config[0];
            long interval = Long.parseLong(config[1]);
            AsyncClient client = new AsyncClient(message, interval);
            clients.add(client);
            Thread thread = new Thread(() -> {
                try {
                    client.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
