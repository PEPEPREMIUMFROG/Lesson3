package Lesson19IO.LogConfigLoader;
import Lesson19IO.LoggerConfig.FileLoggerConfiguration;
import Lesson19IO.Logger.LoggingLevel;

import java.util.Scanner;

public class FileLoggerConfigurationLoader extends AbstractConfigurationLoader<FileLoggerConfiguration> {

    @Override
    protected FileLoggerConfiguration parse(Scanner scanner) throws Exception {
        String directoryPath = "";
        String filenamePrefix = "";
        LoggingLevel loggingLevel = LoggingLevel.INFO;
        long maxFileSizeBytes = 1024;
        String logFormat = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("=", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid configuration format in line: " + line);
            }

            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                case "directory_path":
                    directoryPath = value;
                    break;
                case "filename_prefix":
                    filenamePrefix = value;
                    break;
                case "logging_level":
                    loggingLevel = LoggingLevel.valueOf(value.toUpperCase());
                    break;
                case "max_file_size_bytes":
                    maxFileSizeBytes = Long.parseLong(value);
                    break;
                case "log_format":
                    logFormat = value;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown configuration parameter: " + key);
            }
        }

        return new FileLoggerConfiguration(directoryPath, filenamePrefix, loggingLevel, maxFileSizeBytes, logFormat);
    }
}