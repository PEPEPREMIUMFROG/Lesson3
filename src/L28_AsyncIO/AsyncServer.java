package L28_AsyncIO;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncServer {

    private static final int PORT = 8086;
    private static final int MAX_CLIENTS = 3;

    private Selector selector;
    private ServerSocketChannel serverChannel;
    private int clientCount = 0;

    public static void main(String[] args) throws Exception {
        new AsyncServer().start();
    }

    public void start() throws Exception {
        initServer();
        runEventLoop();
        shutdown();
    }

    private void initServer() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(PORT));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started on port " + PORT);
        System.out.println("Waiting for " + MAX_CLIENTS + " clients to connect");
    }

    private void runEventLoop() throws IOException {
        while (shouldKeepRunning()) {
            selector.select();
            processSelectedKeys();
        }
    }

    private boolean shouldKeepRunning() {
        return clientCount < MAX_CLIENTS || selector.keys().size() > 1;
    }

    private void processSelectedKeys() throws IOException {
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            if (key.isValid()) {
                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = server.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            clientKey.attach(new ConcurrentLinkedQueue<>());
            System.out.println("Client connected: " + clientChannel.getRemoteAddress());
            clientCount++;
            if (clientCount >= MAX_CLIENTS) {
                System.out.println("Max clients reached. No longer accepting.");
                key.interestOps(0);
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String input = new String(data).trim();
            System.out.println("Received from " + clientChannel.getRemoteAddress() + ": " + input);
            String response = "Echo: " + input + "\n";
            Queue<String> responseQueue = (Queue<String>) key.attachment();
            responseQueue.add(response);
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        } else if (bytesRead == -1) {
            handleClose(key, clientChannel);
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        Queue<String> responseQueue = (Queue<String>) key.attachment();
        String response = responseQueue.poll();
        if (response != null) {
            ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
            clientChannel.write(buffer);
            if (!buffer.hasRemaining()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            } else {
                responseQueue.add(response);
            }
        }
    }

    private void handleClose(SelectionKey key, SocketChannel clientChannel) throws IOException {
        System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
        key.cancel();
        clientChannel.close();
    }

    private void shutdown() throws IOException {
        System.out.println(" === Shutting down server ===");
        selector.close();
        serverChannel.close();
    }
}
