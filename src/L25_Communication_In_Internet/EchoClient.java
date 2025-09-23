package L25_Communication_In_Internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8087;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to server");
            Thread readerThread = new Thread(() -> {
                String inputLine;
                try {
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received: " + inputLine);
                        if (!inputLine.startsWith("Echo:")) {
                            out.println("Echo: " + inputLine);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            });
            readerThread.start();
            String userInput;
            while (!(userInput = scanner.nextLine()).equals("exit")) {
                out.println(userInput);
            }
            readerThread.interrupt();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + HOST);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}