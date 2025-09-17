package L24_Test_Framework.framework.printer.FilePrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriteWrapper implements AutoCloseable {
    private final PrintWriter writer;
    private long bytesWritten;
    private final Path filePath;

    public FileWriteWrapper(Path filePath) throws IOException {
        this.filePath = filePath;
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.APPEND);
        OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        this.writer = new PrintWriter(new BufferedWriter(osw), true);
        this.bytesWritten = Files.size(filePath);
    }

    public void write(String message) {
        if (message == null) return;
        writer.write(message);
        writer.flush();
        bytesWritten += message.getBytes(StandardCharsets.UTF_8).length;
        if (writer.checkError()) {
            throw new RuntimeException("Error occurred while writing to file: " + filePath);
        }
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                System.err.println("[WARN] Error closing writer for file: " + filePath);
                e.printStackTrace();
            }
        }
    }
}