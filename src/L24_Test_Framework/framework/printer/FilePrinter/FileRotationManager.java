package L24_Test_Framework.framework.printer.FilePrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileRotationManager {
    private static final long MAX_FILE_SIZE = 1024L * 1024 * 2;

    public static boolean shouldRotate(long bytesWritten) {
        return bytesWritten >= MAX_FILE_SIZE;
    }

    public static RotationResult rotate(Path currentFilePath) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path rotatedPath = Path.of(currentFilePath + "." + timestamp + ".rotated");
        try {
            Files.move(currentFilePath, rotatedPath, StandardCopyOption.REPLACE_EXISTING);
            System.err.println("[INFO] Log file rotated: " + rotatedPath);
            return new RotationResult(true, null);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to rotate log file: " + currentFilePath);
            e.printStackTrace();
            String fallbackName = FileNameGenerator.generateFileName("fallback_log");
            return new RotationResult(false, fallbackName);
        }
    }

    public static class RotationResult {
        public final boolean success;
        public final String fallbackFileName;

        public RotationResult(boolean success, String fallbackFileName) {
            this.success = success;
            this.fallbackFileName = fallbackFileName;
        }
    }
}