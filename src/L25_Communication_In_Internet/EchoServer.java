package L25_Communication_In_Internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private static final int PORT = 8087;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));
                 Scanner scanner = new Scanner(System.in)) {
                System.out.println("Client connected");
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
                        System.err.println("Error reading from client: " + e.getMessage());
                    }
                });
                readerThread.start();
                String userInput;
                while (!(userInput = scanner.nextLine()).equals("exit")) {
                    out.println(userInput);
                }
                readerThread.interrupt();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
