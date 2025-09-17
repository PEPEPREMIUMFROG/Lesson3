package L24_Test_Framework.framework.printer.FilePrinter;

import L24_Test_Framework.framework.executions.Execution;
import L24_Test_Framework.framework.printer.Printer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FilePrinter implements Printer, AutoCloseable {
    private static final String DEFAULT_DIRECTORY = "src/L24_Test_Framework/project/tests/logs";
    private static final String DEFAULT_TEMPLATE = "yyyy-MM-dd HH:mm:ss ";
    private static final String DEFAULT_NAME = "test";
    private final String template;
    private String fileName;
    private final Path directoryPath;
    private FileWriteWrapper fileWriter;
    private boolean isInitialized = false;

    public FilePrinter(String template, String fileName, String directory) {
        this.template = template != null ? template : DEFAULT_TEMPLATE;
        this.fileName = FileNameGenerator.generateFileName(fileName);
        this.directoryPath = Paths.get(directory != null ?
                directory : DEFAULT_DIRECTORY).toAbsolutePath().normalize();
    }

    public FilePrinter(String template, String baseFileName) {
        this(template, baseFileName, DEFAULT_DIRECTORY);
    }

    public FilePrinter(String baseFileName) {
        this(DEFAULT_TEMPLATE, baseFileName);
    }

    public FilePrinter() {
        this(DEFAULT_NAME);
    }


    @Override
    public synchronized void write(List<Execution> executions) {
        if (!isInitialized) {
            initializeWriter();
            isInitialized = true;
        }
        String message = MessageFormatter.formatExecutions(executions, template);
        fileWriter.write(message);
        if (FileRotationManager.shouldRotate(fileWriter.getBytesWritten())) {
            rotateFile();
        }
    }

    private void initializeWriter() {
        try {
            Path filePath = directoryPath.resolve(fileName);
            fileWriter = new FileWriteWrapper(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file writer", e);
        }
    }

    private void rotateFile() {
        fileWriter.close();
        Path currentFilePath = directoryPath.resolve(fileName);
        var rotationResult = FileRotationManager.rotate(currentFilePath);
        if (!rotationResult.success) {
            fileName = rotationResult.fallbackFileName;
        }
        initializeWriter();
        isInitialized = true;
    }

    @Override
    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

}