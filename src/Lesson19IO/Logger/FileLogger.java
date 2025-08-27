package Lesson19IO.Logger;

import Lesson19IO.LoggerConfig.FileLoggerConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements Logger {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final FileLoggerConfiguration configuration;
    private PrintWriter writer;
    private File currentFile;
    private long bytesWritten;

    public FileLogger(FileLoggerConfiguration config) throws IOException {
        this.configuration = config;
        createNewFile();
    }

    private void createNewFile() throws IOException {
        prepareDirectory();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String filename = Paths.get(configuration.getDirectoryPath(),
                configuration.getFilenamePrefix() + "_" + timestamp + ".log").toString();

        currentFile = new File(filename);
        currentFile.createNewFile();
        writer = new PrintWriter(new BufferedWriter(new FileWriter(currentFile)), true);
        bytesWritten = 0L;
    }

    private void checkAndRotateFile() throws IOException {
        if (bytesWritten >= configuration.getMaxFileSizeBytes()) {
            writer.close();
            createNewFile();
        }
    }

    private void writeMessage(LoggingLevel level, String message) throws IOException {
        LoggingLevel activeLevel = configuration.getLevel();
        if (!activeLevel.allows(level)) {
            return;
        }
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String formattedMessage = String.format(configuration.getPattern(),
                timestamp, level.name(), message);
        writer.println(formattedMessage);
        bytesWritten += formattedMessage.length();
        checkAndRotateFile();
    }

    private void prepareDirectory() throws IOException {
        Path dirPath = Paths.get(configuration.getDirectoryPath());
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    @Override
    public void info(String message) {
        try {
            writeMessage(LoggingLevel.INFO, message);
        } catch (IOException e) {
            System.err.println("Error write to log file: " + e.getMessage());
        }
    }

    @Override
    public void debug(String message) {
        try {
            writeMessage(LoggingLevel.DEBUG, message);
        } catch (IOException e) {
            System.err.println("Error write to log file: " + e.getMessage());
        }
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}