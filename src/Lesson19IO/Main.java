package Lesson19IO;

import Lesson19IO.LogConfigLoader.FileLoggerConfigurationLoader;
import Lesson19IO.Logger.FileLogger;
import Lesson19IO.LoggerConfiguration.FileLoggerConfiguration;

public class Main {
    public static void main(String[] args) {
        try {
            FileLoggerConfigurationLoader loader = new FileLoggerConfigurationLoader();
            FileLoggerConfiguration debugConfig = loader.load("debug.config.properties");
            FileLogger debugLogger = new FileLogger(debugConfig);
            FileLoggerConfiguration infoConfig = loader.load("info.config.properties");
            FileLogger infoLogger = new FileLogger(infoConfig);
            debugLogger.info("Приложение запущено.");
            debugLogger.debug("Отладка: начальное состояние системы.");
            debugLogger.close();


            infoLogger.info("Отладочный логгер закрыт");
            infoLogger.info("Информационный логгер запущен");
            infoLogger.debug("Этого сообщения не будет");
            infoLogger.close();
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
}
