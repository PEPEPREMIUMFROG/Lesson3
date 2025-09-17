package L24_Test_Framework.framework.printer.FilePrinter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class FileNameGenerator {
    private static final Set<Character> ILLEGAL_CHARS = Set.of('/', '\\', ':', '*', '?', '"', '<', '>', '|');

    public static String generateFileName(String baseFileName) {
        if (baseFileName == null || baseFileName.isEmpty() || containsIllegalCharacters(baseFileName)) {
            System.err.println("!! Invalid filename provided: '" + baseFileName + "'. Using default name 'test'.");
            baseFileName = "test";
        }
        int lastDotIndex = baseFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            baseFileName = baseFileName.substring(0, lastDotIndex);
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        return baseFileName + "_" + timestamp + ".txt";
    }

    private static boolean containsIllegalCharacters(String fileName) {
        return fileName.chars().anyMatch(ch -> ILLEGAL_CHARS.contains((char) ch));
    }
}