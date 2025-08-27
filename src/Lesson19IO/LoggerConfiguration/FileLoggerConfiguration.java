package Lesson19IO.LoggerConfiguration;

import Lesson19IO.Logger.LoggingLevel;

public class FileLoggerConfiguration implements LoggerConfiguration {
    private final String directoryPath;
    private final String filenamePrefix;
    private final LoggingLevel activeLevel;
    private final long maxFileSizeBytes;
    private final String logFormat;

    public FileLoggerConfiguration(
            String directoryPath,
            String filenamePrefix,
            LoggingLevel activeLevel,
            long maxFileSizeBytes,
            String logFormat
    ) {
        this.directoryPath = directoryPath;
        this.filenamePrefix = filenamePrefix;
        this.activeLevel = activeLevel;
        this.maxFileSizeBytes = maxFileSizeBytes;
        this.logFormat = logFormat != null ? logFormat : "[%s][%s] Message: %s";
    }

    @Override
    public LoggingLevel getLevel() {
        return activeLevel;
    }

    @Override
    public String getPattern() {
        return logFormat;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getFilenamePrefix() {
        return filenamePrefix;
    }

    public long getMaxFileSizeBytes() {
        return maxFileSizeBytes;
    }
}