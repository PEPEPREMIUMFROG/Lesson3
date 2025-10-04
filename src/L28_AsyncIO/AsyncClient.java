package L28_AsyncIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;

public class AsyncClient {

    public static final String HOST_NAME = "localhost";
    public static final int PORT = 8086;
    private static final Random rnd = new Random();

    private final Selector selector;
    private final SocketChannel channel;
    private final String message;
    private final long sendInterval;

    private long lastSendTime = 0;

    public AsyncClient(String message, long sendInterval) throws IOException {
        this.message = message;
        this.sendInterval = sendInterval;
        this.selector = Selector.open();
        this.channel = SocketChannel.open();
    }

    public void run() throws Exception {
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(HOST_NAME,PORT));
        channel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            handleSend();
            selector.select(50);
            processEvents();
        }
    }


    private void handleSend() throws IOException {
        if (channel.isConnected() && System.currentTimeMillis() - lastSendTime >= sendInterval) {
            channel.write(ByteBuffer.wrap((message + rnd.nextInt(10)+"\n").getBytes()));
            System.out.println("Sent: "+ message);
            lastSendTime = System.currentTimeMillis();
        }
    }

    private void processEvents() throws IOException {
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            if (key.isConnectable()) {
                handleConnect(key);
            }
            if (key.isReadable()) {
                handleRead();
            }
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        if (channel.finishConnect()) {
            key.interestOps(SelectionKey.OP_READ);
            System.out.println("Connected.");
        }
    }

    private void handleRead() throws IOException {
        var buf = ByteBuffer.allocate(128);
        if (channel.read(buf) > 0) {
            buf.flip();
            System.out.print("Server: " + new String(buf.array(), 0, buf.remaining()));
        }
    }
}